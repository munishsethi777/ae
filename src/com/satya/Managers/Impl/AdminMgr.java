package com.satya.Managers.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.ImportedSet;
import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;
import com.satya.Managers.AdminMgrI;
import com.satya.Managers.ProjectMgrI;
import com.satya.Managers.QuestionsMgrI;
import com.satya.Managers.UserMgrI;
import com.satya.Persistence.AdminDataStoreI;
import com.satya.Persistence.UserDataStoreI;
import com.satya.Utils.ImportUtils;
import com.satya.mail.MailerI;


public class AdminMgr implements AdminMgrI {

	Logger log = Logger.getLogger(AdminMgr.class.getName());

	@Override
	public List<Admin> getAllAdmins() throws ServletException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray getAllAdminsJson() throws ServletException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void signup(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String emailId = request.getParameter("emailId");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String fullName = request.getParameter("fullName");
		String mobile = request.getParameter("mobile");
		String city = request.getParameter("city");
		
		List<String> errorMsgs = new ArrayList<String>();
		Admin admin = new Admin();
		admin.setEmail(emailId);
		admin.setUsername(userName);
		admin.setPassword(password);
		admin.setName(fullName);
		admin.setMobile(mobile);
		admin.setCity(city);
		admin.setCreatedon(new Date());
		admin.setLastModifiedDate(new Date());
		admin.setIsEnable(false);
		admin.setIsSuperUser(false);
		AdminDataStoreI ADS = ApplicationContext.getApplicationContext().getDataStoreMgr().getAdminDataStore();
		Admin adminExists = ADS.findByUserName(userName);
		if (adminExists != null) {
			errorMsgs.add("User with username '" + userName
					+ "' already exists, Enter some other username");
		}
		
		if (errorMsgs != null && errorMsgs.size() > 0) {
			request.setAttribute(IConstants.errMessages, errorMsgs);
			request.setAttribute("registeringAdmin", admin);
			request.getRequestDispatcher(IConstants.admin_SignupPage).forward(request,response);
		} else {
			ADS.Save(admin);
			MailerI mailer = ApplicationContext.getApplicationContext().getMailer();
			mailer.sendAdminSignUpNotification(admin);
			HttpSession session = request.getSession(true);
			session.setAttribute(IConstants.loggedInAdmin, admin);
			request.setAttribute(IConstants.loggedInAdmin, admin);
			request.getRequestDispatcher(IConstants.admin_DashboardPage).forward(request,response);
		}

	}
	
	@Override
	public void updateAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String emailId = request.getParameter("emailId");
		String fullName = request.getParameter("fullName");
		String mobile = request.getParameter("mobile");
		String city = request.getParameter("city");
		HttpSession session = request.getSession(true);
		List<String> errorMsgs = new ArrayList<String>();
		List<String> msgs = new ArrayList<String>();
		Admin admin = (Admin)session.getAttribute(IConstants.loggedInAdmin);
		admin.setEmail(emailId);
		admin.setName(fullName);
		admin.setMobile(mobile);
		admin.setCity(city);
		admin.setLastModifiedDate(new Date());		
		AdminDataStoreI ADS = ApplicationContext.getApplicationContext().getDataStoreMgr().getAdminDataStore();	
		try{
			ADS.Save(admin);
			msgs.add("Updated Successfully");
			session.setAttribute(IConstants.loggedInAdmin, admin);
			request.setAttribute(IConstants.loggedInAdmin, admin);
		}catch(Exception e){
			errorMsgs.add("Error During Update Adminseq : " + admin.getSeq() + " " + e.getMessage() );
		}
		if (errorMsgs != null && errorMsgs.size() > 0) {
			request.setAttribute(IConstants.errMessages, errorMsgs);			
		} else {
			request.setAttribute(IConstants.sccMessages, msgs);	
		}
		request.getRequestDispatcher(IConstants.admin_MYAccountPage).forward(request,response);
	}
	@Override
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		AdminDataStoreI ADS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getAdminDataStore();
		Admin admin = ADS.findByUserNamePassword(userName, password);
		List<String> errorMsgs = new ArrayList<String>();

		if (admin == null) {
			errorMsgs.add(IConstants.err_invalidUsernamePassword);
			request.setAttribute(IConstants.errMessages, errorMsgs);
			request.getRequestDispatcher(IConstants.admin_loginIndex).forward(request, response);
		} else {
			HttpSession session = request.getSession(true);
			session.setAttribute(IConstants.loggedInAdmin, admin);
			request.setAttribute(IConstants.loggedInAdmin, admin);
			if(admin.getProjects() != null && admin.getProjects().size() > 0){
				session.setAttribute(IConstants.adminWorkspaceProject, admin.getProjects().get(0));
			}
			String sourceURL = (String) session.getAttribute(IConstants.SOURCE_URL);
			if (sourceURL != null) {
				sourceURL = sourceURL.substring(1);
				request.getRequestDispatcher(IConstants.admin_DashboardPage).forward(request,response);
			} else {
				request.getRequestDispatcher("/"+IConstants.admin_DashboardPage).forward(request,response);
			}
		}

	}

	public void reloadSessionUser(HttpServletRequest request) {
		UserDataStoreI UDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getUserDataStore();
		HttpSession session = request.getSession(true);
		User loggedUser = (User) session.getAttribute(IConstants.loggedInUser);
		User user = UDS.findBySeq(loggedUser.getSeq());
		session.setAttribute(IConstants.loggedInUser, user);
		request.setAttribute(IConstants.loggedInUser, user);
	}

	public void changePassword(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<String> errorMsgs = new ArrayList<String>();
		String currentPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");

		if (newPassword != null && confirmPassword != null) {
			if (!newPassword.equals(confirmPassword)) {
				errorMsgs
						.add("New Password and Confirm Password can not be different");
			}
		}
		if (!ApplicationContext.getApplicationContext()
				.getLoggedinUser(request).getPassword().equals(currentPassword)) {
			errorMsgs.add("False Current Password. Pls check");
		}
		if (errorMsgs != null && errorMsgs.size() > 0) {
			request.setAttribute(IConstants.errMessages, errorMsgs);

		} else {
			UserDataStoreI UDS = ApplicationContext.getApplicationContext()
					.getDataStoreMgr().getUserDataStore();
			User user = UDS.findBySeq(ApplicationContext
					.getApplicationContext().getLoggedinUser(request).getSeq());
			user.setPassword(newPassword);
			UDS.changePassword(user);
			HttpSession session = request.getSession(true);
			session.setAttribute(IConstants.loggedInUser, user);
			request.setAttribute(IConstants.loggedInUser, user);
			List<String> sccMsgs = new ArrayList<String>();
			sccMsgs.add("Password Updated successfully.");
			request.setAttribute(IConstants.sccMessages, sccMsgs);
		}
		request.getRequestDispatcher("myAccount.jsp")
				.forward(request, response);
	}

	@Override
	public JSONObject changeWorkspaceProject(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		try{
			String projectSeq = request.getParameter("projectSeq");
			ProjectMgrI projMgr = ApplicationContext.getApplicationContext().getProjectMgr();
			Project project = projMgr.getProject(Integer.parseInt(projectSeq));
			HttpSession session = request.getSession(true);
			session.setAttribute(IConstants.adminWorkspaceProject, project);
			json.put(IConstants.STATUS, IConstants.SUCCESS);
		}catch(Exception e){
			try{
				json.put(IConstants.STATUS, IConstants.FAILURE);
			}catch(JSONException jsonException){}
		}
		return json;
		
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setAttribute(IConstants.loggedInAdmin, null);
		request.setAttribute(IConstants.loggedInAdmin, null);
		request.getRequestDispatcher(IConstants.admin_loginIndex).forward(request,response);
	}
	
	public void downloadFailedRows(HttpServletRequest request, HttpServletResponse response){
		ImportUtils importUtils = new ImportUtils();
		try {
			importUtils.downloadFailedListingsCSV(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@Override
	public JSONObject importUsersFromXls(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		UserMgrI userMgr = ApplicationContext.getApplicationContext().getUserMgr();
		JSONObject jsonArr = null;
		try{
			jsonArr = userMgr.importFromXls(request,response);
		}catch(Exception e){
			String msg = e.getMessage();
		}
		return jsonArr;
	}
	
	@Override
	public JSONObject importQuestionsFromXls(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		QuestionsMgrI quesMgr = ApplicationContext.getApplicationContext().getQuestionMgr();
		JSONObject jsonArr = null;
		try{
			jsonArr = quesMgr.importFromXls(request,response);
		}catch(Exception e){
			String msg = e.getMessage();
		}
		return jsonArr;
	}
	
}
