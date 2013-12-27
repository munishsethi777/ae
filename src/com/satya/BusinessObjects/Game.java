package com.satya.BusinessObjects;

import java.util.Date;
import java.util.List;

public class Game {

	private long seq;
	private String title;
	private String description;
	private GameTemplates gameTemplate;
	private Project project;
	private boolean isEnable;
	private Date createdOn;
	private Date lastModifiedDate;
	private List<Questions> questions;
	private Result gameResult; //for showing played games in Set
	private int maxSecondsAllowed;
	private boolean isPublished;
	private int maxQuestions;
	
	public int getMaxSecondsAllowed() {
		return maxSecondsAllowed;
	}
	public void setMaxSecondsAllowed(int maxSecondsAllowed) {
		this.maxSecondsAllowed = maxSecondsAllowed;
	}
	public Result getGameResult() {
		return gameResult;
	}
	public void setGameResult(Result gameResult) {
		this.gameResult = gameResult;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	public GameTemplates getGameTemplate() {
		return gameTemplate;
	}
	public void setGameTemplate(GameTemplates gameTemplate) {
		this.gameTemplate = gameTemplate;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public List<Questions> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Questions> questions) {
		this.questions = questions;
	}
	/*	
	 * Property meant for XML streami
	 */
	public QuestionsList getQuestionsListObj(){
		QuestionsList questList = new QuestionsList();
		questList.setMaxSecondsAllowed(this.getMaxSecondsAllowed());
		questList.setQuestionsList(this.getQuestions());
		return questList;
	}
	public String getQuestionsXMLPath(){
		String str = "questionXmls\\"+ this.getSeq();
		return str;
	}
	public boolean isPublished() {
		return isPublished;
	}
	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}
	public int getMaxQuestions() {
		return maxQuestions;
	}
	public void setMaxQuestions(int maxQuestions) {
		this.maxQuestions = maxQuestions;
	}
}
