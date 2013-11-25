package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.GameTemplates;
import com.satya.Managers.GameMgrI;
import com.satya.Managers.GameTemplatesMgrI;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.GameTemplatesDataStoreI;

public class GameTemplateMgr implements GameTemplatesMgrI {
	private static final String IMAGE_PATH = "imagePath";
	private static final String PATH = "path";

	public List<GameTemplates> getAllGameTemplates()throws ServletException, IOException{
		List<GameTemplates> gameTemplates = null;
		GameTemplatesDataStoreI GTDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameTemplateDataStore();
		gameTemplates = GTDS.findAll();
		return gameTemplates;
	}

	
	

	@Override
	public JSONArray getAllGameTemplateJson(HttpServletRequest request)
			throws ServletException, IOException {
		String campaignSeqStr = (String)request.getParameter("campaignSeq");
		List<GameTemplates> gameTemplates = getAllGameTemplates();
		List<Game> campaignGames = null;
		if(campaignSeqStr != null && !campaignSeqStr.equals("")){
			long campaignSeq = Long.parseLong(campaignSeqStr);
			GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
			campaignGames = GDS.findSelectedForCampaign(campaignSeq);
		}
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		JSONObject templateObject;
		try{
			for(GameTemplates gameTemplate: gameTemplates){
				templateObject = toJson(gameTemplate);
				templateObject.put("gameSeq", 0);
				if(campaignGames != null){
					for(Game game : campaignGames){
						if(game.getGameTemplate().getSeq() == gameTemplate.getSeq()){
							templateObject.put(IConstants.NAME,game.getTitle());
							templateObject.put("gameSeq", game.getSeq());
						}
					}
				}
				jsonArr.put(templateObject);
			}
		
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	private JSONObject toJson(GameTemplates gameTemplate){		
		JSONObject json = new JSONObject();
		try{
			json.put(IConstants.SEQ, gameTemplate.getSeq());
			json.put(IConstants.NAME, gameTemplate.getName());
			json.put(PATH, gameTemplate.getPath());
			json.put(IMAGE_PATH, gameTemplate.getImagePath());
			json.put(IConstants.DESCRIPTION, gameTemplate.getDescription());			
		}catch( Exception e){
			
		}
		return json;
	}
	
}
