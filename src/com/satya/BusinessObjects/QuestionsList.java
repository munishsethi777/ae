package com.satya.BusinessObjects;

import java.util.List;

/*
 * This object is mainly created for XML generation purposes
 * As we need to set maxSecsAllowed on this questions list tag so we have this now
 * or we had a simple list first.
 */
public class QuestionsList {
	private List<Questions> questionsList;
	private int maxSecondsAllowed;
	
	public List<Questions> getQuestionsList() {
		return questionsList;
	}
	public void setQuestionsList(List<Questions> questionsList) {
		this.questionsList = questionsList;
	}
	public int getMaxSecondsAllowed() {
		return maxSecondsAllowed;
	}
	public void setMaxSecondsAllowed(int maxSecondsAllowed) {
		this.maxSecondsAllowed = maxSecondsAllowed;
	}

	
	
}
