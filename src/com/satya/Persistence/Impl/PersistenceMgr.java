package com.satya.Persistence.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.Persistence.RowMapper;

public class PersistenceMgr {

	private long lastInsertedSeq = 0;
	private String jdbcUrl;
	private String username;
	private String password;
	Logger log = Logger.getLogger(PersistenceMgr.class);
	
	
	public PersistenceMgr(String jdbcUrl, String uname, String pass){
		this.setJdbcUrl(jdbcUrl);
		this.setUsername(uname);
		this.setPassword(pass);
	}
	private Connection getConnection(){
		String jdbcUrl = this.getJdbcUrl() + "?characterEncoding=UTF-8&autoReconnect=true&user=" + this.getUsername() + "&password=" + this.getPassword();
		Connection Conn = null;
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				log.error("JDBC not connected"+ e.getMessage(),e);
			}
			Conn = DriverManager.getConnection(jdbcUrl);
		}
		catch (Exception E) {
			log.error("Error connecting database: "+E.getMessage(),E);
		}
		return Conn;
	}

	public void excecuteUpdate(String sql,Object[] params){
		Connection c = getConnection();
		java.sql.PreparedStatement ps = null;
		try{
			ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if(params != null){
				for(int i=0; i<params.length ;i++){
					Object paramValue = params[i];
					if(paramValue== null || paramValue.equals(new Object())){
						ps.setNull(i+1, Types.NULL );
					}else{
						ps.setObject(i+1, paramValue);
					}
				}
			}

			int recordsUdpate = ps.executeUpdate();
			ResultSet t = ps.getGeneratedKeys();  
			if(t.next()) {  
			      lastInsertedSeq = t.getInt(1);  
			} 
		}catch (SQLException e){
			log.error("SQL Exception ",e);
		}finally{
			try {
				if(ps != null){
					ps.close();					  
				}
				c.close();
			}catch (SQLException e){
				log.error("SQL Exception ",e);
			}
		}
	}

	public List executePSQuery(String sql, RowMapper rowMapper){
		long startTime = System.currentTimeMillis();
		List results = new ArrayList();
		ResultSet rs = null;
		Connection c = this.getConnection();
		PreparedStatement ps = null;
		try{
			ps = c.prepareStatement(sql);
			rs = ps.executeQuery();

			while(rs.next()){
				results.add(rowMapper.mapRow(rs));
			}
		}catch(SQLException e){
			log.error("SQL Error: " + e.getMessage(),e);
		}finally{
			try{
				if(rs != null)rs.close();
				if(ps != null)ps.close();
				c.close();
			}catch(SQLException e){
				log.error("SQL Error: " + e.getMessage(), e);
			}
		}
		return results;
	}
	
	public int executeCountQuery(String sql,Object[] params){
		int count = 0;
		ResultSet rs = null;
		Connection c = this.getConnection();
		PreparedStatement ps = null;
		try{
			long startTime = System.currentTimeMillis();
			ps = c.prepareStatement(sql);
			if(params != null){
				for(int i=0;i<params.length;i++){
					Object paramValue = params[i];
					ps.setObject(i+1,paramValue);
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				count = rs.getInt(1);
				break;
			}
		}catch(SQLException e){
			log.error("error during executePSQuery for sql - " + sql,e);
			
		}finally{
			try{
				if(rs != null)rs.close();
				if(ps != null)ps.close();
				c.close();
			}catch(SQLException sqe){}

		}
		return count;
	}

	public Object executeSingleObjectQuery(String sql, Object[] params,
			RowMapper rowMapper){
		List objects = executePSQuery(sql,params,rowMapper);
		Object result = null;
		if(objects.size()>0){
			result = objects.get(0);
		}
		return result;
	}
	
	public List executePSQuery(String sql, Object[] params, 
			RowMapper rowMapper){
		return executePSQuery(sql,params,rowMapper,false,0);
	}
	
	public List executePSQuery(String sql, Object[] params, 
			RowMapper rowMapper,boolean isReturnUniqueRows,int TOP){
		List results = new ArrayList();
		LinkedHashSet<Object> linkedHashSet = new LinkedHashSet<Object>();
		ResultSet rs = null;
		Connection c = this.getConnection();
		PreparedStatement ps = null;
		try{
			long startTime = System.currentTimeMillis();
			ps = c.prepareStatement(sql);
			if(params != null){
				for(int i=0;i<params.length;i++){
					Object paramValue = params[i];
					//indexes are 1 based whereas our counter is 0 based
					ps.setObject(i+1,paramValue);
				}
			}
			rs = ps.executeQuery();
			if(isReturnUniqueRows){
				while(rs.next()){
					if(linkedHashSet.size()< TOP){
						linkedHashSet.add((Object) rowMapper.mapRow(rs));
					}else{
						break;
					}
				}
			}else{
				while(rs.next()){
					results.add(rowMapper.mapRow(rs));
				}	
			}

		}catch(SQLException e){
			log.error("SQL Error occured : ", e);
		}finally{
			try{
				if(rs != null)rs.close();
				if(ps != null)ps.close();
				c.close();
			}catch(SQLException sqe){
				log.error("SQL Error in final clause: " , sqe);
			}

		}
		if(isReturnUniqueRows){
			results.addAll(linkedHashSet);
			return results;
		}else{
			return results;	
		}

	}

	public long getLastUpdatedSeq(){
//		long lastUpdatedSeq = 0;
//		try{
//			lastUpdatedSeq = this.getLastUpdatedSeq(this.getConnection());	
//		}catch(Exception e){}
//		return lastUpdatedSeq;
		return lastInsertedSeq;
	}
	
	protected long getLastUpdatedSeq(Connection c) throws SQLException{
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select last_insert_id()");
		long lastSeq = 0;
		if(rs.next()){
			lastSeq = rs.getLong(1);
		}
		return lastSeq;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
