package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.ApplicationContext;
import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Project;
import com.satya.Persistence.AdminDataStoreI;
import com.satya.Persistence.ProjectDataStoreI;
import com.satya.Persistence.RowMapper;


public class AdminDataStore implements AdminDataStoreI,RowMapper{
	Logger logger = Logger.getLogger(AdminDataStore.class);
	private final static String SELECT = "select * from admins ";
	private final static String SELECT_BY_SEQ = SELECT + "where seq = ?";
	private final static String SELECT_UNAME  = SELECT + "where username=?";
	private final static String SELECT_UNAME_PASS = SELECT + "where username=? and password = ? ";
	private final static String SAVE_ADMIN = "insert into admins(name, email, " +	
		"mobile,city,username, password,isenable,issuperuser,lastmodifieddate,createdon) " +
		"values (?,?,?,?,?,?,?,?,?,?)";
	private final static String UPDATE_ADMIN = "update admins set name=?, email=?, " +
		"mobile=?,city=?, username=?, password=?,isenable=?, issuperuser=?,lastmodifieddate=? where seq=?";
	private final static String UPDATE_ACCOUNT = "update admins set name=?, email=?, " +
	"mobile=?,city=?, lastmodifieddate=? where seq=?";
	private final static String DELETE = "delete from a admins seq = ?";


	private PersistenceMgr persistenceMgr;
	public AdminDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	@Override
	public void Save(Admin admin) {
		String SQL = admin.getSeq() != 0 ? UPDATE_ADMIN : SAVE_ADMIN;
		try{
			
			
			Object[] params  = new Object[10];
			
					params[0]= admin.getName();
					params[1] = admin.getEmail();
					params[2] = admin.getMobile();
					params[3] = admin.getCity();
					params[4] = admin.getUsername();
					params[5] = admin.getPassword();
					params[6] = admin.isEnable();
					params[7] = admin.isSuperUser();
					params[8] = admin.getLastModifiedDate();
					if(admin.getSeq() != 0){
						params[9] =  admin.getSeq();
					}else{
						params[9] = admin.getCreatedOn();
					}
			persistenceMgr.excecuteUpdate(SQL, params);
			if(admin.getSeq() == 0){
				admin.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
		}catch(Exception e){
			
		}
		
	}
	
	
	@Override
	public void Delete(long userSeq) {
		Object [] params = new Object [] {userSeq};
		persistenceMgr.excecuteUpdate(DELETE, params);		
	}
	public List<Admin> findAll() {		
		return (List<Admin>)persistenceMgr.executePSQuery(SELECT, null, this);	
	}
		
	@Override
	public Admin findBySeq(long seq) {
		Object [] params = new Object [] {seq};
		Admin admin =  (Admin) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ, params, this);
		admin = setProjects(admin);
		return admin;
	}

	@Override
	public Admin findByUserName(String userName) {
		Object [] params = new Object [] {userName};
		return (Admin) persistenceMgr.executeSingleObjectQuery(SELECT_UNAME, params, this);
	}

	@Override
	public Admin findByUserNamePassword(String userName, String password) {
		Object [] params = new Object [] {userName,password};
		Admin admin =  (Admin) persistenceMgr.executeSingleObjectQuery(SELECT_UNAME_PASS, params, this);
		admin = setProjects(admin);
		return admin;
	}


	@Override
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	
	private Admin setProjects(Admin admin){
		if(admin== null){
			return null;
		}
		ProjectDataStoreI PDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getProjectDataStore();
		List<Project> projects = PDS.findByAdminSeq(admin.getSeq());
		admin.setProjects(projects);
		return admin;
	}
	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		Admin admin  = null;
		try{			
			long seq = rs.getLong("seq");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String mobile = rs.getString("mobile");
			String city = rs.getString("city");
			String username = rs.getString("username");
			String password = rs.getString("password");
			boolean isEnable = rs.getBoolean("isenable");
			boolean isSuperUser= rs.getBoolean("issuperuser");
			
			Date createdOn = rs.getDate("createdon");
			Date lastModifiedDate = rs.getDate("lastmodifieddate");
			admin = new Admin();
			admin.setSeq(seq);
			admin.setName(name);
			admin.setEmail(email);
			admin.setMobile(mobile);
			admin.setCity(city);
			admin.setUsername(username);
			admin.setPassword(password);
			admin.setIsEnable(isEnable);
			admin.setIsSuperUser(isSuperUser);
			admin.setCreatedon(createdOn);
			admin.setLastModifiedDate(lastModifiedDate);
		}catch(Exception e){
			logger.error("AdminDataStore populate method error",e);
		}
		
		return admin;
		
	}
}
