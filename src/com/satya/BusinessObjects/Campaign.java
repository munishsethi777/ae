package com.satya.BusinessObjects;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.IConstants;
import com.satya.Utils.DateUtils;


	
public class Campaign {
	private long seq;
	private Project project;
	private String name;
	private String description;
	private Date  createdOn;
	private String validityDays;
	private boolean isEnabled;
	private List<Set> sets;
	private List<Game> games;

	private List<UserGroup>userGroups;	
	private Date lastModifiedDate;
	private Date startDate;
	private Date validTillDate;
	private String launchMessage;
	private boolean isPublished;
	
	public static final String LAUNCH_MESSAGE = "launchMessage";
	public static final String START_DATE = "startDate";
	public static final String VALID_TILL_DATE = "validTillDate";
	
	public Campaign(){
		
	}
	public Campaign(long seq){
		this.setSeq(seq);
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getValidTillDate() {
		return validTillDate;
	}
	public void setValidTillDate(Date validTillDate) {
		this.validTillDate = validTillDate;
	}	
	
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getValidityDays() {
		return validityDays;
	}
	public void setValidityDays(String validityDays) {
		this.validityDays = validityDays;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public List<Set> getSets() {
		return sets;
	}
	public void setSets(List<Set> sets) {
		this.sets = sets;
	}
	public List<UserGroup> getUserGroups() {
		return userGroups;
	}
	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public String getLaunchMessage(){
		return this.launchMessage;
	}
	
	public void setLaunchMessage(String launchMessage){
		this.launchMessage=launchMessage;
	}
	
	public static JSONArray toJsonArray(List<Campaign> campaigns){
		JSONArray jsonArr = new JSONArray();
		for(Campaign campaign: campaigns){ 
			jsonArr.put(Campaign.toJson(campaign));
		}
		return jsonArr;
	}
	public static JSONObject toJson(Campaign campaign){		
		JSONObject json = new JSONObject();
		try{
			json.put(IConstants.CAMP_SEQ, campaign.getSeq());
			json.put(IConstants.NAME, campaign.getName());
			json.put(IConstants.DESCRIPTION, campaign.getDescription());
			json.put(IConstants.VALIDITY_DAYS, campaign.getValidityDays());
			json.put(IConstants.IS_ENABLED, campaign.isEnabled());
			json.put(IConstants.CREATED_ON,  DateUtils.getGridDateFormat(campaign.getCreatedOn()));
			json.put(IConstants.LAST_MODIFIED_DATE,  DateUtils.getGridDateFormat(campaign.getLastModifiedDate()));
			json.put(START_DATE,  DateUtils.getGridDateFormat(campaign.getStartDate()));
			json.put(VALID_TILL_DATE,  DateUtils.getGridDateFormat(campaign.getValidTillDate()));
			json.put(LAUNCH_MESSAGE, campaign.getLaunchMessage());
		}catch( Exception e){
			
		}
		return json;
	}
	public boolean isPublished() {
		return isPublished;
	}
	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}
}
