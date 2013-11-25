package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.Set;

public interface SetMgrI {
	//admin methods
	public List<Set> getAllSets(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getAllSetJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addSet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public JSONObject delete(long setSeq)throws Exception;
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;

	public JSONArray getAvailableOnCampaignJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getSelectedOnCampaignJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	
	//User methods
	public JSONArray getSetsByCampaign(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
}
