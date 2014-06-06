package com.satya;

public  class IConstants {
	//Web pages
	public final static String admin_loginIndex = "index.jsp";
	public final static String admin_DashboardPage = "dashboard.jsp";
	public final static String admin_SignupPage = "signup.jsp";
	public final static String admin_MYAccountPage = "myAccount.jsp";
	
	public final static String user_ThanksRegistrationPage = "thanks.jsp";
	public final static String user_loginIndex = "userLogin.jsp";
	public final static String user_DashboardPage = "userDashboard.jsp";
	
	//session variable names
	public final static String loggedInAdmin = "loggedInAdmin";
	public final static String loggedInUser = "loggedInUser";
	public final static String adminWorkspaceProject = "adminWorkspaceProject";
	
	//json return strings
	public final static String errMessages = "errMessages";
	public final static String sccMessages = "sccMessages";

	public static final String SUCCESS = "success";
	public final static String FAILURE = "failure";
	
	public final static String err_invalidUsernamePassword = "Invalid Username or Password";
	public final static String err_TitleRequired = "Title is required. Please enter valid Title.";
	public final static String err_pleaseLogin = "Invalid Execution! Please login.";
	public final static String err_invalidManagerExecution = "Invalid Execution! Only Manager can view this webpage.";

	public final static String msg_DeletedSuccessfully = "Deleted Successfully.";
	public final static String LAST_MODIFIED = "lastModified";
	public final static String SOURCE_URL = "SourceURL";
	public final static String IS_ENABLED = "isEnabled";
	public static final String VALIDITY_DAYS = "validityDays";
	public static final String DESCRIPTION = "description";
	public static final String SAVED_SUCCESSFULLY = "Saved Successfully";
	public static final String MESSAGE = "message";
	public static final String STATUS = "status";
	public static final String ERROR = "Error: ";
	public static final String NAME = "name";
	public static final String SEQ = "seq";
	public static final String CAMP_SEQ = "campSeq";
	
	public static final String CREATED_ON = "createdOn";
	public final static String LAST_MODIFIED_DATE = "lastmodifieddate";
	public final static String TITLE = "title";
	public final static String GAME_TITLE = "gameTitle";
	public final static String GAME_DESCRIPTION ="gameDescription";
	public final static String SELECTED_CHILDREN_ROWS = "selectedChildrenRows";
	public final static String REGISTRATION_URL = "http://localhost:8080/AE/register.jsp";
	public final static String APPLICATION_URL = "http://localhost:8080/AE/";
	public final static String APPLICATION_FILE_PATH = "G:\\Webdocs\\AssessmentEngine\\WebContent\\questionXmls";
}
