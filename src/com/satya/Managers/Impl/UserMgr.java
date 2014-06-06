package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import validator.AEValidationMessage;
import validator.AEValidatorFactory;

import com.satya.ApplicationContext;
import com.satya.CustomExceptionHandler;
import com.satya.IConstants;
import com.satya.ImportedSet;
import com.satya.RowImporterI;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;
import com.satya.BusinessObjects.UserGroup;
import com.satya.Managers.UserMgrI;
import com.satya.Persistence.UserDataStoreI;
import com.satya.Utils.DateUtils;
import com.satya.Utils.ImportUtils;
import com.satya.importmgmt.UserImporter;
import com.satya.mail.MailerI;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class UserMgr implements UserMgrI{
	private static final String USER_GROUP_DESCRIPTION = "userGroupDescription";
	private static final String USER_GROUP_NAME = "userGroupName";
	private static final String USER_GROUP_SEQ = "userGroupSeq";
	private static final String LAST_MODIFIED = "lastModified";
	private static final String DELETED_SUCCESSFULLY = "Deleted Successfully";
	private static final String USER_SAVED_SUCCESSFULLY = "User Saved Successfully";
	private static final String MESSAGE = "message";
	private static final String STATUS = "status";
	private static final String ERROR = "Error: ";
	private static final String FAILD = "failed";
	private static final String SUCCESS = "success";
	private static final String CREATEDON = "createdOn";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String LOCATION = "location";
	private static final String MOBILE = "mobile";
	private static final String EMAIL = "email";
	private static final String NAME = "name";
	private static final String ID = "seq";
	
	@Override
	public JSONArray getAvailableUsersOnGroupJson(UserGroup userGroup)throws Exception{
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		List<User> users = null;
		users = UDS.findSelectedByUserGroupSeq(userGroup.getSeq());
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(User user: users){ 
			JSONObject json = toJson(user);
			json.put(USER_GROUP_NAME, userGroup.getName());
			json.put(USER_GROUP_DESCRIPTION, userGroup.getDescription());
			json.put(USER_GROUP_SEQ, userGroup.getSeq());
			jsonArr.put(json);
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	
	@Override
	public JSONArray getAvailableUsersOnGroupJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, Exception {
		String userGroupSeqStr = request.getParameter(USER_GROUP_SEQ);
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		List<User> users = null;
		if(userGroupSeqStr.equals("")){
			users = UDS.findAll();
		}else{
			users = UDS.findAvailableByUserGroupSeq(Long.parseLong(userGroupSeqStr));
		}
		
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(User user: users){ 
			JSONObject json = toJson(user);
			json.put(USER_GROUP_SEQ, userGroupSeqStr);
			jsonArr.put(toJson(user));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	@Override
	public JSONArray getSelectedUsersOnGroupJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userGroupSeqStr = request.getParameter(USER_GROUP_SEQ);
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		List<User> users = null;
		if(userGroupSeqStr.equals("")){
			users = new ArrayList<User>();
		}else{
			users = UDS.findSelectedByUserGroupSeq(Long.parseLong(userGroupSeqStr));
		}
		
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(User user: users){ 
			jsonArr.put(toJson(user));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	
	public List<User> getAllUsers(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Project currentProject = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
		List<User> users = null;
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		users = UDS.findByProjectSeq(currentProject.getSeq());
		return users;		
	}
	public JSONArray getAllUserJson (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		List<User> users = getAllUsers(request,response);
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(User user: users){ 
			jsonArr.put(toJson(user));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
	
	public static JSONObject toJson(User user){		
		JSONObject json = new JSONObject();
		try{
			json.put(ID, user.getSeq());
			json.put("nameUser", user.getName());
			json.put(EMAIL, user.getEmail());
			json.put(MOBILE, user.getMobile());
			json.put(LOCATION, user.getLocation());
			json.put(USERNAME, user.getUsername());
			json.put(PASSWORD, user.getPassword());
			json.put(CREATEDON, DateUtils.getGridDateFormat(user.getLastModifiedDate()));
			json.put(IConstants.IS_ENABLED, user.isEnabled());
			json.put(IConstants.LAST_MODIFIED_DATE,DateUtils.getGridDateFormat(user.getLastModifiedDate()));
		}catch( Exception e){
			
		}
		return json;
	}
	
	public JSONObject addUser(HttpServletRequest request, HttpServletResponse response)
										 			throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		User user = new User();
		String id = request.getParameter(ID);
		user.setSeq(Long.valueOf(id));
		String status = SUCCESS;
		String message = USER_SAVED_SUCCESSFULLY;
		try{
			UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
			String userName = request.getParameter(USERNAME);
			Project project = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
			User dupeUser = UDS.findByUserNameAndProject(userName,project.getSeq());
			if(dupeUser != null && (dupeUser.getSeq() != user.getSeq())){
				status = FAILD;
				message = ERROR + " User with username "+ userName + " already exists";
			}else{
				user = saveUserInternal(request,response);
				json.put(LAST_MODIFIED, DateUtils.getGridDateFormat(user.getLastModifiedDate()));
				json.put(IConstants.CREATED_ON, DateUtils.getGridDateFormat(user.getCreatedOn()));
			}
		}catch(Exception  e){
			status = FAILD;
			message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(ID, user.getSeq());
				
		return json;
	}
	
	public User saveUserInternal(HttpServletRequest request, HttpServletResponse response)
							throws Exception{
		User user = new User();		
		String id = request.getParameter(ID);
		String name = request.getParameter("nameUser");
		String email = request.getParameter(EMAIL);
		String mobile = request.getParameter(MOBILE);
		String location = request.getParameter(LOCATION);
		String userName = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String isEnabledStr = request.getParameter(IConstants.IS_ENABLED);		
		long userSeq = 0;
		if(id != null && !id.equals("")){
			userSeq = Long.parseLong(id);	
		}
		user.setSeq(userSeq);
		user.setName(name);
		user.setEmail(email);
		user.setMobile(mobile);
		user.setLocation(location);
		user.setUsername(userName);
		user.setPassword(password);
		user.setProject(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request));
		
		boolean isEnabled = false;
		if(isEnabledStr != null && !isEnabledStr.equals("")){
			isEnabled = Boolean.parseBoolean(isEnabledStr);
		}
		user.setEnabled(isEnabled);
		user.setCreatedOn(new Date());
		user.setLastModifiedDate(new Date());
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		try{
			UDS.Save(user);
			return user;
		}catch(Exception  e){
			throw e;
		}
	}
	public JSONObject delete(long userSeq)throws Exception{
		JSONObject json = new JSONObject();
		String status = SUCCESS;
		String message = DELETED_SUCCESSFULLY;
		try{
			UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
			UDS.Delete(userSeq);			
		}catch (Exception e){
			 status = FAILD;
			 message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(ID, userSeq);
		return json;
	}
	public JSONArray deleteBulk(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException,Exception{
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if(idsStr != null && !idsStr.equals("")){
			String[] ids = idsStr.split(",");
			for(String id : ids){
				long userSeq = Long.parseLong(id);
				json = this.delete(userSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}
	
	@Override
	public void signup(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String name = request.getParameter(NAME);
		String email = request.getParameter(EMAIL);
		String mobile = request.getParameter(MOBILE);
		String location = request.getParameter(LOCATION);
		String userName = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String projectToken = request.getParameter("token");
		String projectSeqStr = ApplicationContext.getApplicationContext().getSecurityUtil().getDecryptedString(projectToken);
		long projectSeq = Long.parseLong(projectSeqStr);
		
		List<String> errorMsgs = new ArrayList<String>();
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setMobile(mobile);
		user.setLocation(location);
		user.setUsername(userName);
		user.setPassword(password);
		user.setProject(new Project(projectSeq));
		user.setEnabled(false);
		user.setCreatedOn(new Date());
		user.setLastModifiedDate(new Date());
		
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		User userAlreadyExists = UDS.findByUserNameAndProject(userName,projectSeq);
		
		if(userAlreadyExists != null){
			errorMsgs.add("User with username '" + userName
					+ "' already exists, Enter some other username");
		}
		
		if (errorMsgs != null && errorMsgs.size() > 0) {
			request.setAttribute(IConstants.errMessages, errorMsgs);
			request.setAttribute("registeringUser", user);
			request.getRequestDispatcher(IConstants.admin_SignupPage).forward(request,response);
		} else {
			try{
				UDS.Save(user);
				MailerI mailer = ApplicationContext.getApplicationContext().getMailer();
				mailer.sendUserSignUpNotification(user);
				request.getRequestDispatcher(IConstants.user_ThanksRegistrationPage).forward(request,response);
			}catch(Exception e){
				
			}
		}
		
	}
	@Override
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		User user = UDS.findByUserNamePassword(userName, password);
		
		List<String> errorMsgs = new ArrayList<String>();

		if (user == null) {
			errorMsgs.add(IConstants.err_invalidUsernamePassword);
			request.setAttribute(IConstants.errMessages, errorMsgs);
			request.getRequestDispatcher(IConstants.user_loginIndex).forward(request, response);
		} else {
			HttpSession session = request.getSession(true);
			session.setAttribute(IConstants.loggedInUser, user);
			request.setAttribute(IConstants.loggedInUser, user);
			
			String sourceURL = (String) session.getAttribute(IConstants.SOURCE_URL);
			if (sourceURL != null) {
				sourceURL = sourceURL.substring(1);
				request.getRequestDispatcher(IConstants.user_DashboardPage).forward(request,response);
			} else {
				request.getRequestDispatcher("/"+IConstants.user_DashboardPage).forward(request,response);
			}
		}
		
	}
	
	@Override
	public void changePassword(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setAttribute(IConstants.loggedInUser, null);
		request.setAttribute(IConstants.loggedInUser, null);
		request.getRequestDispatcher(IConstants.user_loginIndex).forward(request,response);
		
	}
	public List<User> successImportedUsers = null;
	public List<AEValidationMessage> validationMessages = null;
	@Override
	public JSONObject importFromXls(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Project currentProject = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
		UserImporter importer =  new UserImporter(currentProject);
		JSONObject jsonArr = importer.importFromXls(request, response);
		return jsonArr;
	}
}










