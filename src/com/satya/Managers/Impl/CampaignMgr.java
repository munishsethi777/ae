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
import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.Set;
import com.satya.BusinessObjects.User;
import com.satya.BusinessObjects.UserGroup;
import com.satya.Managers.CampaignMgrI;
import com.satya.Managers.UserMgrI;
import com.satya.Persistence.CampaignDataStoreI;
import com.satya.Persistence.UserGroupDataStoreI;
import com.satya.Utils.DateUtils;
import com.satya.mail.MailerI;

public class CampaignMgr implements CampaignMgrI{
	private final static Logger logger = Logger.getLogger(CampaignMgr.class);
	private static final String SUCCESS = "success";
	private static final String SAVED_SUCCESSFULLY = "User Group Saved Successfully";
	public List<Campaign> getAllCampaigns(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Project currentProject = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
		List<Campaign> campaigns = null;
		CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
		campaigns = CDS.findByProjectSeq(currentProject.getSeq());
		return campaigns;		
	}

	public JSONArray getAllCampaignsJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		List<Campaign> campaigns = getAllCampaigns(request,response);
		return Campaign.toJsonArray(campaigns);
	}
	
	
//	public JSONObject addCampaign(HttpServletRequest request, HttpServletResponse response)
//										 			throws ServletException, IOException,Exception{
//		JSONObject json = new JSONObject();
//		String id = request.getParameter(IConstants.SEQ);
//		String name = request.getParameter(IConstants.NAME);
//		String description = request.getParameter(IConstants.DESCRIPTION);	
//		String validityDays = request.getParameter(IConstants.VALIDITY_DAYS);		
//		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);	
//		
//		
//		Campaign campaign = new Campaign();
//		long campaignSeq = 0;
//		if(id != null && !id.equals("")){
//			campaignSeq = Long.parseLong(id);	
//		}
//		boolean isEnable = false;
//		if(isEnableStr != null && !isEnableStr.equals("")){
//			isEnable = Boolean.parseBoolean(isEnableStr);
//		}
//		campaign.setSeq(campaignSeq);
//		campaign.setName(name);
//		campaign.setDescription(description);
//		campaign.setValidityDays(validityDays);
//		campaign.setEnabled(isEnable);
//		campaign.setCreatedOn(new Date());
//		campaign.setLastModifiedDate(new Date());
//		campaign.setProject(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request));
//		
//		//Set Selected userGroups on Campaign
//		String selectedUsersGroupsString = request.getParameter("selectedUserGroups");
//		String[] selectedUserGroupsSeqsStrArray = selectedUsersGroupsString.split(",");
//
//		for (String userGroupSeqStr : selectedUserGroupsSeqsStrArray) {
//			try {
//				int ugseq = Integer.valueOf(userGroupSeqStr);
//				UserGroup userGroup = new UserGroup();
//				userGroup.setSeq(ugseq);
//				if (campaign.getUserGroups() == null) {
//					List<UserGroup> userGroupList = new ArrayList<UserGroup>();
//					campaign.setUserGroups(userGroupList);
//				}
//				campaign.getUserGroups().add(userGroup);
//			} catch (Exception e) {
//				logger.error("Error converting useqStr to useq" + e.getMessage());
//			}
//		}
//		
//		//Set Selected sets on Campaign
//		String selectedSetsString = request.getParameter("selectedSets");
//		String[] selectedSetSeqsStrArray = selectedSetsString.split(",");
//
//		for (String setSeqStr : selectedSetSeqsStrArray) {
//			try {
//				int setseq = Integer.valueOf(setSeqStr);
//				Set set = new Set();
//				set.setSeq(setseq);
//				if (campaign.getSets() == null) {
//					List<Set> setList = new ArrayList<Set>();
//					campaign.setSets(setList);
//				}
//				campaign.getSets().add(set);
//			} catch (Exception e) {
//				
//			}
//		}
//		
//		
//		
//		CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
//		String status = IConstants.SUCCESS;
//		String message = IConstants.SAVED_SUCCESSFULLY;
//		try{
//			CDS.Save(campaign);
//			sendCampaignNotification(campaign);
//			json.put(IConstants.LAST_MODIFIED, DateUtils.getGridDateFormat(campaign.getLastModifiedDate()));
//			json.put(IConstants.CREATED_ON, DateUtils.getGridDateFormat(campaign.getCreatedOn()));
//		}catch(Exception  e){
//			status = IConstants.FAILURE;
//			message = IConstants.ERROR + " : " + e.getMessage();
//		}
//		json.put(IConstants.STATUS, status);
//		json.put(IConstants.MESSAGE, message);
//		json.put(IConstants.SEQ, campaign.getSeq());
//				
//		return json;
//	}
	
	
	public JSONObject addCampaign(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		String id = request.getParameter(IConstants.SEQ);
		String name = request.getParameter(IConstants.NAME);
		String description = request.getParameter(IConstants.DESCRIPTION);	
		String validityDays = request.getParameter(IConstants.VALIDITY_DAYS);		
		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);
		String fromDateStr = request.getParameter("startDate");	
		String toDateStr = request.getParameter("validTillDate");
		String launchMessage = request.getParameter("launchMessage");
		
		Campaign campaign = new Campaign();
		long campaignSeq = 0;
		if(id != null && !id.equals("")){
			campaignSeq = Long.parseLong(id);	
		}
		boolean isEnable = false;
		if(isEnableStr != null && !isEnableStr.equals("")){
			isEnable = Boolean.parseBoolean(isEnableStr);
		}
		campaign.setSeq(campaignSeq);
		campaign.setName(name);
		campaign.setDescription(description);
		campaign.setValidityDays(validityDays);
		campaign.setEnabled(isEnable);
		campaign.setCreatedOn(new Date());
		campaign.setLastModifiedDate(new Date());
		campaign.setProject(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request));
		campaign.setStartDate(DateUtils.getDateFromString(fromDateStr));
		campaign.setValidTillDate(DateUtils.getDateFromString(toDateStr));
		campaign.setLaunchMessage(launchMessage);
		CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
		String status = IConstants.SUCCESS;
		String message = IConstants.SAVED_SUCCESSFULLY;
		try{
			CDS.Save(campaign);
			sendCampaignNotification(campaign);
			json.put(IConstants.LAST_MODIFIED, DateUtils.getGridDateFormat(campaign.getLastModifiedDate()));
			json.put(IConstants.CREATED_ON, DateUtils.getGridDateFormat(campaign.getCreatedOn()));
		}catch(Exception  e){
			status = IConstants.FAILURE;
			message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, campaign.getSeq());
		
		return json;
}
	private void sendCampaignNotification(Campaign campaign){
		List<UserGroup>userGroupList = campaign.getUserGroups();
		if(userGroupList!= null && userGroupList.size() > 0){
			MailerI mailer = ApplicationContext.getApplicationContext().getMailer();
			for(UserGroup userGroup:userGroupList){
				List<User>userList = userGroup.getUsers();
				if(userList!= null && userList.size()>0){
					for(User user:userList){
						try{
							mailer.sendCampaignAlertNotification(user);
						}catch(Exception e){
							logger.error("Error during sending email to user : " 
									+ user.getEmail(), e);
						}
					}
				}
				else{
					logger.error("Users not available on UserGroups : - " 
							+ userGroup.getName() + " for campaign : - " + campaign.getName());
				}
			}
		}else{
			logger.error("UserGroups not available for campaign : - " 
					+ campaign.getName());
		}
	}
	
	public JSONObject delete(long userGroupSeq)throws Exception{
		JSONObject json = new JSONObject();
		String status = IConstants.SUCCESS;
		String message = IConstants.msg_DeletedSuccessfully;
		try{
			CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
			CDS.Delete(userGroupSeq);			
		}catch (Exception e){
			 status = IConstants.FAILURE;
			 message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, userGroupSeq);
		return json;
	}
	
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if(idsStr != null && !idsStr.equals("")){
			String[] ids = idsStr.split(",");
			for(String id : ids){
				long campaignSeq = Long.parseLong(id);
				json = this.delete(campaignSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}

	@Override
	public List<Campaign> getCampaignsForUser(long userSeq) throws Exception {
		CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
		return CDS.findByUserSeq(userSeq);
	}

	@Override
	public JSONArray getCampaignsForUserJSON(long userSeq) throws Exception {
		List<Campaign> campaigns = getCampaignsForUser(userSeq);
		return Campaign.toJsonArray(campaigns);
	}
	@Override
	public void saveCampaignGames(Long campaignSeq,List<Game>games)throws Exception{
		Campaign campaign = new Campaign();
		campaign.setSeq(campaignSeq);
		List<Game>savedGames = campaign.getGames();
		if(savedGames == null){
			savedGames = new ArrayList<Game>();
		}
		savedGames.addAll(games);
		campaign.setGames(savedGames);
		CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
		CDS.saveGames(campaign);
	}
	
	public JSONObject saveCampaignUserGroups(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		
		String UsersGroupSeqString = request.getParameter("userGroupSeq");
		String campaignSeqStr = request.getParameter("campaignSeq");
		String status = SUCCESS;
		String message = SAVED_SUCCESSFULLY;
		UserGroupDataStoreI UGDS = ApplicationContext.getApplicationContext()
										.getDataStoreMgr().getUserGroupDataStore();
		UserGroup userGroup = new UserGroup();
		try{
			int userGroupSeq = Integer.valueOf(UsersGroupSeqString);
			userGroup = UGDS.findBySeq(userGroupSeq);
			userGroup.setProject(ApplicationContext.getApplicationContext()
					.getAdminWorkspaceProject(request));
			Long campaignSeq = Long.parseLong(campaignSeqStr);
			CampaignMgrI campaignMgr = ApplicationContext.getApplicationContext().getCampaiMgr();
			List<UserGroup>userGroups = new ArrayList<UserGroup>();
			userGroups.add(userGroup);
			campaignMgr.saveCampaignUserGroups(campaignSeq, userGroups);
			
		} catch (Exception e) {
			status = "faild";
			message = "Error" + e.getMessage();
		}
		json.put("status", status);
		json.put("message", message);
		return json;
	}
	@Override
	public void saveCampaignUserGroups(Long campaignSeq,List<UserGroup>userGroups)throws Exception{
		Campaign campaign = new Campaign();
		campaign.setSeq(campaignSeq);
		List<UserGroup>savedUserGroup = campaign.getUserGroups();
		if(savedUserGroup == null){
			savedUserGroup = new ArrayList<UserGroup>();
		}
		savedUserGroup.addAll(userGroups);
		campaign.setUserGroups(savedUserGroup);
		CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
		CDS.saveUserGroups(campaign);
	}
}
