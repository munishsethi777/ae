package com.satya.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Project;
import com.satya.Managers.AdminMgrI;
import com.satya.Managers.CampaignMgrI;
import com.satya.Managers.GameMgrI;
import com.satya.Managers.GameTemplatesMgrI;
import com.satya.Managers.ImageMgrI;
import com.satya.Managers.ProjectMgrI;
import com.satya.Managers.QuestionsMgrI;
import com.satya.Managers.ResultsMgrI;
import com.satya.Managers.SetMgrI;
import com.satya.Managers.UserGroupMgrI;
import com.satya.Managers.UserMgrI;

public class AdminUserServlet extends BaseServletClass {
	private static final String GET_ALL_GAMES = "getAllGames";
	private static final String UPLOAD_IMAGE = "uploadImage";
	private static final String GET_ALL_IMAGES = "getAllImages";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOGOUT = "logout";
	private static final String LOGIN = "login";
	private static final String SIGNUP = "signup";
	private static final String RELOADCONTEXT = "reloadContext";
	private static final String CHANGEPASSWORD = "changePassword";
	private static final String CHANGE_WORKSPACE_PROJECT = "changeWorkspaceProject";

	// users children for usergroups
	private static final String GET_USERS_AVAILABLE_ON_USERGROUP = "getUsersAvailableOnUserGroup";
	private static final String GET_USERS_SELECTED_ON_USERGROUP = "getUsersSelectedOnUserGroup";
	// usergroup children for campaign
	private static final String GET_USERSGROUPS_AVAILABLE_ON_CAMPAIGN = "getUserGroupsAvailableOnCampaign";
	private static final String GET_USERSGROUPS_SELECTED_ON_CAMPAIGN = "getUserGroupsSelectedOnCampaign";
	// sets children for campaign
	private static final String GET_SETS_AVAILABLE_ON_CAMPAIGN = "getSetsAvailableOnCampaign";
	private static final String GET_SETS_SELECTED_ON_CAMPAIGN = "getSetsSelectedOnCampaign";
	// questions children for games
	private static final String GET_QUESTIONS_AVAILABLE_ON_GAME = "getQuestionsAvailableOnGame";
	private static final String GET_QUESTIONS_SELECTED_ON_GAME = "getQuestionsSelectedOnGame";
	// games children for sets
	private static final String GET_GAMES_AVAILABLE_ON_SET = "getGamesAvailableOnSet";
	private static final String GET_GAMES_SELECTED_ON_SET = "getGamesSelectedOnSet";

	// get GamesTemplates
	private static final String GET_ALL_GAME_TEMPLATES_JSON = "getAllGameTemplates";

	// Results UI Advance Search
	private static final String GET_USERGROUPS_BY_CAMPAIGN = "getUserGroupsByCampaign";
	private static final String GET_ASSESSEMENTSETS_BY_CAMPAIGN = "getAssessmentSetsByCampaign";
	private static final String GET_GAMES_BY_ASSESSMENTSET = "getGamesByAssessmentSet";
	private static final String GET_RESULTS_FOR_GRID = "getResultsForGrid";
	private static final String MY_ACCOUNT = "myAccount";
	private static final String UPDATE_ACCOUNT = "updateAccount";
	private static final String IMPORT_USERS = "importUsers";
	private static final String IMPORT_QUESTIONS = "importQuestions";
	private static final String ADD_GAME_FROM_IMPORT_QUESTIONS = "addGameFromImportQuestion";
	private static final String ADD_USER_GROUP_FROM_CAMPAIGN = "addUserGroupFromCampaign";
	private static final String GET_SELECTED_USERGROUPS_BY_CAMPAIGN = "getSelectedUserGroupsByCampaign";
	private static final String SET_GAMES_ON_CAMPAIGN = "setGamesOnCampaign";

	// Users UI
	private static final String GET_REGISRATION_URL = "getRegistrationUrl";

	Logger log = Logger.getLogger(UserServlet.class.getName());

	public AdminUserServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// try{
		// String plaintextHex =
		// "<?xml version=\"1.0\" encoding=\"utf-8\"?><questions><question id=\"1\" answerId=\"3\" points=\"10\" value=\"Question One?\" description=\"this is description for question 1\"><answer id=\"1\">two balls</answer><answer id=\"2\">four balls</answer><answer id=\"3\">six balls</answer><answer id=\"4\">three balls</answer></question><question id=\"2\" answerId=\"3\" points=\"10\" value=\"Question 2?\" description=\"this is description for question 1\"><answer id=\"1\">Incorrect</answer><answer id=\"2\">incorrect</answer><answer id=\"3\">sahi</answer><answer id=\"4\">galat</answer></question></questions>";
		// //String plaintextHex = "Munish Sethi Ji";
		//
		// String password = "m8dwhZQB7+Y=";
		// DESKeySpec key = new DESKeySpec(password.getBytes());
		// SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		// cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(key));
		// String encryptedString = encryptBase64(plaintextHex,cipher);
		// System.out.println(encryptedString);
		// }catch(Exception e){
		// System.out.print(e.getMessage());
		// }

		List<String> errorMsgs = new ArrayList<String>();
		String action = request.getParameter("action");
		log.debug("Get Action on Admin User Servlet called:" + action);
		try {
			if (action.equals("")) {
				request.getRequestDispatcher(IConstants.admin_loginIndex)
						.forward(request, response);
				return;
			}
			if (getLoggedinAdmin(request) == null) {
				errorMsgs.add(IConstants.err_pleaseLogin);
				request.setAttribute(IConstants.errMessages, errorMsgs);

				request.getRequestDispatcher(IConstants.admin_loginIndex)
						.forward(request, response);
			} else {

				UserMgrI userMgr = ApplicationContext.getApplicationContext()
						.getUserMgr();
				ProjectMgrI projectMgr = ApplicationContext
						.getApplicationContext().getProjectMgr();
				SetMgrI setMgr = ApplicationContext.getApplicationContext()
						.getSetMgr();
				GameMgrI gameMgr = ApplicationContext.getApplicationContext()
						.getGamesMgr();
				CampaignMgrI campaignMgr = ApplicationContext
						.getApplicationContext().getCampaiMgr();
				UserGroupMgrI userGroupMgr = ApplicationContext
						.getApplicationContext().getUserGroupMgr();
				GameTemplatesMgrI gameTemplateMgr = ApplicationContext
						.getApplicationContext().getGameTemplateMgr();
				QuestionsMgrI questionMgr = ApplicationContext
						.getApplicationContext().getQuestionMgr();
				ResultsMgrI resultsMgr = ApplicationContext
						.getApplicationContext().getResultMgr();
				AdminMgrI adminMgr = ApplicationContext.getApplicationContext()
						.getAdminMgr();
				ImageMgrI imageMgr = ApplicationContext.getApplicationContext()
						.getImageMgr();
				{
					if (action.equals("getAllUsers")) {
						JSONArray json = userMgr.getAllUserJson(request,
								response);
						response.getWriter().write(json.toString());
					} else if (action.equals(GET_USERS_AVAILABLE_ON_USERGROUP)) {
						JSONArray jsonArr = userMgr
								.getAvailableUsersOnGroupJson(request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_USERS_SELECTED_ON_USERGROUP)) {
						JSONArray jsonArr = userMgr
								.getSelectedUsersOnGroupJson(request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action
							.equals(GET_USERSGROUPS_AVAILABLE_ON_CAMPAIGN)) {
						JSONArray jsonArr = userGroupMgr
								.getAvailableOnCampaignJson(request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action
							.equals(GET_USERSGROUPS_SELECTED_ON_CAMPAIGN)) {
						JSONArray jsonArr = userGroupMgr
								.getSelectedOnCampaignJson(request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_SETS_AVAILABLE_ON_CAMPAIGN)) {
						JSONArray jsonArr = setMgr.getAvailableOnCampaignJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_SETS_SELECTED_ON_CAMPAIGN)) {
						JSONArray jsonArr = setMgr.getSelectedOnCampaignJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_QUESTIONS_AVAILABLE_ON_GAME)) {
						JSONArray jsonArr = questionMgr.getAvailableOnGameJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_QUESTIONS_SELECTED_ON_GAME)) {
						JSONArray jsonArr = questionMgr.getSelectedOnGameJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_GAMES_AVAILABLE_ON_SET)) {
						JSONArray jsonArr = gameMgr.getAvailableOnSetJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_GAMES_SELECTED_ON_SET)) {
						JSONArray jsonArr = gameMgr.getSelectedOnSetJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("addUser")) {

						JSONObject json = userMgr.addUser(request, response);
						response.getWriter().write(json.toString());

					} else if (action.equals("deleteUser")) {

						JSONArray jsonArr = userMgr.deleteBulk(request,
								response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("getAllProjects")) {
						JSONArray jsonArr = projectMgr.getAllProjectsJson();
						response.getWriter().write(jsonArr.toString());
					} else if (action.equals("addProject")) {

						JSONObject json = projectMgr.addProject(request,
								response);
						response.getWriter().write(json.toString());

					} else if (action.equals("deleteProject")) {

						JSONArray jsonArr = projectMgr.deleteBulk(request,
								response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("getAllSets")) {
						JSONArray jsonArr = setMgr.getAllSetJson(request,
								response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("addSet")) {
						JSONObject json = setMgr.addSet(request, response);
						response.getWriter().write(json.toString());

					} else if (action.equals("deleteSet")) {
						JSONArray jsonArr = setMgr
								.deleteBulk(request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("addGame")) {
						JSONObject json = gameMgr.addGames(request, response);
						response.getWriter().write(json.toString());
					} else if (action.equals("deleteGame")) {
						JSONArray jsonArr = gameMgr.deleteBulk(request,
								response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("deleteImage")) {
						JSONArray jsonArr = imageMgr.deleteBulk(request,
								response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("getAllGames")) {
						JSONArray jsonArr = gameMgr.getAllGameJson(request,
								response, true);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("getAllCampaigns")) {
						JSONArray jsonArr = campaignMgr.getAllCampaignsJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("addCampaign")) {
						JSONObject json = campaignMgr.addCampaign(request,
								response);
						response.getWriter().write(json.toString());

					} else if (action.equals("deleteCampaign")) {
						JSONArray jsonArr = campaignMgr.deleteBulk(request,
								response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("getAllUserGroups")) {
						JSONArray jsonArr = userGroupMgr.getAllUserGroupsJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("addUserGroup")) {
						JSONObject json = userGroupMgr.addUserGroup(request,
								response);
						response.getWriter().write(json.toString());

					} else if (action.equals("addCampaignUserGroup")) {
						JSONObject json = campaignMgr.saveCampaignUserGroups(
								request, response);
						response.getWriter().write(json.toString());

					} else if (action.equals("deleteUserGroup")) {
						JSONArray jsonArr = userGroupMgr.deleteBulk(request,
								response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_ALL_GAME_TEMPLATES_JSON)) {
						JSONArray jsonArr = gameTemplateMgr
								.getAllGameTemplateJson(request);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("getAllQuestions")) {
						JSONArray jsonArr = questionMgr.getAllQuestionsJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals("deleteQuestions")) {
						JSONArray jsonArr = questionMgr.deleteBulk(request,
								response);
						response.getWriter().write(jsonArr.toString());
					} else if (action.equals("addQuestions")) {
						JSONObject json = questionMgr.addQuestions(request,
								response);
						response.getWriter().write(json.toString());

					} else if (action.equals(GET_REGISRATION_URL)) {
						Project project = ApplicationContext
								.getApplicationContext()
								.getAdminWorkspaceProject(request);
						response.getWriter()
								.write(project.getRegistrationURL());
					}
					// -----------RESULTS ADVANCE SEARCH PARAMS JSONS STARTS
					// ----------------
					else if (action.equals(GET_ASSESSEMENTSETS_BY_CAMPAIGN)) {
						JSONArray jsonArr = setMgr.getSelectedOnCampaignJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_USERGROUPS_BY_CAMPAIGN)) {
						JSONArray jsonArr = userGroupMgr
								.getSelectedOnCampaignJson(request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_GAMES_BY_ASSESSMENTSET)) {
						JSONArray jsonArr = gameMgr.getSelectedOnSetJson(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(GET_RESULTS_FOR_GRID)) {
						JSONArray jsonArr = resultsMgr.getGamesJSONByCampaign(
								request, response);
						response.getWriter().write(jsonArr.toString());

					} else if (action.equals(ADD_GAME_FROM_IMPORT_QUESTIONS)) {
						JSONObject jsonArr = gameMgr.addGameFromImportQuestion(
								request, response);
						response.getWriter().write(jsonArr.toString());
					}
					// PROJECTS DROPDOWN CHANGE
					else if (action.equals(CHANGE_WORKSPACE_PROJECT)) {
						JSONObject json = adminMgr.changeWorkspaceProject(
								request, response);
						response.getWriter().write(json.toString());
					} else if (action.equals(ADD_USER_GROUP_FROM_CAMPAIGN)) {
						JSONObject json = userGroupMgr
								.addUserGroupFromCampaign(request, response);
						response.getWriter().write(json.toString());
					} else if (action
							.equals(GET_SELECTED_USERGROUPS_BY_CAMPAIGN)) {
						JSONArray json = userGroupMgr
								.getSelectedUsersOnCampaignJson(request,
										response);
						response.getWriter().write(json.toString());
					} else if (action.equals(GET_ALL_IMAGES)) {
						JSONArray json = imageMgr.getAllImagesJson(request,
								response);
						response.getWriter().write(json.toString());
					}
					// LOGOUT BUTTON HIT ACTION
					else if (action.equals(LOGOUT)) {
						adminMgr.logout(request, response);
						request.getRequestDispatcher(
								IConstants.admin_loginIndex).forward(request,
								response);
					} else if (action.equals("downloadFailedRows")) {
						ApplicationContext.getApplicationContext()
								.getAdminMgr()
								.downloadFailedRows(request, response);
					}

					// campaign UI methods
					else if (action.equals(SET_GAMES_ON_CAMPAIGN)) {
						campaignMgr.saveCampaignGames(request);
					}
				}

			}
		} catch (Exception e) {
			log.error("Error During action : " + action, e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// LOGIN METHOD
		String action = (String) request.getParameter("action");
		AdminMgrI adminMgr = ApplicationContext.getApplicationContext()
				.getAdminMgr();
		log.debug("Post Action on User Servlet called:" + action);
		if (action != null) {
			if (action.equals(SIGNUP)) {
				ApplicationContext.getApplicationContext().getAdminMgr()
						.signup(request, response);

			} else if (action.equals(LOGIN)) {
				ApplicationContext.getApplicationContext().getAdminMgr()
						.login(request, response);

			} else if (action.equals(CHANGEPASSWORD)) {
				ApplicationContext.getApplicationContext().getAdminMgr()
						.changePassword(request, response);

			} else if (action.equals(UPDATE_ACCOUNT)) {
				ApplicationContext.getApplicationContext().getAdminMgr()
						.updateAccount(request, response);

			} else if (action.equals(IMPORT_USERS)) {
				JSONObject json = adminMgr
						.importUsersFromXls(request, response);
				response.getWriter().write(json.toString());
			} else if (action.equals(IMPORT_QUESTIONS)) {
				JSONObject json = adminMgr.importQuestionsFromXls(request,
						response);
				response.getWriter().write(json.toString());
			} else if (action.equals(UPLOAD_IMAGE)) {
				ApplicationContext.getApplicationContext().getImageMgr()
						.uploadImage(request, response);
			}
		}
	}

}