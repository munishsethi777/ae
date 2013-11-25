package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;

public interface QuestionAnswersMgrI {
	public List<QuestionAnswers> getAllQuestionAnswers(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getAllQuestionAnswersJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addQuestionAnswers(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public JSONObject delete(long questionAnswersSeq)throws Exception;
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public List<QuestionAnswers> getQuestionAnswersFromRequest(HttpServletRequest request, HttpServletResponse response,Questions question);
	public void addQuestionAnswers(List<QuestionAnswers>questionAnswers)throws Exception;
	public void deleteByQuestion(long questionsSeq) throws Exception;
	
}
