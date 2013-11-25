package com.satya.BusinessObjects;

import java.util.Date;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.Utils.SecurityUtil;

public class Project {
	private long seq;
	private Admin admin;
	private String name;
	private String description;
	private String email;
	private String phone;
	private String mobile;
	private String contactPerson;
	private String address;
	private String city;
	private String country;
	private Date createdOn;
	private Date lastModifiedDate;
	private boolean isEnable;

	public Project(){
		
	}
	public Project(long seq){
		this.setSeq(seq);
	}
	
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
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
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getRegistrationURL(){
		if(this.getSeq() == 0){
			return null;
		}
		String url = IConstants.REGISTRATION_URL;
		url = url + "?token=" + ApplicationContext.getApplicationContext().getSecurityUtil().getEncryptedString(String.valueOf(this.getSeq()));
		return url;
	}
	
}
