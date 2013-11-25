package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.ApplicationContext;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;
import com.satya.BusinessObjects.UserGroup;
import com.satya.Persistence.ProjectDataStoreI;
import com.satya.Persistence.RowMapper;
import com.satya.Persistence.UserGroupDataStoreI;

public class UserGroupDataStore implements UserGroupDataStoreI,RowMapper {
	
	Logger logger = Logger.getLogger(UserDataStore.class);
	private final static String SELECT = "select * from usergroups ";
	private final static String SELECT_BY_SEQ = SELECT + "where seq = ?";
	private final static String SELECT_BY_NAME_PROJECT = SELECT + "where name = ? and projectseq = ?";
	private final static String SELECT_BY_PROJECT_SEQ = SELECT + "where projectseq = ?";
	
	private final static String SAVE = "insert into usergroups(projectseq, name, description, " +
			"isenabled,lastmodifieddate,createdon) " +
			"values (?,?,?,?,?,?)";
	private final static String UPDATE = "update usergroups set projectseq=?, name=?, description=?, " +
			" isenabled=?,lastmodifieddate=? where seq=?";
	private final static String DELETE = "delete from usergroups where seq = ?";
	
	private final static String SAVE_USERGROUPUSERS = "insert into usergroupusers(userseq, usergroupseq) values (?,?)";
	private final static String DELETE_USERGROUPUSERS = "delete from usergroupusers where usergroupseq = ?";
	
	private final static String FIND_USERGROUPS_SELECTED_IN_CAMPAIGN = "select usergroups.* from usergroups left join campaignusergroups on " +
			"campaignusergroups.usergroupseq = usergroups.seq left join campaigns on campaigns.seq = campaignusergroups.campaignseq where campaigns.seq = ?";
	
	private final static String FIND_USERGROUPS_AVAILABLE_FOR_CAMPAIGN = "select DISTINCT usergroups.* from usergroups left join campaignusergroups on " +
			"campaignusergroups.usergroupseq = usergroups.seq and campaignusergroups.campaignseq = ? left join campaigns on campaigns.seq = campaignusergroups.campaignseq where " +
			"campaignusergroups.campaignseq is null";

	
	private PersistenceMgr persistenceMgr;
	
	public UserGroupDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	
	@Override
	public List<UserGroup> findAvailableForCampaign(long campaignSeq) {
		Object [] params = new Object [] {campaignSeq};
		return (List<UserGroup>) persistenceMgr.executePSQuery(FIND_USERGROUPS_AVAILABLE_FOR_CAMPAIGN, params, this);	
	}

	@Override
	public List<UserGroup> findSelectedForCampaign(long campaignSeq) {
		Object [] params = new Object [] {campaignSeq};
		return (List<UserGroup>) persistenceMgr.executePSQuery(FIND_USERGROUPS_SELECTED_IN_CAMPAIGN, params, this);
	}
	
	@Override
	public void Save(UserGroup userGroup) {
		String SQL = userGroup.getSeq() != 0 ? UPDATE : SAVE;
		try{
			
			Object[] params  = new Object[6];
			
			params[0]= userGroup.getProject().getSeq();
			params[1] = userGroup.getName();
			params[2] = userGroup.getDescription();	
			params[3] = userGroup.isEnable();
			params[4] = new Date();		
			if(userGroup.getSeq() != 0){
				params[5] =  userGroup.getSeq();
			}else{
				params[5] = userGroup.getCreatedOn();
			}
		
			persistenceMgr.excecuteUpdate(SQL, params);
			if(userGroup.getSeq() == 0){
				userGroup.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
			this.saveUserGroupUsers(userGroup);
		
		}catch(Exception e){
			logger.error("Err occured in group saving"+ e.getMessage());
		}
	}

	private void saveUserGroupUsers(UserGroup userGroup){
		Object[] deleteParams  = new Object[1];
		deleteParams[0]= userGroup.getSeq();
		persistenceMgr.excecuteUpdate(DELETE_USERGROUPUSERS, deleteParams);
		
		for(User user:userGroup.getUsers()){
			Object[] params  = new Object[2];
			params[0]= user.getSeq();
			params[1]= userGroup.getSeq();
			try{
			persistenceMgr.excecuteUpdate(SAVE_USERGROUPUSERS, params);
			}catch(Exception e){
				logger.error("Error settig user group usrs"+ e.getMessage());
			}
		}
		
	}
	@Override
	public void Delete(long userSeq) {
		Object [] params = new Object [] {userSeq};
		persistenceMgr.excecuteUpdate(DELETE, params);		
	}
	
	@Override
	public List<UserGroup> findByProjectSeq(long seq) {
		Object [] params = new Object [] {seq};
		return (List<UserGroup>) persistenceMgr.executePSQuery(SELECT_BY_PROJECT_SEQ, params, this);	
	}
	
	public List<UserGroup> findAll() {		
		return (List<UserGroup>)persistenceMgr.executePSQuery(SELECT, null, this);	
	}
		
	@Override
	public UserGroup findBySeq(long seq) {
		Object [] params = new Object [] {seq};
		return (UserGroup) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ, params, this);
	}
	@Override
	public UserGroup findByNameAndProject(String name,long seq){
		Object [] params = new Object [] {name};
		return (UserGroup) persistenceMgr.executeSingleObjectQuery(SELECT_BY_NAME_PROJECT, params, this);
	}
	
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	

	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		UserGroup userGroup  = null;
		try{			
			long seq = rs.getLong("seq");
			String name = rs.getString("name");
			String  description = rs.getString("description");			
			Date createdOn = rs.getDate("createdon");
			long projectSeq = rs.getLong("projectseq");
			boolean isEnable = rs.getBoolean("isenabled");
			Date lastModifiedDate = rs.getDate("lastmodifieddate");
			userGroup = new UserGroup();
			userGroup.setSeq(seq);
			userGroup.setName(name);
			userGroup.setDescription(description);
			userGroup.setCreatedOn(createdOn);
			userGroup.setEnable(isEnable);
			userGroup.setLastModifiedDate(lastModifiedDate);
			Project project = null;
			if(projectSeq != 0 ){
				ProjectDataStoreI PDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getProjectDataStore();
				project = PDS.findBySeq(projectSeq);
			}
			userGroup.setProject(project);
		}catch(Exception e){
			logger.error("UserGroupDataStore populate method error",e);
		}
		return userGroup;

}
}