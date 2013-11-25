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
import com.satya.BusinessObjects.GameTemplates;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.Questions;
import com.satya.BusinessObjects.QuestionsList;
import com.satya.BusinessObjects.Result;
import com.satya.BusinessObjects.User;
import com.satya.Managers.CampaignMgrI;
import com.satya.Managers.GameMgrI;
import com.satya.Persistence.CampaignDataStoreI;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.GameTemplatesDataStoreI;
import com.satya.Persistence.QuestionDataStoreI;
import com.satya.Persistence.ResultsDataStoreI;
import com.satya.Utils.DateUtils;
import com.satya.Utils.FileUtils;
import com.satya.Utils.XstreamUtil;

public class GameMgr implements GameMgrI {
	
	Logger logger = Logger.getLogger(GameMgr.class);
	
	
	@Override
	public JSONArray getAvailableOnSetJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return getGamesBySet(false, request);
	}
	@Override
	public JSONArray getSelectedOnSetJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return getGamesBySet(true, request);
	}
	
	private JSONArray getGamesBySet(boolean isSelected, HttpServletRequest request){
		String setSeqStr = request.getParameter("setSeq");
		GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
		
		List<Game> games = null;
		if(setSeqStr.equals("")){
			games = GDS.FindAll(true);
		}else{
			if(isSelected){
				games = GDS.findSelectedForSet(Long.parseLong(setSeqStr));
			}else{
				games = GDS.findAvailableForSet(Long.parseLong(setSeqStr));
			}
		}
		
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(Game game: games){ 
			jsonArr.put(toJson(game));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	
	
	public List<Game> getAllGames(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Project currentProject = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
		List<Game> games = null;
		GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
		games = GDS.FindByProject(currentProject.getSeq());
		return games;		
	}
	public JSONArray getAllGameJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		List<Game> games = getAllGames(request,response);
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(Game game: games){ 
			jsonArr.put(toJson(game));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	@Override
	public void loadPlayer(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String gameIdStr = request.getParameter("gid");
		String campaignIdStr = request.getParameter("cid");
		String setIdStr = request.getParameter("sid");
		
		long gameSeq = 0;
		long campaignSeq = 0;
		long setSeq = 0;
		User user = ApplicationContext.getApplicationContext().getLoggedinUser(request);
		long userSeq = user.getSeq();
		try{
			gameSeq = Long.parseLong(gameIdStr);
			campaignSeq = Long.parseLong(campaignIdStr);
			setSeq = Long.parseLong(setIdStr);
			
			GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
			boolean isGameAlloted = GDS.isGameByCampaignSetGameUser(campaignSeq, setSeq, gameSeq, userSeq);
			int campaignSetSeq = 0;
			boolean isAllow = true;
			if(isGameAlloted){
				
				CampaignDataStoreI CDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getCampaignDataStore();
				campaignSetSeq = CDS.getCampaignSetSeq(campaignSeq, setSeq);
				
				ResultsDataStoreI RDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getResultsDataStore();
				List<Result> results = RDS.findByCampaign(campaignSeq);
				
				for(Result result : results){
					if(result.getCampaign().getSeq() == campaignSeq){
						if(result.getSet().getSeq() == setSeq){
							if(result.getGame().getSeq() == gameSeq){
								if (result.getCreatedOn() != null){
									isAllow = false;
								}
							}
						}
					}
				}
			}else{
				isAllow = false;
			}
			if(isAllow){
				request.setAttribute("campaignSetSeq",String.valueOf(campaignSetSeq));
				request.getRequestDispatcher("player.jsp").forward(request,response);
			}
			
		}catch(Exception e){
			logger.error("Error occured while loading Player page", e);
		}
		
	}
	
	public JSONObject addGames(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException,Exception{
				JSONObject json = new JSONObject();
				String id = request.getParameter(IConstants.SEQ);
				String title = request.getParameter(IConstants.GAME_TITLE);
				String description = request.getParameter(IConstants.GAME_DESCRIPTION);		
				String isEnableStr = request.getParameter(IConstants.IS_ENABLED);	
				String gameTemplateSeqStr = request.getParameter("gameTemplate");
				String maxSecondsAllowedStr = request.getParameter("gameMaxSecondsAllowed");
				Game game = new Game();
				long gameSeq = 0;
				if(id != null && !id.equals("")){
					gameSeq = Long.parseLong(id);	
				}
				boolean isEnable = false;
				if(isEnableStr != null && !isEnableStr.equals("")){
					isEnable = Boolean.parseBoolean(isEnableStr);
				}
				if(gameTemplateSeqStr != null && !gameTemplateSeqStr.equals("")){
					long gameTemplateSeq = Long.parseLong(gameTemplateSeqStr);
					GameTemplates gameTemplate = new GameTemplates();
					gameTemplate.setSeq(gameTemplateSeq);
					game.setGameTemplate(gameTemplate);
				}
				String selectedQuestionsString = request.getParameter(IConstants.SELECTED_CHILDREN_ROWS);
				String[] selectedQuestionsSeqsStrArray = selectedQuestionsString.split(",");

				
				for (String seqStr : selectedQuestionsSeqsStrArray) {
					try {
						int qseq = Integer.valueOf(seqStr);
						Questions question = new Questions();
						question.setSeq(qseq);
						if (game.getQuestions() == null) {
							List<Questions> questionsList = new ArrayList<Questions>();
							game.setQuestions(questionsList);
						}
						game.getQuestions().add(question);
					} catch (Exception e) {
						logger.error(e);
					}
				}
				
				
				game.setTitle(title);
				game.setSeq(gameSeq);
				game.setDescription(description);
				game.setEnable(isEnable);
				game.setCreatedOn(new Date());				
				game.setLastModifiedDate(new Date());
				game.setProject(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request));
				GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
				String status = IConstants.SUCCESS;
				String message = IConstants.SAVED_SUCCESSFULLY;
				try{
					GDS.Save(game);
					game = GDS.findBySeqWithQuesAnswers(game.getSeq());
					generateQuestionXml(game);
					json.put(IConstants.LAST_MODIFIED, DateUtils.getGridDateFormat(game.getLastModifiedDate()));
					json.put(IConstants.CREATED_ON,  DateUtils.getGridDateFormat(game.getCreatedOn()));
				}catch(Exception  e){
					status = IConstants.FAILURE;
					message = IConstants.ERROR + " : " + e.getMessage();
				}
				json.put(IConstants.STATUS, status);
				json.put(IConstants.MESSAGE, message);
				json.put(IConstants.SEQ, game.getSeq());
				
				return json;
	}
	public JSONObject addGameFromImportQuestion(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException,Exception{
			JSONObject json = new JSONObject();
			String gameTemplateSeqStr = request.getParameter("gameTempSeq");
			String gameSeqStr = request.getParameter("gameSeq");
			String questionSeqsStr = request.getParameter("quesSeqs");
			String campaignSeqStr = request.getParameter("campaignSeq");
			List<Questions>questionsList = new ArrayList<Questions>();
			String status = IConstants.SUCCESS;
			String message = IConstants.SAVED_SUCCESSFULLY;
			Game game = new Game();
			try{
				if(questionSeqsStr != null && !questionSeqsStr.equals("")){		
					String[] questionSeqs = questionSeqsStr.split(",");
					QuestionDataStoreI QDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionDataStore();
					for(String seq:questionSeqs){
						Long questionSeq = Long.parseLong(seq);
						Questions question = QDS.findBySeq(questionSeq);
						questionsList.add(question);
						
					}
				}
				GameTemplatesDataStoreI GTDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameTemplateDataStore();
				GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
				game.setLastModifiedDate(new Date());
				game.setProject(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request));
				if(gameTemplateSeqStr != null && !gameTemplateSeqStr.equals("")){				
					GameTemplates GT = GTDS.findBySeq(Long.parseLong(gameTemplateSeqStr));
					game.setTitle(GT.getName());
					game.setDescription(GT.getDescription());
					game.setEnable(true);
					game.setGameTemplate(GT);
					game.setQuestions(questionsList);
					json.put("gameSeq", game.getSeq());
				}else if(gameSeqStr != null && !gameSeqStr.equals("")){
					game = GDS.findBySeqWithQuesAnswers(Long.parseLong(gameSeqStr));
					List<Questions>savedQuestions = game.getQuestions();
					if(savedQuestions == null){
						savedQuestions = new ArrayList<Questions>();
					}
					savedQuestions.addAll(questionsList);
					game.setQuestions(savedQuestions);
				}	
				game.setCreatedOn(new Date());			
				GDS.Save(game);
				if(campaignSeqStr != null && !campaignSeqStr.equals("")){
					CampaignMgrI campaignMgr = ApplicationContext.getApplicationContext().getCampaiMgr();
					Long campaignSeq = Long.parseLong(campaignSeqStr);
					List<Game>games = new ArrayList<Game>();
					games.add(game);
					campaignMgr.saveCampaignGames(campaignSeq,games);
				}
				
			}catch(Exception  e){
				status = IConstants.FAILURE;
				message = IConstants.ERROR + " : " + e.getMessage();
			}
			json.put(IConstants.STATUS, status);
			json.put(IConstants.MESSAGE, message);
			json.put("gameSeq", game.getSeq());
			return json;
	}
	

	
	private void generateQuestionXml(Game game){
		XstreamUtil xstreamUtil = new XstreamUtil();
		String str = xstreamUtil.generateQuestionsXML(game);
		String filePath = IConstants.APPLICATION_FILE_PATH + "\\"+ game.getSeq() + ".txt" ;
		FileUtils.saveTextFile(str, filePath);
	}
	
	private JSONObject toJson(Game game){		
		JSONObject json = new JSONObject();
		try{
			json.put(IConstants.SEQ, game.getSeq());
			json.put(IConstants.GAME_TITLE, game.getTitle());
			json.put(IConstants.GAME_DESCRIPTION, game.getDescription());
			json.put(IConstants.IS_ENABLED, game.isEnable());
			json.put("gameMaxSecondsAllowed", game.getMaxSecondsAllowed());
			json.put(IConstants.CREATED_ON,  DateUtils.getGridDateFormat(game.getCreatedOn()));
			json.put(IConstants.LAST_MODIFIED_DATE, DateUtils.getGridDateFormat(game.getLastModifiedDate()));
			if(game.getGameTemplate() != null){
				json.put("gameTemplate", game.getGameTemplate().getSeq());
			}
		}catch( Exception e){
			
		}
		return json;
	}

	
}