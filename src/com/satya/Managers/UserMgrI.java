package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.BusinessObjects.User;
import com.satya.BusinessObjects.UserGroup;

public interface UserMgrI {
	
	public List<User> getAllUsers(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONArray getAllUserJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	public JSONObject addUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	public JSONObject delete(long userSeq)throws Exception;
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception;
	
	public JSONArray getAvailableUsersOnGroupJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, Exception;
	public JSONArray getSelectedUsersOnGroupJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	
	public void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public JSONObject importFromXls(HttpServletRequest request, HttpServletResponse response)throws Exception;
	public User saveUserInternal(HttpServletRequest request, HttpServletResponse response)throws Exception;
	public JSONArray getAvailableUsersOnGroupJson(UserGroup userGroup)throws Exception;

}
