package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.Questions;

public interface QuestionsMgrI {
	public List<Questions> getAllQuestions(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getAllQuestionsJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addQuestions(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public JSONObject delete(long questionsSeq)throws Exception;
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;

	public JSONArray getAvailableOnGameJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getSelectedOnGameJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject importFromXls(HttpServletRequest request, HttpServletResponse response)throws Exception;

}
