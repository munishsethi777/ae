package com.satya.BusinessObjects;

import com.satya.IConstants;

public class GameTemplates {
	private long seq;
	private String name;
	private String path;
	private String imagePath;
	private String description;
	private int maxQuestions;
	
	
	public GameTemplates(){
		
	}
	public GameTemplates(long seq){
		this.seq = seq;
	}
	public GameTemplates(String name){
		this.name = name;
	}
	
	
	
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getImagePath() {
		String appURL = IConstants.APPLICATION_URL;
		return appURL + imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMaxQuestions() {
		return maxQuestions;
	}
	public void setMaxQuestions(int maxQuestions) {
		this.maxQuestions = maxQuestions;
	}
	
	
	
}
