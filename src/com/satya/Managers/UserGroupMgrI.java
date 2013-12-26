package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;


import com.satya.BusinessObjects.UserGroup;

public interface UserGroupMgrI {
	public List<UserGroup> getAllUserGroups(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getAllUserGroupsJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addUserGroup(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public JSONObject delete(long userSeq)throws Exception;
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;

	public JSONArray getAvailableOnCampaignJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getSelectedOnCampaignJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getSelectedOnCampaignJSON(long campaignSeq);
	public JSONObject addUserGroupFromCampaign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception;
	public JSONArray getSelectedUsersOnCampaignJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, Exception ;
	public JSONObject updateUserGroup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception ;
}
