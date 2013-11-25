package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.Managers.QuestionAnswersMgrI;
import com.satya.Persistence.QuestionAnswersDataStoreI;
import com.satya.Persistence.QuestionDataStoreI;

public class QuestionAnswersMgr implements QuestionAnswersMgrI {
	private static final String ANSWER = "answer";
	private static final String IS_ANSWER_CORRECT = "isAnswerCorrect";
	private static final String QUESTION_SEQ = "questionSeq";
	private static final String IS_CORRECT = "isCorrect";
	Logger logger = Logger.getLogger(QuestionAnswersMgr.class);
	
	public List<QuestionAnswers> getAllQuestionAnswers(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		List<QuestionAnswers> gameAnswers = null;
		QuestionAnswersDataStoreI QADS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionAnswersDataStore();
		gameAnswers = QADS.findAll();
		return gameAnswers;
	}
	public JSONArray getAllQuestionAnswersJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		List<QuestionAnswers> questionAnswers = getAllQuestionAnswers(request,response);
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(QuestionAnswers questionAnswer: questionAnswers){ 
			jsonArr.put(toJson(questionAnswer));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			logger.error(e);
		}
		return jsonArr;
	}
	
	private JSONObject toJson(QuestionAnswers questionAnswer){		
		JSONObject json = new JSONObject();
		try{
			json.put(IConstants.SEQ, questionAnswer.getSeq());
			json.put(IConstants.TITLE, questionAnswer.getAnswerTitle());
			json.put(IS_CORRECT, questionAnswer.isCorrect());
			if(questionAnswer.getQuestion() != null)
			json.put(QUESTION_SEQ, questionAnswer.getQuestion().getSeq());
		}catch( Exception e){
			
		}
		return json;
	}
	public List<QuestionAnswers> getQuestionAnswersFromRequest(HttpServletRequest request, HttpServletResponse response ,Questions question){
		List<QuestionAnswers> ansList= new ArrayList<QuestionAnswers>();
		QuestionAnswers answer = null;
		String correctAnswer = request.getParameter(IS_ANSWER_CORRECT);
		int i = 1;
		while(i<=4){
			answer = new QuestionAnswers();
			String ans = request.getParameter(ANSWER + i);
			answer.setAnswerTitle(ans);
			answer.setQuestion(question);
			if(correctAnswer.equals(String.valueOf(i))){
				answer.setCorrect(true);
			}
			ansList.add(answer);
			i++;
		}	
		return ansList;
	}
	public void addQuestionAnswers(List<QuestionAnswers>questionAnswers)throws Exception{
		QuestionAnswersDataStoreI QADS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionAnswersDataStore();
		for(QuestionAnswers questionAnswer:questionAnswers){
			QADS.Save(questionAnswer);
		}
	}
	
	public JSONObject addQuestionAnswers(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		String id = request.getParameter(IConstants.SEQ);
		String title = request.getParameter(IConstants.TITLE);
		String questionSeq = request.getParameter(QUESTION_SEQ);
		String isCorrectStr = request.getParameter(IS_CORRECT);	
		
		
		QuestionAnswers questionsAnswers = new QuestionAnswers();
		long questionAnswerSeq = 0;
		if(id != null && !id.equals("")){
			questionAnswerSeq = Long.parseLong(id);	
		}
		boolean isCorrect = false;
		if(isCorrectStr != null && !isCorrectStr.equals("")){
			isCorrect = Boolean.parseBoolean(isCorrectStr);
		}
		questionsAnswers.setSeq(questionAnswerSeq);
		questionsAnswers.setAnswerTitle(title);
		questionsAnswers.setCorrect(isCorrect);
		//Here We need to set QuestionSeq on QuestionAnswer object
		QuestionAnswersDataStoreI QADS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionAnswersDataStore();
		String status = IConstants.SUCCESS;
		String message = IConstants.SAVED_SUCCESSFULLY;
		try{
			QADS.Save(questionsAnswers);
		}catch(Exception  e){
			status = IConstants.FAILURE;
			message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, questionsAnswers.getSeq());
				
		return json;
	}
	
	public JSONObject delete(long questionAnswersSeq)throws Exception{
		JSONObject json = new JSONObject();
		String status = IConstants.SUCCESS;;
		String message = IConstants.msg_DeletedSuccessfully;
		try{
			QuestionAnswersDataStoreI QADS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionAnswersDataStore();
			QADS.Delete(questionAnswersSeq);			
		}catch (Exception e){
			 status = IConstants.FAILURE;
			 message = IConstants.ERROR + " : " + e.getMessage();
		}
		json.put(IConstants.STATUS, status);
		json.put(IConstants.MESSAGE, message);
		json.put(IConstants.SEQ, questionAnswersSeq);
		return json;
	}
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if(idsStr != null && !idsStr.equals("")){
			String[] ids = idsStr.split(",");
			for(String id : ids){
				long questionAnswersSeq = Long.parseLong(id);
				json = this.delete(questionAnswersSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}
	public void deleteByQuestion(long questionsSeq) throws Exception{
		QuestionAnswersDataStoreI QADS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionAnswersDataStore();
		QADS.DeleteByQuestionseq(questionsSeq);		
	}
}
