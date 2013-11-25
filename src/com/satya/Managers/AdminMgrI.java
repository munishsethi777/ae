package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.Admin;

public interface AdminMgrI {
	
	public List<Admin> getAllAdmins()throws ServletException, IOException;
	public JSONArray getAllAdminsJson ()throws ServletException, IOException;
	public void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void updateAccount(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject changeWorkspaceProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public JSONObject importUsersFromXls(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException;
	public void downloadFailedRows(HttpServletRequest request, HttpServletResponse response);
	public JSONObject importQuestionsFromXls(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException;
	
}
