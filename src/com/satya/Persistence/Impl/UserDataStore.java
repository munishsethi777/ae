package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.User;
import com.satya.Persistence.RowMapper;
import com.satya.Persistence.UserDataStoreI;


public class UserDataStore implements UserDataStoreI,RowMapper {
	
	Logger logger = Logger.getLogger(UserDataStore.class);
	private final static String SELECT = "select * from users ";
	private final static String SELECT_BY_SEQ = SELECT + "where seq = ?";
	private final static String SELECT_BY_PROJECT_SEQ = SELECT + "where projectseq = ?";
	private final static String SELECT_UNAME_PROJECTSEQ = SELECT + "where username=? and projectseq = ?";
	private final static String SELECT_UNAME_PASS = SELECT + "where username=? and password = ? ";
	private final static String SAVE_USER = "insert into users(projectseq, name, email, " +
			"mobile,location, department, username, password,isenabled,lastmodifieddate, createdon) " +
			"values (?,?,?,?,?,?,?,?,?,?,?)";
	private final static String UPDATE_USER = "update users set projectseq=?, name=?, email=?, " +
			"mobile=?,location=?, department=?, username=?, password=?,isenabled=?,lastmodifieddate=? where seq=?";
	private final static String DELETE = "delete from users where seq = ?";
	private final static String UPDATE_PASSWORD = "Update users set password = ? where seq = ?";
	
	private final static String FIND_USERS_SELECTED_IN_USERGROUP = "select users.* from users left join usergroupusers on usergroupusers.userseq = users.seq" +
			" left join usergroups on usergroups.seq = usergroupusers.usergroupseq where usergroups.seq = ?";
	
	private final static String FIND_USERS_AVAILABLE_FOR_USERGROUP = "select DISTINCT users.*,usergroups.seq from users left join usergroupusers on " +
			" usergroupusers.userseq = users.seq and usergroupusers.usergroupseq = ? left join usergroups on usergroups.seq = usergroupusers.usergroupseq where " +
			" usergroupusers.userseq is null";
	
	
	private PersistenceMgr persistenceMgr;
	public UserDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	@Override
	public void Save(User user){
		String SQL = user.getSeq() != 0 ? UPDATE_USER : SAVE_USER;
		try{
			long projectSeq = 0;
			if(user.getProject()!= null){
				projectSeq = user.getProject().getSeq();
			}
			Object[] params  = new Object[11];
			
					params[0]= projectSeq;
					params[1] = user.getName();
					params[2] = user.getEmail();
					params[3] = user.getMobile();
					params[4] = user.getLocation();
					params[5] = user.getDepartment();
					params[6] = user.getUsername();
					params[7] = user.getPassword();
					params[8] = user.isEnabled();
					params[9] = user.getLastModifiedDate();
					if(user.getSeq() != 0){
						params[10] =  user.getSeq();
					}else{
						params[10] = user.getCreatedOn();
					}
		
			persistenceMgr.excecuteUpdate(SQL, params);
			if(user.getSeq() == 0){
				user.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
		}catch(Exception e){
			logger.error(e);
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void Delete(long userSeq) {
		Object [] params = new Object [] {userSeq};
		persistenceMgr.excecuteUpdate(DELETE, params);		
	}
	
	@Override
	public  List<User> findByProjectSeq(long seq) {
		Object [] params = new Object [] {seq};
		return (List<User>) persistenceMgr.executePSQuery(SELECT_BY_PROJECT_SEQ, params, this);	
	}
	public List<User> findAll() {		
		return (List<User>)persistenceMgr.executePSQuery(SELECT, null, this);	
	}
	
	@Override
	public List<User> findSelectedByUserGroupSeq(long userGroupSeq) {
		Object [] params = new Object [] {userGroupSeq};
		return (List<User>) persistenceMgr.executePSQuery(FIND_USERS_SELECTED_IN_USERGROUP, params, this);	
	}
	@Override
	public List<User> findAvailableByUserGroupSeq(long userGroupSeq) {
		Object [] params = new Object [] {userGroupSeq};
		return (List<User>) persistenceMgr.executePSQuery(FIND_USERS_AVAILABLE_FOR_USERGROUP, params, this);
	}
		
	@Override
	public User findBySeq(long seq) {
		Object [] params = new Object [] {seq};
		return (User) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ, params, this);
	}

	@Override
	public User findByUserNameAndProject(String userName,long projectSeq) {
		Object [] params = new Object [] {userName,projectSeq};
		return (User) persistenceMgr.executeSingleObjectQuery(SELECT_UNAME_PROJECTSEQ, params, this);
	}

	@Override
	public User findByUserNamePassword(String userName, String password) {
		Object [] params = new Object [] {userName,password};
		User user =  (User) persistenceMgr.executeSingleObjectQuery(SELECT_UNAME_PASS, params, this);
		return user;
	}


	@Override
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	

	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		User user  = null;
		try{			
			long seq = rs.getLong("seq");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String mobile = rs.getString("mobile");
			String location = rs.getString("location");
			String departement = rs.getString("department");
			String password = rs.getString("password");
			String username = rs.getString("username");
			Date createdon = rs.getDate("createdon");
			boolean isEnabled = rs.getBoolean("isenabled");
			Date lastModifiedDate = rs.getDate("lastmodifieddate");
			
			user = new User();
			user.setSeq(seq);
			user.setName(name);
			user.setEmail(email);
			user.setMobile(mobile);
			user.setLocation(location);
			user.setDepartment(departement);
			user.setPassword(password);
			user.setUsername(username);
			user.setCreatedOn(createdon);
			user.setEnabled(isEnabled);
			user.setLastModifiedDate(lastModifiedDate);
		}catch(Exception e){
			logger.error("UserDataStore populate method error",e);
		}
		
		return user;
		
	}
	@Override
	public void changePassword(User user) {
		Object [] params = new Object [] {user.getPassword(),user.getSeq()};
		persistenceMgr.excecuteUpdate(UPDATE_PASSWORD, params);
	}

	
}
