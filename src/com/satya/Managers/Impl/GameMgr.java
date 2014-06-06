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
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
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

	private JSONArray getGamesBySet(boolean isSelected,
			HttpServletRequest request) {
		String setSeqStr = request.getParameter("setSeq");
		GameDataStoreI GDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getGameDataStore();

		List<Game> games = null;
		if (setSeqStr.equals("")) {
			games = GDS.FindAll(true);
		} else {
			if (isSelected) {
				games = GDS.findSelectedForSet(Long.parseLong(setSeqStr));
			} else {
				games = GDS.findAvailableForSet(Long.parseLong(setSeqStr));
			}
		}

		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for (Game game : games) {
			jsonArr.put(toJson(game));
		}
		try {
			mainJsonObject.put("jsonArr", jsonArr);
		} catch (Exception e) {

		}
		return jsonArr;
	}

	public List<Game> getAllGames(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Project currentProject = ApplicationContext.getApplicationContext()
				.getAdminWorkspaceProject(request);
		List<Game> games = null;
		GameDataStoreI GDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getGameDataStore();
		games = GDS.FindByProject(currentProject.getSeq());
		return games;
	}

	public JSONArray getAllGameJson(HttpServletRequest request,
			HttpServletResponse response, boolean isPublished)
			throws ServletException, IOException {
		List<Game> games = getAllGames(request, response);
		List<Game> campaignGames = new ArrayList<Game>();
		if (request.getParameter("campaignSeq") != "") {
			CampaignDataStoreI CDS = ApplicationContext.getApplicationContext()
					.getDataStoreMgr().getCampaignDataStore();
			Campaign campaign = CDS.findBySeq(Long.parseLong(request
					.getParameter("campaignSeq")));
			campaignGames = campaign.getGames();
		}
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		try {
			for (Game game : games) {
				if (game.isPublished() == isPublished) {
					JSONObject jsonObject = toJson(game);
					for (Game campaignGame : campaignGames) {
						if (campaignGame.getSeq() == game.getSeq()) {
							jsonObject.put("isSelectedOnCampaign", "true");
							continue;
						}
					}
					jsonArr.put(jsonObject);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return jsonArr;
	}

	@Override
	public void loadPlayer(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String gameIdStr = request.getParameter("gid");
		String campaignIdStr = request.getParameter("cid");
		
		long gameSeq = 0;
		long campaignSeq = 0;
		User user = ApplicationContext.getApplicationContext().getLoggedinUser(
				request);
		long userSeq = user.getSeq();
		try {
			gameSeq = Long.parseLong(gameIdStr);
			campaignSeq = Long.parseLong(campaignIdStr);
			
			GameDataStoreI GDS = ApplicationContext.getApplicationContext()
					.getDataStoreMgr().getGameDataStore();
			boolean isGameAlloted = GDS.isGameByCampaignGameUser(campaignSeq, gameSeq, userSeq);
			int campaignSetSeq = 0;
			boolean isAllow = true;
			if (isGameAlloted) {

//				CampaignDataStoreI CDS = ApplicationContext
//						.getApplicationContext().getDataStoreMgr()
//						.getCampaignDataStore();
//				campaignSetSeq = CDS.getCampaignSetSeq(campaignSeq, setSeq);
//
//				ResultsDataStoreI RDS = ApplicationContext
//						.getApplicationContext().getDataStoreMgr()
//						.getResultsDataStore();
//				List<Result> results = RDS.findByCampaign(campaignSeq);
//
//				for (Result result : results) {
//					if (result.getCampaign().getSeq() == campaignSeq) {
//						if (result.getSet().getSeq() == setSeq) {
//							if (result.getGame().getSeq() == gameSeq) {
//								if (result.getCreatedOn() != null) {
//									isAllow = false;
//								}
//							}
//						}
//					}
//				}
			} else {
				isAllow = false;
			}
			if (isAllow) {
				request.setAttribute("campaignSeq",
						String.valueOf(campaignSeq));
				request.getRequestDispatcher("userGamePage.jsp").forward(request,
						response);
			}

		} catch (Exception e) {
			logger.error("Error occured while loading Player page", e);
		}

	}

	public JSONObject addGames(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		String id = request.getParameter(IConstants.SEQ);
		String title = request.getParameter(IConstants.GAME_TITLE);
		String description = request.getParameter(IConstants.GAME_DESCRIPTION);
		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);
		String gameTemplateSeqStr = request.getParameter("gameTemplate");
		String maxSecondsAllowedStr = request
				.getParameter("gameMaxSecondsAllowed");
		Game game = new Game();
		long gameSeq = 0;
		if (id != null && !id.equals("")) {
			gameSeq = Long.parseLong(id);
		}
		boolean isEnable = false;
		if (isEnableStr != null && !isEnableStr.equals("")) {
			isEnable = Boolean.parseBoolean(isEnableStr);
		}
		if (gameTemplateSeqStr != null && !gameTemplateSeqStr.equals("")) {
			long gameTemplateSeq = Long.parseLong(gameTemplateSeqStr);
			GameTemplates gameTemplate = new GameTemplates();
			gameTemplate.setSeq(gameTemplateSeq);
			game.setGameTemplate(gameTemplate);
			game.setMaxQuestions(gameTemplate.getMaxQuestions());
			game.setImagePath(gameTemplate.getImagePath());
		}
		String selectedQuestionsString = request
				.getParameter(IConstants.SELECTED_CHILDREN_ROWS);
		String[] selectedQuestionsSeqsStrArray = selectedQuestionsString
				.split(",");

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
		game.setProject(ApplicationContext.getApplicationContext()
				.getAdminWorkspaceProject(request));
		GameDataStoreI GDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getGameDataStore();
		String status = IConstants.SUCCESS;
		String message = IConstants.SAVED_SUCCESSFULLY;
		try {
			GDS.Save(game);
			game = GDS.findBySeqWithQuesAnswers(game.getSeq());
			generateQuestionXml(game);
			json.put(IConstants.LAST_MODIFIED,
					DateUtils.getGridDateFormat(game.getLastModifiedDate()));
			json.put(IConstants.CREATED_ON,
					DateUtils.getGridDateFormat(game.getCreatedOn()));
		} catch (Exception e) {
			status = IConstants.FAILURE;
			message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, game.getSeq());

		return json;
	}

	public JSONObject addGameFromImportQuestion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		String gameTemplateSeqStr = request.getParameter("gameTempSeq");
		String gameSeqStr = request.getParameter("gameSeq");
		String questionSeqsStr = request.getParameter("quesSeqs");
		String campaignSeqStr = request.getParameter("campaignSeq");
		List<Questions> questionsList = new ArrayList<Questions>();
		String status = IConstants.SUCCESS;
		String message = IConstants.SAVED_SUCCESSFULLY;
		Game game = new Game();
		try {
			if (questionSeqsStr != null && !questionSeqsStr.equals("")) {
				String[] questionSeqs = questionSeqsStr.split(",");
				QuestionDataStoreI QDS = ApplicationContext
						.getApplicationContext().getDataStoreMgr()
						.getQuestionDataStore();
				for (String seq : questionSeqs) {
					Long questionSeq = Long.parseLong(seq);
					Questions question = QDS.findBySeq(questionSeq);
					questionsList.add(question);

				}
			}
			GameTemplatesDataStoreI GTDS = ApplicationContext
					.getApplicationContext().getDataStoreMgr()
					.getGameTemplateDataStore();
			GameDataStoreI GDS = ApplicationContext.getApplicationContext()
					.getDataStoreMgr().getGameDataStore();
			game.setLastModifiedDate(new Date());
			game.setProject(ApplicationContext.getApplicationContext()
					.getAdminWorkspaceProject(request));
			if (gameTemplateSeqStr != null && !gameTemplateSeqStr.equals("")) {
				GameTemplates GT = GTDS.findBySeq(Long
						.parseLong(gameTemplateSeqStr));
				game.setTitle(GT.getName());
				game.setDescription(GT.getDescription());
				game.setEnable(true);
				game.setGameTemplate(GT);
				game.setQuestions(questionsList);
				game.setImagePath(GT.getImagePath());
				game.setMaxQuestions(GT.getMaxQuestions());
				json.put("gameSeq", game.getSeq());
			} else if (gameSeqStr != null && !gameSeqStr.equals("")) {
				game = GDS.findBySeqWithQuesAnswers(Long.parseLong(gameSeqStr));
				List<Questions> savedQuestions = game.getQuestions();
				if (savedQuestions == null) {
					savedQuestions = new ArrayList<Questions>();
				}
				savedQuestions.addAll(questionsList);
				game.setQuestions(savedQuestions);
			}
			game.setCreatedOn(new Date());
			GDS.Save(game);
			if (campaignSeqStr != null && !campaignSeqStr.equals("")) {
				CampaignMgrI campaignMgr = ApplicationContext
						.getApplicationContext().getCampaiMgr();
				Long campaignSeq = Long.parseLong(campaignSeqStr);
				List<Game> games = new ArrayList<Game>();
				games.add(game);
				campaignMgr.saveCampaignGames(campaignSeq, games,false);
			}

		} catch (Exception e) {
			status = IConstants.FAILURE;
			message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put("gameSeq", game.getSeq());
		return json;
	}

	private void generateQuestionXml(Game game) {
		XstreamUtil xstreamUtil = new XstreamUtil();
		String str = xstreamUtil.generateQuestionsXML(game);
		String filePath = IConstants.APPLICATION_FILE_PATH + "\\"
				+ game.getSeq() + ".txt";
		FileUtils.saveTextFile(str, filePath);
	}

	private JSONObject toJson(Game game) {
		JSONObject json = new JSONObject();
		try {
			json.put(IConstants.SEQ, game.getSeq());
			json.put(IConstants.GAME_TITLE, game.getTitle());
			json.put(IConstants.GAME_DESCRIPTION, game.getDescription());
			json.put(IConstants.IS_ENABLED, game.isEnable());
			json.put("isPublished", game.isPublished());
			json.put("gameMaxSecondsAllowed", game.getMaxSecondsAllowed());
			json.put(IConstants.CREATED_ON,	DateUtils.getGridDateFormat(game.getCreatedOn()));
			json.put(IConstants.LAST_MODIFIED_DATE,	DateUtils.getGridDateFormat(game.getLastModifiedDate()));
			json.put("imagePath", game.getImagePath());
			if (game.getGameTemplate() != null) {
				json.put("gameTemplate", game.getGameTemplate().getSeq());
			}
			json.put("maxQuestions", game.getMaxQuestions());
			int totalQuestions = 0;
			if(game.getQuestions()!= null){
				totalQuestions = game.getQuestions().size();
			}
			json.put("totalQuestions", totalQuestions);
			if(totalQuestions < game.getMaxQuestions()){
				json.put("status","incompleted");
			}
			if(game.getGameResult()!= null){
				JSONObject resultJSON = new JSONObject();
				resultJSON.put("scoreEarned",game.getGameResult().getTotalScore());
				resultJSON.put("timeTook",game.getGameResult().getTotalTime());
				resultJSON.put("playedOn",game.getGameResult().getCreatedOn());
				json.put("result", resultJSON);
			}
		} catch (Exception e) {

		}
		return json;
	}

	@Override
	public JSONObject delete(long gameSeq) throws Exception {
		JSONObject json = new JSONObject();
		String status = IConstants.SUCCESS;
		String message = IConstants.msg_DeletedSuccessfully;
		try {
			GameDataStoreI GDS = ApplicationContext.getApplicationContext()
					.getDataStoreMgr().getGameDataStore();
			GDS.Delete(gameSeq);

		} catch (Exception e) {
			status = IConstants.FAILURE;
			message = IConstants.ERROR + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, gameSeq);
		return json;
	}

	@Override
	public JSONArray deleteBulk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if (idsStr != null && !idsStr.equals("")) {
			String[] ids = idsStr.split(",");
			for (String id : ids) {
				long gameSeq = Long.parseLong(id);
				json = this.delete(gameSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}

	@Override
	public List<Game> getGames(HttpServletRequest request) {
		String gameSeqsStr = request.getParameter("gameSeqs");
		String[] gameSeqsStrArr = gameSeqsStr.split(",");
		Long[] gameSeqsArr = new Long[gameSeqsStrArr.length];
		for(int i=0;i<gameSeqsStrArr.length; i++){
			gameSeqsArr[i] = Long.parseLong(gameSeqsStrArr[i]);
		}
		GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
		List<Game> games = GDS.findBySeqs(true, gameSeqsArr);
		return games;
	}

	@Override
	public JSONArray getJSONArray(List<Game> games) {
		JSONArray jsonArray = new JSONArray();
		for(Game game: games){
			JSONObject json = toJson(game);
			jsonArray.put(json);
		}
		return jsonArray;
	}

	@Override
	public JSONArray getGameQuestionAnswersJSONByGameSeq(
			HttpServletRequest request) {
		String gameSeqStr = request.getParameter("gameSeq");
		JSONArray mainJsonarr = new JSONArray();
		try{
			long gameSeq = Long.parseLong(gameSeqStr);
			GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
			Game game = GDS.findBySeqWithQuesAnswers(gameSeq);
			
			for(Questions question : game.getQuestions()){
				JSONObject json = new JSONObject();
				json.put("title", question.getTitle());
				json.put("description", question.getDescription());
				StringBuilder answersString = new StringBuilder();
				for(QuestionAnswers answer : question.getQuestionAnswers()){
					if(question.getAnswerId() == answer.getSeq()){
						answersString.append("<span style='color:green'>"+ answer.getAnswerTitle() +"</span><br>");
					}else{
						answersString.append("<span style='color:red'>"+ answer.getAnswerTitle() +"</span><br>");
					}
					
				}
				json.put("answers", answersString.toString());
				mainJsonarr.put(json);
			}
		}catch(Exception e){
			logger.error("error while getGameQuestionAnswrsCall",e);
		}
		return mainJsonarr;
	}

	@Override
	public JSONObject updateGameDetails(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try{
			String gameSeqStr = request.getParameter("gameSeq");
			String gameTitle = request.getParameter("gameTitle");
			String gameDescription = request.getParameter("gameDescription");
			
			GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
			Game game = GDS.findBySeq(Long.parseLong(gameSeqStr));
			game.setTitle(gameTitle);
			game.setDescription(gameDescription);
			GDS.saveGameDetails(game);
			
			json.put(IConstants.STATUS, IConstants.SUCCESS);
			json.put(IConstants.MESSAGE, "Game updated Successfully");
		}catch(Exception e){
			logger.error("Error occured while updating game details",e);
			try{
				json.put(IConstants.STATUS, IConstants.FAILURE);
				json.put(IConstants.MESSAGE, "Game failed to update."+ e.getMessage());
			}catch(Exception e1){
				logger.error(e1);
			}
		}
		
		return json;
	}
	//Used in User UI
	@Override
	public JSONArray getGamesByCampaign(HttpServletRequest request) {
		String campaignSeqStr = request.getParameter("campaignSeq");
		GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
		List<Game> games  = GDS.findSelectedForCampaign(Long.parseLong(campaignSeqStr));
		JSONArray jsonArray = new JSONArray();
		ResultsDataStoreI RDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getResultsDataStore();
		for(Game game : games){
			Result result = RDS.findByCampaignAndGameAndUser(Long.parseLong(campaignSeqStr),
					game.getSeq(),
					ApplicationContext.getApplicationContext().getLoggedinUser(request).getSeq());
			game.setGameResult(result);
			JSONObject json = toJson(game);
			jsonArray.put(json);
		}
		return jsonArray;
	}

	@Override
	public JSONObject removeQuestionFromGame(HttpServletRequest request) {
		String gameSeqStr = request.getParameter("gameSeq");
		String idsStr = request.getParameter("ids");
		JSONObject json = new JSONObject();
		if(idsStr != null && !idsStr.equals("")){
			long gameSeq = Long.parseLong(gameSeqStr);
			String[] ids = idsStr.split(",");
			if(ids.length>0){
				for(String id : ids){
					try{
						long questionSeq = Long.parseLong(id);
						GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
						GDS.deleteGameQuestion(gameSeq, questionSeq);
	
						json.put(IConstants.STATUS, IConstants.SUCCESS);
						json.put(IConstants.MESSAGE, IConstants.msg_DeletedSuccessfully);
						json.put(IConstants.SEQ, questionSeq);
						
					}catch(Exception e){
					}
				}
			}
			
		}
		return json;
	}


}
