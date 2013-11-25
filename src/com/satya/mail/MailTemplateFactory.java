/*
 * Created on Aug 25, 2013
 */
package com.satya.mail;

import com.satya.ApplicationContext;
import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.User;
import com.satya.Managers.EmailTemplateMgrI;

/**
 * @author BaljeetGaheer
 * 
 */
public class MailTemplateFactory {

	private String emailDesignerTemplateHTML = new String();
	private String PHRASE = "<span class='style6'>Please do not reply to this email.</span><br /><br /><br />";

	public MailMessage getMailMessage(MailTemplateType templateType,
			MailContext mailContext) {
		EmailTemplateMgrI templateMgr = ApplicationContext
				.getApplicationContext().getEmailTemplateMgr();
		EmailTemplate template = templateMgr.getEmailTemplate(templateType);
		MailMessage mailMessage = template.getMailMessage(mailContext);
		return mailMessage;
	}

	public MailMessage getUserSignUpEmail(MailContext mailContext) {
		User user = mailContext.getUser();
		MailMessage mailMessage = getMailMessage(
				MailTemplateType.USER_SIGNUP_NOTIFICATION, mailContext);
		mailMessage.setSubject("WelCome To AssessmentEngine");
		mailMessage.setReceiver(user.getEmail());
		return mailMessage;
	}

	public MailMessage getAdminSignUpEmail(MailContext mailContext) {
		Admin admin = mailContext.getAdmin();
		MailMessage mailMessage = getMailMessage(
				MailTemplateType.ADMIN_SIGNUP_NOTIFICATION, mailContext);
		mailMessage.setSubject("WelCome To AssessmentEngine");
		mailMessage.setReceiver(admin.getEmail());
		return mailMessage;
	}

	public MailMessage getCampaignAlertEmail(MailContext mailContext) {
		User user = mailContext.getUser();
		MailMessage mailMessage = getMailMessage(
				MailTemplateType.CAMPAIGN_ALERT_NOTIFICATION, mailContext);
		mailMessage.setSubject("Campaign Alert From AssessmentEngine");
		mailMessage.setReceiver(user.getEmail());
		return mailMessage;
	}

}
