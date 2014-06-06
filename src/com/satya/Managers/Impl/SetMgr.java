package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.Questions;
import com.satya.BusinessObjects.Set;
import com.satya.BusinessObjects.UserGroup;
import com.satya.Managers.SetMgrI;
import com.satya.Persistence.CampaignDataStoreI;
import com.satya.Persistence.SetDataStoreI;
import com.satya.Persistence.UserGroupDataStoreI;
import com.satya.Utils.DateUtils;

public class SetMgr implements SetMgrI {
	
	
	private static final String DELETED_SUCCESSFULLY = "Deleted Successfully";
	private static final String USER_SAVED_SUCCESSFULLY = "User Saved Successfully";
	private static final String MESSAGE = "message";
	private static final String STATUS = "status";
	private static final String ERROR = "Error: ";
	private static final String FAILD = "faild";
	private static final String SUCCESS = "success";
	
	@Override
	public JSONArray getAvailableOnCampaignJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String campaignSeqStr = request.getParameter("campaignSeq");
		SetDataStoreI SDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getSetDataStore();
		List<Set> sets = null;
		if (campaignSeqStr.equals("")) {
			sets = SDS.findAll();
		} else {
			sets = SDS.findAvailableForCampaign(Long.parseLong(campaignSeqStr));
		}

		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for (Set set : sets) {
			jsonArr.put(Set.toJson(set));
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
		SetDataStoreI SDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getSetDataStore();
		List<Set> sets = null;
		if (campaignSeqStr.equals("")) {
			sets = SDS.findAll();
		} else {
			sets = SDS.findSelectedForCampaign(Long.parseLong(campaignSeqStr));
		}

		return Set.getJSONArray(sets);
	}
	
	public List<Set> getAllSets(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Project currProject = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
		List<Set> sets = null;
		SetDataStoreI SDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getSetDataStore();
		sets = SDS.findByProjectSeq(currProject.getSeq());
		return sets;		
	}
	public JSONArray getAllSetJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		List<Set> sets = getAllSets(request,response);
		return Set.getJSONArray(sets);
	}
	
	
	public JSONObject addSet(HttpServletRequest request, HttpServletResponse response)
										 			throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		String id = request.getParameter(Set.ID);
		String name = request.getParameter(Set.NAME);
		String description = request.getParameter(Set.DESCRIPTION);		
		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);	
		
		Set set = new Set();
		long setSeq = 0;
		if(id != null && !id.equals("")){
			setSeq = Long.parseLong(id);	
		}
		
		String selectedGamesString = request.getParameter(IConstants.SELECTED_CHILDREN_ROWS);
		String[] selectedGamesSeqsStrArray = selectedGamesString.split(",");

		
		for (String seqStr : selectedGamesSeqsStrArray) {
			try {
				int gseq = Integer.valueOf(seqStr);
				Game game = new Game();
				game.setSeq(gseq);
				if (set.getGames() == null) {
					List<Game> gamesList = new ArrayList<Game>();
					set.setGames(gamesList);
				}
				set.getGames().add(game);
			} catch (Exception e) {
				//log.error("Error converting useqStr to useq" + e.getMessage());
			}
		}
		
		
		set.setSeq(setSeq);
		set.setName(name);
		set.setDescription(description);
		set.setCreatedOn(new Date());
		set.setLastModifiedDate(new Date());
		boolean isEnable = false;
		if(isEnableStr != null && !isEnableStr.equals("")){
			isEnable = Boolean.parseBoolean(isEnableStr);
		}
		set.setEnable(isEnable);
		set.setProject(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request));
		SetDataStoreI SDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getSetDataStore();
		String status = SUCCESS;
		String message = USER_SAVED_SUCCESSFULLY;
		try{
			SDS.Save(set);
			json.put(IConstants.LAST_MODIFIED,  DateUtils.getGridDateFormat(set.getLastModifiedDate()));
			json.put(IConstants.CREATED_ON, DateUtils.getGridDateFormat(set.getCreatedOn()));
		}catch(Exception  e){
			status = FAILD;
			message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(Set.ID, set.getSeq());
				
		return json;
	}
	public JSONObject delete(long userSeq)throws Exception{
		JSONObject json = new JSONObject();
		String status = SUCCESS;
		String message = DELETED_SUCCESSFULLY;
		try{
			SetDataStoreI SDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getSetDataStore();
			SDS.Delete(userSeq);			
		}catch (Exception e){
			 status = FAILD;
			 message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(Set.ID, userSeq);
		return json;
	}
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if(idsStr != null && !idsStr.equals("")){
			String[] ids = idsStr.split(",");
			for(String id : ids){
				long setSeq = Long.parseLong(id);
				json = this.delete(setSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}
	@Override
	public JSONArray getSetsByCampaign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			return null;
	}
}
