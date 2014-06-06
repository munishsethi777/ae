package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
	private static final String MAX_QUESTIONS="maxQuestions";
	Logger logger = Logger.getLogger(GameTemplateMgr.class);
	
	public List<GameTemplates> getAllGameTemplates()throws ServletException, IOException{
		List<GameTemplates> gameTemplates = null;
		GameTemplatesDataStoreI GTDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameTemplateDataStore();
		gameTemplates = GTDS.findAll();
		return gameTemplates;
	}

	@Override
	public JSONArray getAllGameTemplateJson(HttpServletRequest request)
			throws ServletException, IOException {
		//Method will fetch all the game Templates AND campaignGames if campaignSeq provided in API
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
				jsonArr.put(templateObject);
			}
			if(campaignGames != null){
				for(Game game : campaignGames){
					if(game.isPublished()==false){
						templateObject = toJson(game.getGameTemplate());
						templateObject.put(IConstants.NAME,game.getTitle());
						templateObject.put(IConstants.DESCRIPTION,game.getDescription());
						templateObject.put(IMAGE_PATH,game.getImagePath());
						templateObject.put(MAX_QUESTIONS,game.getMaxQuestions());
						templateObject.put("gameSeq", game.getSeq());
						if(game.getQuestions()!=null){
							templateObject.put("totalQuestions", game.getQuestions().size());
						}
						jsonArr.put(templateObject);
					}
				}
			}
		
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			logger.error("Error occured fetching templates for campaigns",e);
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
			json.put(MAX_QUESTIONS, gameTemplate.getMaxQuestions());
		}catch( Exception e){
			
		}
		return json;
	}

	@Override
	public GameTemplates getBySeq(long gameSeq) throws ServletException, IOException {
		List<GameTemplates> gameTemplates = null;
		GameTemplatesDataStoreI GTDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameTemplateDataStore();
		return GTDS.findBySeq(gameSeq);
	}
	
}
