package com.satya.mail;

import java.util.StringTokenizer;

public enum TemplateKeyword {	
	USER_NAME("User Name","user.username"),
	APPLICATION_TITLE("Application Title","applicationTitle"),
	ADMIN_USERNAME("Admin Name","admin.username"),
	PROJECT_NAME("Project Name","user.project.name"),
	CAMPAIGN_NAME("Campaign Name", "campaign.name"),
	CAMPAIGN_START_DATE("Start Date","campaign.startDate"),
	CAMPAIGN_VALID_TILL_DATE("Valid Till Date","campaign.validTillDate");
	
	private String name;
	private String mapping;
	private boolean isLink;
	
	private TemplateKeyword(String name,String mapping){
		this.name = name;
		this.mapping = mapping;
	}
	private TemplateKeyword(String name,String mapping, boolean isLink){
		this.name = name;
		this.mapping = mapping;
		this.isLink = isLink;
	}
	
	public String getName(){
		return name;
	}
	
	public String getMapping(){
		return mapping;
	}
	
	public String getKey(){
		return this.toString();
	}
	
	public String[] getMappingTokens(){
		StringTokenizer st = new StringTokenizer(mapping,".");
		int tokenCount = st.countTokens();
		String[] tokens = new String[tokenCount];
		int counter = 0;
		while(st.hasMoreTokens()){
			tokens[counter++] = st.nextToken();
		}
		return tokens;
	}

	public boolean isLink() {
		return isLink;
	}

	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
}
