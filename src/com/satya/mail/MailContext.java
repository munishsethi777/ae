package com.satya.mail;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.User;



public class MailContext {
	private Campaign campaign;
	private Admin admin;
	private User user;
	private String applicationTitle;
	
	
	public Campaign getCampaign() {
		return campaign;
	}
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getApplicationTitle(){
		String applicationTitle = "Assessment Engine";
		return applicationTitle;
	}
	
	
	
}
