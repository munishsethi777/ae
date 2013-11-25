package com.satya;

import javax.servlet.http.HttpServletRequest;

import validator.AEValidatorFactory;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;
import com.satya.Managers.AdminMgrI;
import com.satya.Managers.ApplicationMgr;
import com.satya.Managers.CampaignMgrI;
import com.satya.Managers.EmailTemplateMgrI;
import com.satya.Managers.GameMgrI;
import com.satya.Managers.GameTemplatesMgrI;
import com.satya.Managers.ProjectMgrI;
import com.satya.Managers.QuestionAnswersMgrI;
import com.satya.Managers.QuestionsMgrI;
import com.satya.Managers.ResultsMgrI;
import com.satya.Managers.SetMgrI;
import com.satya.Managers.UserGroupMgrI;
import com.satya.Managers.UserMgrI;
import com.satya.Persistence.DataStoreMgr;
import com.satya.Utils.SecurityUtil;
import com.satya.mail.MailerI;



public class ApplicationContext {

	private static ApplicationContext applicationContext;
	private static DataStoreMgr dataStoreMgr;
	private UserMgrI userMgr;
	private ProjectMgrI projectMgr;
	private SetMgrI setMgr;
	private GameMgrI gamesMgr; 
	private CampaignMgrI campaignMgr;
	private UserGroupMgrI userGroupMgr;
	private AdminMgrI adminMgr;
	private ApplicationMgr applicationMgr;
	private QuestionsMgrI questionMgr;
	private QuestionAnswersMgrI questionAnswerMgr;
	private GameTemplatesMgrI gameTemplateMgr;
	private ResultsMgrI resultMgr;
	private EmailTemplateMgrI emailTemplateMgr;
	private MailerI mailerMgr;
	private SecurityUtil securityUtil;
	private AEObjectsMetaRegistry metaRegistry;
	private AEValidatorFactory validatorFactory;
	
	public SecurityUtil getSecurityUtil() {
		return securityUtil;
	}

	public void setSecurityUtil(SecurityUtil securityUtil) {
		this.securityUtil = securityUtil;
	}

	public static ApplicationContext getApplicationContext(){
    	if(applicationContext == null){
    		throw new RuntimeException("No Application Context was initialized");
        }else{
        	return applicationContext;
        }        
    }
	
	public static void setApplicationContext(ApplicationContext ac){
		applicationContext = ac;
	}

	public DataStoreMgr getDataStoreMgr() {
		return dataStoreMgr;
	}

	public void setDataStoreMgr(DataStoreMgr dataStoreMgr) {
		ApplicationContext.dataStoreMgr = dataStoreMgr;
	}
	
	public User getLoggedinUser(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute(IConstants.loggedInUser);
		return user;
	}
	public Admin getLoggedinAdmin(HttpServletRequest request){
		Admin admin = (Admin) request.getSession().getAttribute(IConstants.loggedInAdmin);
		return admin;
	}
	public Project getAdminWorkspaceProject(HttpServletRequest request){
		Project project = (Project) request.getSession().getAttribute(IConstants.adminWorkspaceProject);
		return project;
	}

	public UserMgrI getUserMgr() {
		return userMgr;
	}
	public  void setUserMgr(UserMgrI userMgr) {
		this.userMgr = userMgr;	
		
	}
	
	public ProjectMgrI getProjectMgr() {
		return projectMgr;
	}
	public void setProjectMgr(ProjectMgrI projectMgr) {
		this.projectMgr = projectMgr;	
	}
	
	public SetMgrI getSetMgr() {
		return setMgr;
	}
	public void setSetMgr(SetMgrI setMgr) {
		this.setMgr = setMgr;	
	}

	public GameMgrI getGamesMgr() {
		return gamesMgr;
	}

	public void setGameMgr(GameMgrI gamesMgr) {
		this.gamesMgr = gamesMgr;
	}
	
	
	public CampaignMgrI getCampaiMgr() {
		return campaignMgr;
	}

	public void setCampaignMgr(CampaignMgrI campaignMgr) {
		this.campaignMgr = campaignMgr;
	}

	public UserGroupMgrI getUserGroupMgr() {
		return userGroupMgr;
	}

	public void setUserGroupMgr(UserGroupMgrI userGroupMgr) {
		this.userGroupMgr = userGroupMgr;
	}

	public AdminMgrI getAdminMgr() {
		return adminMgr;
	}

	public void setAdminMgr(AdminMgrI adminMgr) {
		this.adminMgr = adminMgr;
	}

	public ApplicationMgr getApplicationMgr() {
		return applicationMgr;
	}

	public void setApplicationMgr(ApplicationMgr applicationMgr) {
		this.applicationMgr = applicationMgr;
	}
	
	public void setQuestionMgr(QuestionsMgrI questionMgr){
		this.questionMgr = questionMgr;
	}
    
	public QuestionsMgrI getQuestionMgr(){
		return this.questionMgr;
	}
	
	public void setQuestionAnswersMgr(QuestionAnswersMgrI questionAnswersMgr){
		this.questionAnswerMgr = questionAnswersMgr;
	}
	public QuestionAnswersMgrI getQuestionAnswersMgr(){
		return this.questionAnswerMgr;
	}
	public void setGameTemplateMgr(GameTemplatesMgrI gamTemplateMgr){
		this.gameTemplateMgr = gamTemplateMgr;
	}
	public GameTemplatesMgrI getGameTemplateMgr(){
		return gameTemplateMgr;
	}
	public ResultsMgrI getResultMgr() {
		return resultMgr;
	}

	public void setResultMgr(ResultsMgrI resultMgr) {
		this.resultMgr = resultMgr;
	}
	
	public EmailTemplateMgrI getEmailTemplateMgr() {
		return emailTemplateMgr;
	}

	public void setEmailTemplateMgr(EmailTemplateMgrI emailTemplateMgr) {
		this.emailTemplateMgr = emailTemplateMgr;
	}
	
	public MailerI getMailer() {
		return mailerMgr;
	}

	public void setMailer(MailerI mailer) {
		this.mailerMgr = mailer;
	}

	public AEObjectsMetaRegistry getMetaRegistry() {
		return metaRegistry;
	}

	public void setMetaRegistry(AEObjectsMetaRegistry metaRegistry) {
		this.metaRegistry = metaRegistry;
	}

	public AEValidatorFactory getValidatorFactory() {
		return validatorFactory;
	}

	public void setValidatorFactory(AEValidatorFactory validatorFactory) {
		this.validatorFactory = validatorFactory;
	}
	
}
