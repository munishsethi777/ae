package com.satya.BusinessObjects;

import java.util.Date;
import java.util.List;

import com.satya.xmlObjects.Category;
import com.satya.xmlObjects.Success;

public class Questions {

	private long seq;
	private String title;
	private String description;
	private Integer points;
	private Project project;
	private Date createdOn;
	private Date lastmodified;
	private Boolean isEnabled;
	private long answerId;
	private int negativePoints;
	private int maxSecondsAllowed;
	private int extraAttemptsAllowed;
	private String hint;
	private long imageSeq;
	private Category category;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Success getSuccess() {
		return success;
	}

	public void setSuccess(Success success) {
		this.success = success;
	}

	private Success success;

	public static final String IMAGE_SEQ = "imageSeq";

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	private List<QuestionAnswers> questionAnswers;

	public long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(long answerId) {
		this.answerId = answerId;
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

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdon) {
		this.createdOn = createdon;
	}

	public Date getLastModified() {
		return lastmodified;
	}

	public void setLastModified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Boolean IsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isenabled) {
		this.isEnabled = isenabled;
	}

	public List<QuestionAnswers> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(List<QuestionAnswers> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	public int getNegativePoints() {
		return negativePoints;
	}

	public void setNegativePoints(int negativePoints) {
		this.negativePoints = negativePoints;
	}

	public int getMaxSecondsAllowed() {
		return maxSecondsAllowed;
	}

	public void setMaxSecondsAllowed(int maxSecondsAllowed) {
		this.maxSecondsAllowed = maxSecondsAllowed;
	}

	public int getExtraAttemptsAllowed() {
		return extraAttemptsAllowed;
	}

	public void setExtraAttemptsAllowed(int extraAttemptsAllowed) {
		this.extraAttemptsAllowed = extraAttemptsAllowed;
	}

	public long getImageSeq() {
		return this.imageSeq;
	}

	public void setImageSeq(long imageSeq) {
		this.imageSeq = imageSeq;
	}

	private String introduction;

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
