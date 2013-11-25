package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.User;

public interface AdminDataStoreI {
	public void Save(Admin admin);
	public void Delete(long adminSeq);
	public List<Admin> findAll();
	public Admin findBySeq(long seq);
	public Admin findByUserName(String userName);
	public Admin findByUserNamePassword(String userName, String password);
}
