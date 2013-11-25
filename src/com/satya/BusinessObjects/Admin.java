package com.satya.BusinessObjects;

import java.util.Date;
import java.util.List;


public class Admin {
	private long seq;
	private String name;
	private boolean isSuperUser;
	private String email;
	private String mobile;
	private String city;
	private boolean isEnable;
	private Date createdOn;
	private String username;
	private String password;
	private Date lastModifiedDate;
	private List<Project> projects;

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
	public boolean isSuperUser() {
		return isSuperUser;
	}
	public void setIsSuperUser(boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean isEnable() {
		return isEnable;
	}
	public void setIsEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedon(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
}
