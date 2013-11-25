package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;

public interface ProjectMgrI {
	public Project getProject(long seq);
	public List<Project> getAllProjects()throws ServletException, IOException;
	public JSONArray getAllProjectsJson ()throws ServletException, IOException;
	public JSONObject addProject(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public JSONObject delete(long projectSeq)throws Exception;
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public Project FindBySeq(long seq)throws Exception;
}
