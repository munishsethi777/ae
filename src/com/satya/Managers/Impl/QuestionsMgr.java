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
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.GameTemplates;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.BusinessObjects.User;
import com.satya.Managers.CampaignMgrI;
import com.satya.Managers.GameMgrI;
import com.satya.Managers.QuestionAnswersMgrI;
import com.satya.Managers.QuestionsMgrI;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.GameTemplatesDataStoreI;
import com.satya.Persistence.QuestionDataStoreI;
import com.satya.Persistence.Impl.GameDataStore;
import com.satya.Utils.DateUtils;
import com.satya.importmgmt.QuestionImporter;
import com.satya.importmgmt.UserImporter;

public class QuestionsMgr implements QuestionsMgrI {
	
	private static final String EXTRA_ATTEMPTS_ALLOWED = "extraAttemptsAllowed";
	private static final String MAX_SECONDS_ALLOWED = "maxSecondsAllowed";
	private static final String NEGATIVE_POINTS = "negativePoints";
	private static final String QUES_TITLE = "quesTitle";
	private static final String POINTS = "points";

	@Override
	public JSONArray getAvailableOnGameJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return getQuestionsByGame(false, request);
	}
	@Override
	public JSONArray getSelectedOnGameJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return getQuestionsByGame(true, request);
	}
	
	private JSONArray getQuestionsByGame(boolean isSelected,HttpServletRequest request){
		String gameSeqStr = request.getParameter("gameSeq");
		QuestionDataStoreI QDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionDataStore();
		
		List<Questions> questions = null;
		if(!isSelected && (gameSeqStr.equals("") || gameSeqStr.equals("0"))){
			questions = QDS.findAll();
		}else{
			if(isSelected){
				questions = QDS.findSelectedByGameSeq(Long.parseLong(gameSeqStr));
			}else{
				questions = QDS.findAvailableByGameSeq(Long.parseLong(gameSeqStr));
			}
		}
		
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(Questions question: questions){ 
			jsonArr.put(toJson(question));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	
	public List<Questions> getAllQuestions(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		List<Questions> questions = null;
		Project currentProject = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
		QuestionDataStoreI QDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionDataStore();
		questions = QDS.findByProjectSeq(currentProject.getSeq());
		return questions;
	}
	public JSONArray getAllQuestionsJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		List<Questions> questions = getAllQuestions(request,response);
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(Questions question: questions){ 
			jsonArr.put(toJson(question));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	
	
	
	public static JSONObject toJson(Questions question){		
		JSONObject json = new JSONObject();
		try{
			json.put(IConstants.SEQ, question.getSeq());
			json.put(QUES_TITLE, question.getTitle());
			json.put(IConstants.DESCRIPTION, question.getDescription());
			json.put(POINTS, question.getPoints());
			json.put(NEGATIVE_POINTS, question.getNegativePoints());
			json.put(MAX_SECONDS_ALLOWED, question.getMaxSecondsAllowed());
			json.put(EXTRA_ATTEMPTS_ALLOWED, question.getExtraAttemptsAllowed());
			json.put(IConstants.CREATED_ON,  DateUtils.getGridDateFormat(question.getCreatedOn()));
			json.put(IConstants.IS_ENABLED, question.IsEnabled());			
			json.put(IConstants.LAST_MODIFIED_DATE, DateUtils.getGridDateFormat(question.getLastModified()));
			List<QuestionAnswers>answers = question.getQuestionAnswers();
			json.put("answer1", answers.get(0));
			json.put("answer2", answers.get(1));
			json.put("answer3", answers.get(2));
			json.put("answer4", answers.get(3));
			String correctAnswer = "1";
			if(answers.get(1).isCorrect()){
				correctAnswer = "2";
			}else if(answers.get(2).isCorrect()){
				correctAnswer = "3";
			}else if(answers.get(3).isCorrect()){
				correctAnswer = "4";
			}
			json.put("isAnswerCorrect",correctAnswer);
			json.put(Questions.IMAGE_SEQ, question.getImageSeq());
		}catch( Exception e){
			
		}
		return json;
	}
	
	public JSONObject addQuestions(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		String id = request.getParameter(IConstants.SEQ);
		String title = request.getParameter(QUES_TITLE);
		String description = request.getParameter(IConstants.DESCRIPTION);
		String pointsStr = request.getParameter(POINTS);
		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);
		String negativePointsStr = request.getParameter(NEGATIVE_POINTS);
		String maxSecondsAllowedStr = request.getParameter(MAX_SECONDS_ALLOWED);
		String extraAttemptsAllowedStr = request.getParameter(EXTRA_ATTEMPTS_ALLOWED);
		String gameTemplateSeqStr = request.getParameter("gameTempSeq");
		String gameSeqStr = request.getParameter("gameSeq");
		String campaignSeqStr = request.getParameter("campaignSeq");
		String hintStr = request.getParameter("hint");
		
		Questions questions = new Questions();
		long questionsSeq = 0;
		if(id != null && !id.equals("")){
			questionsSeq = Long.parseLong(id);	
		}
		boolean isEnable = false;
		if(isEnableStr != null && !isEnableStr.equals("")){
			isEnable = Boolean.parseBoolean(isEnableStr);
		}
		Integer points = 0;
		if(pointsStr != null && !pointsStr.equals("")){
			try{
				points = Integer.parseInt(pointsStr);
			}catch(Exception e)
			{}
		}
		int negativePoints = 0;
		if(negativePointsStr != null && !pointsStr.equals("")){
			try{
				negativePoints = Integer.parseInt(negativePointsStr);
			}catch(Exception e){}
		}
		int maxSecondsAllowed = 0;
		if(maxSecondsAllowedStr != null && !maxSecondsAllowedStr.equals("")){
			try{
				maxSecondsAllowed = Integer.parseInt(maxSecondsAllowedStr);
			}catch(Exception e){}
		}
		int extraAttemptsAllowed = 0;
		if(extraAttemptsAllowedStr != null && !extraAttemptsAllowedStr.equals("")){
			try{
				extraAttemptsAllowed = Integer.parseInt(maxSecondsAllowedStr);
			}catch(Exception e){}
		}
		questions.setSeq(questionsSeq);
		questions.setTitle(title);
		questions.setDescription(description);
		questions.setIsEnabled(isEnable);
		questions.setPoints(points);
		questions.setNegativePoints(negativePoints);
		questions.setExtraAttemptsAllowed(extraAttemptsAllowed);
		questions.setMaxSecondsAllowed(maxSecondsAllowed);
		questions.setCreatedOn(new Date());
		questions.setLastModified(new Date());
		questions.setHint(hintStr);
		questions.setProject(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request));
		QuestionAnswersMgrI questionAnswerMgr = ApplicationContext.getApplicationContext().getQuestionAnswersMgr();
		List<QuestionAnswers> questionAnswers = questionAnswerMgr.getQuestionAnswersFromRequest(request, response,questions);
		questions.setQuestionAnswers(questionAnswers);
		QuestionDataStoreI QDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionDataStore();
		
		String status = IConstants.SUCCESS;
		String message = IConstants.SAVED_SUCCESSFULLY;
		try{
			QDS.Save(questions);
			GameTemplatesDataStoreI GTDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameTemplateDataStore();
			GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
			//GameMgrI gameMgr = ApplicationContext.getApplicationContext().getGamesMgr()
			Game game = new Game();
			if(gameTemplateSeqStr != null && !gameTemplateSeqStr.equals("")){				
				GameTemplates GT = GTDS.findBySeq(Long.parseLong(gameTemplateSeqStr));
				List<Questions>questionsList = new ArrayList<Questions>();
				questionsList.add(questions);
				game.setTitle(GT.getName());
				game.setDescription(GT.getDescription());
				game.setEnable(true);
				game.setGameTemplate(GT);
				game.setQuestions(questionsList);
				GDS.Save(game);
				json.put("gameSeq", game.getSeq());
			}else if(gameSeqStr != null && !gameSeqStr.equals("")){
				game = GDS.findBySeqWithQuesAnswers(Long.parseLong(gameSeqStr));
				List<Questions>questionsList = game.getQuestions();
				questionsList.add(questions);
				game.setQuestions(questionsList);
				GDS.Save(game);
				json.put("gameSeq", game.getSeq());
			}	
			if(campaignSeqStr != null && !campaignSeqStr.equals("")){
				CampaignMgrI campaignMgr = ApplicationContext.getApplicationContext().getCampaiMgr();
				Long campaignSeq = Long.parseLong(campaignSeqStr);
				List<Game>games = new ArrayList<Game>();
				games.add(game);
				campaignMgr.saveCampaignGames(campaignSeq,games);
			}
			json.put(IConstants.LAST_MODIFIED,  DateUtils.getGridDateFormat(questions.getLastModified()));
			json.put(IConstants.CREATED_ON,  DateUtils.getGridDateFormat(questions.getCreatedOn()));
		}catch(Exception  e){
			status = IConstants.FAILURE;
			message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, questions.getSeq());
				
		return json;
	}
	
	
	public JSONObject delete(long questionsSeq)throws Exception{
		JSONObject json = new JSONObject();
		String status = IConstants.SUCCESS;;
		String message = IConstants.msg_DeletedSuccessfully;
		try{
			QuestionDataStoreI QDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionDataStore();
			QDS.Delete(questionsSeq);			
		}catch (Exception e){
			 status = IConstants.FAILURE;
			 message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, questionsSeq);
		return json;
	}
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if(idsStr != null && !idsStr.equals("")){
			String[] ids = idsStr.split(",");
			for(String id : ids){
				long questionsSeq = Long.parseLong(id);
				json = this.delete(questionsSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}
	
	@Override
	public JSONObject importFromXls(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Project currentProject = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
		QuestionImporter importer =  new QuestionImporter(currentProject);
		JSONObject jsonArr = importer.importFromXls(request, response);
		return jsonArr;
	}
	
}
