package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.satya.BusinessObjects.GameTemplates;
import com.satya.BusinessObjects.Result;

public interface ResultsMgrI {

	public void SaveResult(Result result);
	public Result ConvertXMLToResult(String xml);

	public List<Result> getGamesByCampaign(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getGamesJSONByCampaign (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	
}
