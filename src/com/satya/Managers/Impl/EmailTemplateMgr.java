package com.satya.Managers.Impl;

import org.apache.log4j.Logger;

import com.satya.Managers.EmailTemplateMgrI;
import com.satya.Utils.FileUtils;
import com.satya.mail.EmailTemplate;
import com.satya.mail.MailTemplateType;

public class EmailTemplateMgr implements EmailTemplateMgrI {
	Logger logger = Logger.getLogger(EmailTemplateMgr.class);
	
	public EmailTemplate getEmailTemplate(MailTemplateType templateType){
		EmailTemplate emailTemplate = new EmailTemplate();
		emailTemplate.setTemplateKeyword(templateType.getKeywords());
		emailTemplate.setTemplateName(templateType.getName());
		try{
			FileUtils fileUtil = new FileUtils();
			String templatetext = 
				fileUtil.readFile(templateType.getFileName());
			emailTemplate.setTemplateText(templatetext);
		}catch(Exception e){
			
		}
		return emailTemplate;
	}
}