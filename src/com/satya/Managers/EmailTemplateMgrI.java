package com.satya.Managers;

import com.satya.mail.EmailTemplate;
import com.satya.mail.MailTemplateType;

public interface EmailTemplateMgrI {
	public EmailTemplate getEmailTemplate(MailTemplateType templateType);
}
