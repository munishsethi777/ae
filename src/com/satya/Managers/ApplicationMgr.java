package com.satya.Managers;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import validator.AEValidatorFactory;

import com.satya.AEObjectsMetaRegistry;
import com.satya.ApplicationContext;
import com.satya.Managers.Impl.AdminMgr;
import com.satya.Managers.Impl.CampaignMgr;
import com.satya.Managers.Impl.EmailTemplateMgr;
import com.satya.Managers.Impl.GameMgr;
import com.satya.Managers.Impl.GameTemplateMgr;
import com.satya.Managers.Impl.ImageMgr;
import com.satya.Managers.Impl.ProjectMgr;
import com.satya.Managers.Impl.QuestionAnswersMgr;
import com.satya.Managers.Impl.QuestionsMgr;
import com.satya.Managers.Impl.ResultsMgr;
import com.satya.Managers.Impl.SetMgr;
import com.satya.Managers.Impl.UserGroupMgr;
import com.satya.Managers.Impl.UserMgr;
import com.satya.Persistence.DataStoreMgr;
import com.satya.Persistence.Impl.JDBCDataStoreMgr;
import com.satya.Persistence.Impl.PersistenceMgr;
import com.satya.Utils.SecurityUtil;
import com.satya.mail.MailTemplateFactory;
import com.satya.mail.Mailer;
import com.satya.mail.MailerI;

public class ApplicationMgr {

	Logger log = Logger.getLogger(ApplicationMgr.class);
	public static final String userName = "munish@satyainfopages.com";
	public static final String password = "hanuman";
	public static final int port = 995;
	public static final String smtpServer = "mail.satyainfopages.com";
	public static final boolean useSsl = false;

	public void ContextLoader(ServletContext ctx,
			ApplicationContext applicationContext) {

		log.info("Reloading Context Servlet Called....");
		String jdbcUrl = ctx.getInitParameter("jdbcURL");
		String user = ctx.getInitParameter("dbuser");
		String pass = ctx.getInitParameter("dbpassword");

		PersistenceMgr psmgr = new PersistenceMgr(jdbcUrl, user, pass);
		DataStoreMgr dataStoreMgr = new JDBCDataStoreMgr(psmgr);
		applicationContext.setDataStoreMgr(dataStoreMgr);

		UserMgrI userMgr = new UserMgr();
		applicationContext.setUserMgr(userMgr);

		ProjectMgrI projectMgr = new ProjectMgr();
		applicationContext.setProjectMgr(projectMgr);

		SetMgrI setMgr = new SetMgr();
		applicationContext.setSetMgr(setMgr);

		GameMgrI gameMgr = new GameMgr();
		applicationContext.setGameMgr(gameMgr);

		CampaignMgrI campaignMgr = new CampaignMgr();
		applicationContext.setCampaignMgr(campaignMgr);

		UserGroupMgrI userGroupMgr = new UserGroupMgr();
		applicationContext.setUserGroupMgr(userGroupMgr);

		AdminMgrI adminMgr = new AdminMgr();
		applicationContext.setAdminMgr(adminMgr);

		QuestionsMgrI questionMgr = new QuestionsMgr();
		applicationContext.setQuestionMgr(questionMgr);

		QuestionAnswersMgrI questionAnswersMgr = new QuestionAnswersMgr();
		applicationContext.setQuestionAnswersMgr(questionAnswersMgr);

		GameTemplatesMgrI gameTemplateMgr = new GameTemplateMgr();
		applicationContext.setGameTemplateMgr(gameTemplateMgr);

		ResultsMgrI resultMgr = new ResultsMgr();
		applicationContext.setResultMgr(resultMgr);
		EmailTemplateMgrI emailTemplateMgr = new EmailTemplateMgr();
		applicationContext.setEmailTemplateMgr(emailTemplateMgr);

		SecurityUtil securityUtil = new SecurityUtil();
		securityUtil.init();
		applicationContext.setSecurityUtil(securityUtil);

		AEObjectsMetaRegistry metaRegistry = new AEObjectsMetaRegistry();
		applicationContext.setMetaRegistry(metaRegistry);

		AEValidatorFactory validatorFactory = new AEValidatorFactory();
		applicationContext.setValidatorFactory(validatorFactory);

		ImageMgrI imageMgr = new ImageMgr();
		applicationContext.setImageMgr(imageMgr);

		MailerI mailer = new Mailer(new MailTemplateFactory(), userName,
				password, port, smtpServer, useSsl);
		applicationContext.setMailer(mailer);
		ApplicationContext.setApplicationContext(applicationContext);
		// ApplicationContext.getApplicationContext().getMailer().start();
		log.info("Context Reloaded");
	}
}
