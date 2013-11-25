package com.satya.mail;

import java.util.List;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.User;

public interface MailerI {

	/**
	 * Asynchronously sends Messages using JavaMail API.
	 * 
	 * @param messages
	 */
	public abstract void sendMessages(List<MailMessage> messages);

	public abstract void sendMessage(MailMessage mailMsg);

	public void sendUserSignUpNotification(User user);

	public void sendAdminSignUpNotification(Admin admin);

	public void sendCampaignAlertNotification(User user);

	public MailTemplateFactory getTemplateFactory();

	public void start();
}