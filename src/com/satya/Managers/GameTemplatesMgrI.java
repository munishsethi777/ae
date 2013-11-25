package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.satya.BusinessObjects.GameTemplates;

public interface GameTemplatesMgrI {
	
	public List<GameTemplates> getAllGameTemplates()throws ServletException, IOException;
	public JSONArray getAllGameTemplateJson (HttpServletRequest request)throws ServletException, IOException;
	
}
