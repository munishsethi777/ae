package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;
import com.satya.BusinessObjects.UserGroup;
import com.satya.Managers.CampaignMgrI;
import com.satya.Managers.UserGroupMgrI;
import com.satya.Managers.UserMgrI;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.UserDataStoreI;
import com.satya.Persistence.UserGroupDataStoreI;
import com.satya.Utils.DateUtils;

public class UserGroupMgr implements UserGroupMgrI {

	private static final String DESCRIPTION = "description";
	private static final String DELETED_SUCCESSFULLY = "Deleted Successfully";
	private static final String SAVED_SUCCESSFULLY = "User Group Saved Successfully";
	private static final String MESSAGE = "message";
	private static final String STATUS = "status";
	private static final String ERROR = "Error: ";
	private static final String FAILD = "faild";
	private static final String SUCCESS = "success";
	private static final String CREATEDON = "createdOn";
	private static final String NAME = "name";
	private static final String ID = "seq";
	private static final String CHILDRED_ROWS = "selectedChildrenRows";

	Logger log = Logger.getLogger(UserGroupMgr.class.getName());

	@Override
	public JSONArray getAvailableOnCampaignJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String campaignSeqStr = request.getParameter("campaignSeq");
		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getUserGroupDataStore();
		List<UserGroup> userGroups = null;
		if (campaignSeqStr.equals("")) {
			userGroups = UGDS.findAll();
		} else {
			userGroups = UGDS.findAvailableForCampaign(Long.parseLong(campaignSeqStr));
		}

		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for (UserGroup userGroup : userGroups) {
			jsonArr.put(toJson(userGroup));
		}
		try {
			mainJsonObject.put("jsonArr", jsonArr);
		} catch (Exception e) {

		}
		return jsonArr;
	}

	@Override
	public JSONArray getSelectedOnCampaignJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String campaignSeqStr = request.getParameter("campaignSeq");
		long campaignSeq = 0;
		if(!campaignSeqStr.equals("")){
			campaignSeq = Long.parseLong(campaignSeqStr);
		}
		return getSelectedOnCampaignJSON(campaignSeq);
	}
	
	@Override
	public JSONArray getSelectedUsersOnCampaignJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, Exception {
		String campaignSeqStr = request.getParameter("campaignSeq");
		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getUserGroupDataStore();
		List<UserGroup> userGroups = null;
		userGroups = UGDS.findSelectedForCampaign(Long.parseLong(campaignSeqStr));
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		if(userGroups != null && userGroups.size() > 0){
			UserGroup userGroup = userGroups.get(0);
			UserMgrI userMgr = ApplicationContext.getApplicationContext().getUserMgr();
			jsonArr = userMgr.getAvailableUsersOnGroupJson(userGroup);
		}
		try {
			mainJsonObject.put("jsonArr", jsonArr);
		} catch (Exception e) {

		}
		return jsonArr;
	}
	
	
	public List<UserGroup> getAllUserGroups(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Project currentProject = ApplicationContext.getApplicationContext()
				.getAdminWorkspaceProject(request);
		List<UserGroup> userGroups = null;
		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getUserGroupDataStore();
		userGroups = UGDS.findByProjectSeq(currentProject.getSeq());
		return userGroups;
	}

	public JSONArray getAllUserGroupsJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<UserGroup> userGroups = getAllUserGroups(request, response);
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for (UserGroup userGroup : userGroups) {
			jsonArr.put(toJson(userGroup));
		}
		try {
			mainJsonObject.put("jsonArr", jsonArr);
		} catch (Exception e) {

		}
		return jsonArr;
	}

	private JSONObject toJson(UserGroup userGroup) {
		JSONObject json = new JSONObject();
		try {
			json.put(ID, userGroup.getSeq());
			json.put(NAME, userGroup.getName());
			json.put(DESCRIPTION, userGroup.getDescription());
			json.put(CREATEDON, DateUtils.getGridDateFormat(userGroup.getCreatedOn()));
			json.put(IConstants.LAST_MODIFIED_DATE,
					DateUtils.getGridDateFormat(userGroup.getLastModifiedDate()));
			json.put(IConstants.IS_ENABLED, userGroup.isEnable());
		} catch (Exception e) {

		}
		return json;
	}

	public JSONObject addUserGroup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		String id = request.getParameter(ID);
		String name = request.getParameter(NAME);
		String description = request.getParameter(DESCRIPTION);
		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);
		String selectedUsersString = request.getParameter(CHILDRED_ROWS);
		String[] selectedUserSeqsStrArray = selectedUsersString.split(",");

		UserGroup userGroup = new UserGroup();
		for (String userSeqStr : selectedUserSeqsStrArray) {
			try {
				int useq = Integer.valueOf(userSeqStr);
				User user = new User();
				user.setSeq(useq);
				if (userGroup.getUsers() == null) {
					List<User> usersList = new ArrayList<User>();
					userGroup.setUsers(usersList);
				}
				userGroup.getUsers().add(user);
			} catch (Exception e) {
				log.error("Error converting useqStr to useq" + e.getMessage());
			}
		}
		long userGroupSeq = 0;
		if (id != null && !id.equals("")) {
			userGroupSeq = Long.parseLong(id);
		}
		userGroup.setSeq(userGroupSeq);
		userGroup.setName(name);
		userGroup.setDescription(description);
		userGroup.setCreatedOn(new Date());
		userGroup.setLastModifiedDate(new Date());
		boolean isEnable = false;
		if (isEnableStr != null && !isEnableStr.equals("")) {
			isEnable = Boolean.parseBoolean(isEnableStr);
		}
		userGroup.setEnable(isEnable);
		userGroup.setProject(ApplicationContext.getApplicationContext()
				.getAdminWorkspaceProject(request));

		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getUserGroupDataStore();
		String status = SUCCESS;
		String message = SAVED_SUCCESSFULLY;
		UserGroup dupeUserGroup = UGDS.findByNameAndProject(name,userGroup.getProject().getSeq());
		if(dupeUserGroup != null && (dupeUserGroup.getSeq() != userGroup.getSeq())){
			status = FAILD;
			message = ERROR + " Duplicate Usergroup not allowed";
		}else{
			try {
				UGDS.Save(userGroup);
				json.put(IConstants.LAST_MODIFIED, DateUtils.getGridDateFormat(userGroup.getLastModifiedDate()));
				json.put(IConstants.CREATED_ON, DateUtils.getGridDateFormat(userGroup.getCreatedOn()));
			} catch (Exception e) {
				status = FAILD;
				message = ERROR + e.getMessage();
			}
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(ID, userGroup.getSeq());

		return json;
	}
	private void setUsersOnUserGroup(UserGroup userGroup,String selectedUsersString){
		if(selectedUsersString != null && !selectedUsersString.equals("")){
			String[] selectedUserSeqsStrArray = selectedUsersString.split(",");
	     	for (String userSeqStr : selectedUserSeqsStrArray) {
				try {
					int useq = Integer.valueOf(userSeqStr);
					User user = new User();
					user.setSeq(useq);
					if (userGroup.getUsers() == null) {
						List<User> usersList = new ArrayList<User>();
						userGroup.setUsers(usersList);
					}
					userGroup.getUsers().add(user);
				} catch (Exception e) {
					log.error("Error converting useqStr to useq" + e.getMessage());
				}
			}
		}
	}

	
	@Override
	public JSONObject addUserGroupFromCampaign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		String name = request.getParameter("userGroupName");
		String description = request.getParameter("userGroupDescription");
		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);
		String selectedUsersString = request.getParameter("userSeqs");
		String UsersGroupSeqString = request.getParameter("userGroupSeq");
		String campaignSeqStr = request.getParameter("campaignSeq");
		String status = SUCCESS;
		String message = SAVED_SUCCESSFULLY;
		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
										.getDataStoreMgr().getUserGroupDataStore();
		UserGroup userGroup = new UserGroup();
		userGroup.setCreatedOn(new Date());	
		if(UsersGroupSeqString != null && !UsersGroupSeqString.equals("")){
			int userGroupSeq = Integer.valueOf(UsersGroupSeqString);
			userGroup = UGDS.findBySeq(userGroupSeq);
		}
		userGroup.setName(name);
		userGroup.setDescription(description);
		boolean isEnable = false;
		if (isEnableStr != null && !isEnableStr.equals("")) {
			isEnable = Boolean.parseBoolean(isEnableStr);
		}
		userGroup.setEnable(isEnable);
		userGroup.setProject(ApplicationContext.getApplicationContext()
				.getAdminWorkspaceProject(request));

		userGroup.setLastModifiedDate(new Date());
		try {
			if(selectedUsersString != null && !selectedUsersString.equals("")){
				//this case will save usergroup details and relations only. 
				setUsersOnUserGroup(userGroup,selectedUsersString);
			}else{
				List<User> usersList = userGroup.getUsers();
				UserMgrI userMgr = ApplicationContext.getApplicationContext().getUserMgr();
				User user = userMgr.saveUserInternal(request, response);
				if(usersList == null){
					usersList = new ArrayList<User>();
				}
				usersList.add(user);
				userGroup.setUsers(usersList);
			}
			UGDS.Save(userGroup);
			if(campaignSeqStr != null && !campaignSeqStr.equals("")){
				Long campaignSeq = Long.parseLong(campaignSeqStr);
				CampaignMgrI campaignMgr = ApplicationContext.getApplicationContext().getCampaiMgr();
				List<UserGroup>userGroups = new ArrayList<UserGroup>();
				userGroups.add(userGroup);
				campaignMgr.saveCampaignUserGroups(campaignSeq, userGroups);
			}
		} catch (Exception e) {
			status = FAILD;
			message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put("userGroupSeq", userGroup.getSeq());
		return json;
	}
	
	@Override
	public JSONObject updateUserGroup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		String name = request.getParameter("userGroupName");
		String description = request.getParameter("userGroupDescription");
		String UsersGroupSeqString = request.getParameter("userGroupSeq");
		String status = SUCCESS;
		String message = SAVED_SUCCESSFULLY;
		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
										.getDataStoreMgr().getUserGroupDataStore();
		UserGroup userGroup = new UserGroup();
		if(UsersGroupSeqString != null && !UsersGroupSeqString.equals("")){
			int userGroupSeq = Integer.valueOf(UsersGroupSeqString);
			userGroup = UGDS.findBySeq(userGroupSeq);
		}else{
			throw new RuntimeException("UserGroup Seq null");
		}
			userGroup.setName(name);
			userGroup.setDescription(description);
			userGroup.setCreatedOn(new Date());			
			userGroup.setProject(ApplicationContext.getApplicationContext()
					.getAdminWorkspaceProject(request));
	
		userGroup.setLastModifiedDate(new Date());
		try {
			UGDS.Save(userGroup);
		} catch (Exception e) {
			status = FAILD;
			message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put("userGroupSeq", userGroup.getSeq());
		return json;
	}
	
	
	
	
	public JSONObject delete(long userGroupSeq) throws Exception {
		JSONObject json = new JSONObject();
		String status = SUCCESS;
		String message = DELETED_SUCCESSFULLY;
		try {
			UserGroupDataStoreI UGDS = ApplicationContext
					.getApplicationContext().getDataStoreMgr()
					.getUserGroupDataStore();
			UGDS.Delete(userGroupSeq);
		} catch (Exception e) {
			status = FAILD;
			message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(ID, userGroupSeq);
		return json;
	}

	public JSONArray deleteBulk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if (idsStr != null && !idsStr.equals("")) {
			String[] ids = idsStr.split(",");
			for (String id : ids) {
				long userGroupSeq = Long.parseLong(id);
				json = this.delete(userGroupSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}

	@Override
	public JSONArray getSelectedOnCampaignJSON(long campaignSeq) {
		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getUserGroupDataStore();
		List<UserGroup> userGroups = null;
		if (campaignSeq == 0) {
			userGroups = UGDS.findAll();
		} else {
			userGroups = UGDS.findSelectedForCampaign(campaignSeq);
		}

		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for (UserGroup userGroup : userGroups) {
			jsonArr.put(toJson(userGroup));
		}
		try{
			if(jsonArr.length()==0){
				mainJsonObject.put(IConstants.STATUS, IConstants.FAILURE);
				mainJsonObject.put(IConstants.MESSAGE, "No UserGroup found");
				jsonArr.put(mainJsonObject);
			}else{
				mainJsonObject.put("jsonArr", jsonArr);
			}			
		} catch (Exception e) {
			try{
				mainJsonObject.put(IConstants.STATUS, IConstants.FAILURE);
				mainJsonObject.put(IConstants.MESSAGE, "Error occured"+e.getMessage());
				jsonArr.put(mainJsonObject);
			}catch(Exception e1){}
			log.error("Exception occured while fetching selected usergroups on campaign",e);
		}
		return jsonArr;
	}

	@Override
	public JSONObject removeUsersFromUserGroup(HttpServletRequest request) {
		String userGroupStr = request.getParameter("userGroupSeq");
		String idsStr = request.getParameter("ids");
		JSONObject json = new JSONObject();
		if(idsStr != null && !idsStr.equals("")){
//			long gameSeq = Long.parseLong(gameSeqStr);
//			String[] ids = idsStr.split(",");
//			if(ids.length>0){
//				try{
//					long questionSeq = Long.parseLong(ids[0]);
//					GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
//					GDS.deleteGameQuestion(gameSeq, questionSeq);
//
//					json.put(IConstants.STATUS, IConstants.SUCCESS);
//					json.put(IConstants.MESSAGE, IConstants.msg_DeletedSuccessfully);
//					json.put(IConstants.SEQ, questionSeq);
//					return json;
//				}catch(Exception e){
//				}
//			}
			
		}
		return json;
	}
}
