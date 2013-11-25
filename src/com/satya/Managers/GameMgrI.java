package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.Game;


public interface GameMgrI {
	public List<Game> getAllGames(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getAllGameJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addGames(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	
	public JSONArray getAvailableOnSetJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getSelectedOnSetJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addGameFromImportQuestion(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException,Exception;
	//USER METHODS
	public void loadPlayer(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
}
