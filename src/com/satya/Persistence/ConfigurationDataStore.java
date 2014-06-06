/*package com.satya.Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.User;
import com.satya.Configuration.Configuration;


public class ConfigurationDataStore extends AbstractDataStore{

    private final static String propsFileName = "ebayListing.properties";

    private final static String CONFIG_GET_SQL =
            " SELECT * FROM configuration ";
    private final static String FIND_SYSTEM_CONFIG_SQL = 
    	"select configValue from configuration where configKey = ? and userseq = ?";

    private final static String DELETE_CONFIGS_SQL = 
    	"DELETE from configuration where userSeq = ? and userseq <> 1";
    
    private final static String SELECT_SUMMARYEMAILACTIVATED_USERSEQS =
    	"select DISTINCT(userseq) from configuration where configKey='"+ Configuration.IS_ACCOUNTSUMMARY_ENABLED +"' and configValue='true' and userseq <> 1";
    
    
    private static Configuration config = null;

    private static ConfigurationDataStore configDataStore =
            new ConfigurationDataStore();

    public static ConfigurationDataStore getInstance(){
        return configDataStore;
    }

    public void save(User user,String configKey,String configValue){
        Connection c = DbConnectionMgr.getConnection();
        PreparedStatement ps = null;
        long userSeq = user.getSeq();
        SimpleDateFormat sdf = Configuration.sdf;
        String Sql = INSERT_CONFIGURATION_SQL;        
        String value  = findConfigValue(userSeq, configKey);
        //new check use this boolena
        boolean isExists = isConfigurationExists(userSeq, configKey);
        
        if (isExists == true)
    	   Sql  = CONFIG_UPDATE_SQL;       
        try{
            ps = c.prepareStatement(Sql);
            if(configValue==null){
            	ps.setNull(1,Types.NULL);	
            }else{
            	ps.setString(1,configValue);
            }
            	ps.setString(2,configKey);
                ps.setLong(3, userSeq);
                ps.execute();           
            setConfiguartion(config);
        }catch(SQLException sqe){
            throw new RuntimeException("SQL Error : " + sqe);
        }finally{
            try{
                if(ps != null) ps.close();
            }
            catch(Exception e){}
            DbConnectionMgr.release(c);
        }
    }
    public void save(User user,String configKey,Object NullObj){
        Connection c = DbConnectionMgr.getConnection();
        PreparedStatement ps = null;
        long userSeq = user.getSeq();
        SimpleDateFormat sdf = Configuration.sdf;
        String Sql = INSERT_CONFIGURATION_SQL;        
        String value  = findConfigValue(userSeq, configKey);
        //new check use this boolena
        boolean isExists = isConfigurationExists(userSeq, configKey);
        
        if (isExists == true)
    	   Sql  = CONFIG_UPDATE_SQL;       
        try{
            ps = c.prepareStatement(Sql);
            	if(NullObj == null){
            		ps.setNull(1,Types.NULL);	
            	}
                ps.setString(2,configKey);
                ps.setLong(3, userSeq);
                ps.execute();           
            setConfiguartion(config);
        }catch(SQLException sqe){
            throw new RuntimeException("SQL Error : " + sqe);
        }finally{
            try{
                if(ps != null) ps.close();
            }
            catch(Exception e){}
            DbConnectionMgr.release(c);
        }
    }
    
    private boolean isConfigurationExists(long userSeq, String ConfigKey){
    	 Connection conn=DbConnectionMgr.getConnection();
         PreparedStatement ps=null;
         String configValue = null;
         ResultSet rs = null;
         try{
             ps = conn.prepareStatement(FIND_SYSTEM_CONFIG_SQL);             
             ps.setString(1,ConfigKey);
             ps.setLong(2,userSeq);             
             rs= ps.executeQuery();              
             if( rs.next()){
           	  return true;
             }
         }catch(SQLException exception){
             throw new RuntimeException(exception.getMessage());
         }
         finally{
             DbConnectionMgr.release(conn);
         }
         
         return false;
    }
    public String findConfigValue(long userSeq,String ConfigKey) {
    	  Connection conn=DbConnectionMgr.getConnection();
          PreparedStatement ps=null;
          String configValue = null;
          ResultSet rs = null;
          try{
              ps = conn.prepareStatement(FIND_SYSTEM_CONFIG_SQL);             
              ps.setString(1,ConfigKey);
              ps.setLong(2,userSeq);             
              rs= ps.executeQuery();              
              if( rs.next()){
              configValue = (String)rs.getString("configValue");
              }
          }catch(SQLException exception){
              throw new RuntimeException(exception.getMessage());
          }
          finally{
              DbConnectionMgr.release(conn);
          }
          if (configValue == null){
        	  return null;
          }
          
          return configValue;
      }
     
    public List getSummaryEmailActivatedUserSeqs(){
   	 Connection conn=DbConnectionMgr.getConnection();
        PreparedStatement ps=null;
        String configValue = null;
        ResultSet rs = null;
        List users = new ArrayList();
        try{
            ps = conn.prepareStatement(SELECT_SUMMARYEMAILACTIVATED_USERSEQS);             
            rs= ps.executeQuery();              
            if( rs.next()){
          	  users.add(rs.getInt(1));
            }
        }catch(SQLException exception){
            throw new RuntimeException(exception.getMessage());
        }
        finally{
            DbConnectionMgr.release(conn);
        }
        return users;
        
   }
    
    public void updateRuName(String runame){
        Connection conn=DbConnectionMgr.getConnection();
        PreparedStatement pstmt=null;
        try{
            pstmt=conn.prepareStatement(CONFIG_UPDATE_SQL);
            pstmt.setString(1,runame);
            pstmt.setString(2,Configuration.RUNAME);
            pstmt.executeUpdate();
        }catch(SQLException sqe){
            throw new RuntimeException(sqe);
        }finally{
            try{
                if(pstmt != null) pstmt.close();
                }catch(SQLException e){}
                DbConnectionMgr.release(conn);
        }
    }

    public synchronized Configuration getConfiguration(){
        if(config == null){
            Connection conn=DbConnectionMgr.getConnection();
            PreparedStatement pstmt=null;
            try{
                pstmt=conn.prepareStatement(CONFIG_GET_SQL);
                List configs = executePSQuery(pstmt) ;
                if(configs.size() > 0){
                    config = (Configuration)configs.get(0);
                }
                loadPropertiesInfoConfig(config);
                return config;
            }catch(SQLException exception){
                throw new RuntimeException(exception.getMessage());
            }
            finally{
                DbConnectionMgr.release(conn);
            }
        }
        return config;
    }

    public void loadPropertiesInfoConfig(Configuration config) {
        InputStream is = ConfigurationDataStore.class.getClassLoader().
                         getResourceAsStream(propsFileName);
        if(is == null){
            throw new RuntimeException("Properties file " + propsFileName +
                                       " not found");
        }
        Properties props = new Properties();
        try{
            props.load(is);
        }catch(IOException ie){
            throw new RuntimeException("Error reading " + propsFileName);
        }

        Enumeration enu = props.propertyNames();
        while(enu.hasMoreElements()){
            String propName = (String)enu.nextElement();
            String propVal = props.getProperty(propName);
            logger.info("Setting System Property with key = " +
                        propName + " and value = " +
                        propVal);
            config.addConfigData(propName,propVal);
        }
    }

    public void setConfiguartion(Configuration configuration){
        config=configuration;
    }
    
    public void cleanConfigurations(long userSeq){
    	 Connection conn=DbConnectionMgr.getConnection();
         PreparedStatement pstmt=null;
         try{
             pstmt=conn.prepareStatement(DELETE_CONFIGS_SQL);
             pstmt.setLong(1,userSeq);
             pstmt.executeUpdate();
         }catch(SQLException sqe){
             throw new RuntimeException(sqe);
         }finally{
             try{
                 if(pstmt != null) pstmt.close();
                 }catch(SQLException e){}
                 DbConnectionMgr.release(conn);
         }
    }

    protected Object populateObjectFromResultSet(ResultSet rs)
            throws SQLException{
        Configuration config = new Configuration();
        while(rs.next()){
            String key = (String)rs.getString("configKey");
            String value = (String)rs.getString("configValue");
            config.addConfigData(key,value);
        }
        return config;
    }
}







*/