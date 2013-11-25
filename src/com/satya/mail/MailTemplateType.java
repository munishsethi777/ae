package com.satya.mail;


public enum MailTemplateType {
	
	USER_SIGNUP_NOTIFICATION("USER_SIGNUP_NOTIFICATION",
			"userSignup.html",
			new TemplateKeyword[]{TemplateKeyword.USER_NAME,
								  TemplateKeyword.APPLICATION_TITLE,
								  TemplateKeyword.ADMIN_USERNAME}),
	ADMIN_SIGNUP_NOTIFICATION("ADMIN_SIGNUP_NOTIFICATION",
			"adminSignup.html",
			new TemplateKeyword[]{TemplateKeyword.ADMIN_USERNAME,
								  TemplateKeyword.APPLICATION_TITLE}),
	CAMPAIGN_ALERT_NOTIFICATION("CAMPAIGN_ALERT_NOTIFICATION",
			"campaignAlert.html",
			new TemplateKeyword[]{TemplateKeyword.ADMIN_USERNAME,
								  TemplateKeyword.APPLICATION_TITLE,
								  TemplateKeyword.CAMPAIGN_NAME,
								  TemplateKeyword.CAMPAIGN_START_DATE,
								  TemplateKeyword.CAMPAIGN_VALID_TILL_DATE});
	

	
	private String name;
	private TemplateKeyword[] keywords;
	private String fileName;
	
	MailTemplateType(String name,String fileName,TemplateKeyword[] macros){
		this.name = name;
		this.fileName = fileName;
		this.keywords = macros;
	}
	
	public String getName(){
		return name;
	}
	public String getFileName(){
		return fileName;
	}
	public TemplateKeyword[] getKeywords(){
		return keywords;
	}
}
