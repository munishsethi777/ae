package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Result;
import com.satya.BusinessObjects.Result.ResultQuestion;
import com.satya.Managers.ResultsMgrI;
import com.satya.Persistence.ResultsDataStoreI;
import com.satya.Utils.DateUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ResultsMgr implements ResultsMgrI{

	@Override
	public void SaveResult(Result result) {
		ResultsDataStoreI RDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getResultsDataStore();
		RDS.Save(result);
		
	}

	@Override
	public Result ConvertXMLToResult(String xml) {
		return convertToObject(xml);
	}

	private Result convertToObject(String xml){
//		xml = "<?xml version='1.0' encoding='utf-8'?>" +
//				"<result userId='undefined' gameId='undefined' totalScore='15' totalTime='0' campaignSetSeq='undefined'>" +
//				"<question id='1' timeTaken='0' selectedAnswerId='3' points='10' attempts='0' negativePoints='0' />" +
//				"<question id='2' timeTaken='0' selectedAnswerId='3' points='5' attempts='0' negativePoints='0' /></result>";
//		
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("result", Result.class);
		xstream.useAttributeFor(Result.class, "userId");
		xstream.useAttributeFor(Result.class, "gameId");
		xstream.useAttributeFor(Result.class, "totalScore");
		xstream.useAttributeFor(Result.class, "totalTime");
		
		xstream.alias("questions", List.class);
		xstream.alias("question", ResultQuestion.class);
		xstream.addImplicitCollection(Result.class, "questions");
		
		xstream.useAttributeFor(ResultQuestion.class, "id");
		xstream.useAttributeFor(ResultQuestion.class, "timeTaken");
		xstream.useAttributeFor(ResultQuestion.class, "selectedAnswerId");
		xstream.useAttributeFor(ResultQuestion.class, "points");
		xstream.useAttributeFor(ResultQuestion.class, "attempts");
		xstream.useAttributeFor(ResultQuestion.class, "negativePoints");
		
		try{
			Result result = (Result)xstream.fromXML(xml); 
			return result;
		}catch(Exception e){
			System.out.print(e);
		}
		return null;
	}

	@Override
	public List<Result> getGamesByCampaign(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String campaignSeqStr = request.getParameter("campaignSeq");
		if(campaignSeqStr != null && !campaignSeqStr.equals("")){
			ResultsDataStoreI RDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getResultsDataStore();
			return RDS.findByCampaign(Long.valueOf(campaignSeqStr));
		}
		return null;
	}

	@Override
	public JSONArray getGamesJSONByCampaign(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		List<Result> results = this.getGamesByCampaign(request, response);
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(Result result: results){ 
			jsonArr.put(toJson(result));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	private JSONObject toJson(Result result){		
		JSONObject json = new JSONObject();
		try{
			json.put("seq", result.getSeq());
			json.put("userName", result.getUser().getName());
			json.put("createdOn", DateUtils.getGridDateFormat(result.getCreatedOn()));
			json.put("totalScore", result.getTotalScore());
			json.put("timeTaken", result.getTotalTime());
			json.put("campaignName", result.getCampaign().getName());
			json.put("setName", result.getSet().getName());
			json.put("userGroupName", result.getUserGroup().getName());
			json.put("gameName", result.getGame().getTitle());
		}catch( Exception e){
			
		}
		return json;
	}
}
