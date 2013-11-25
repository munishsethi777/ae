package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.UserGroup;


public interface CampaignMgrI {
	//ADMIN METHODS
	public List<Campaign> getAllCampaigns(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getAllCampaignsJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addCampaign(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public JSONObject delete(long campaignSeq)throws Exception;
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	
	//USER METHODS
	public List<Campaign> getCampaignsForUser(long userSeq)throws Exception;
	public JSONArray getCampaignsForUserJSON(long userSeq)throws Exception;
	public void saveCampaignGames(Long campaignSeq,List<Game>games)throws Exception;
	public void saveCampaignUserGroups(Long campaignSeq,List<UserGroup>userGroups)throws Exception;
}
