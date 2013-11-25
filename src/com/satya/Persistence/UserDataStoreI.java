package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.User;

public interface UserDataStoreI {
	public void Save(User user);
	public void Delete(long userSeq);
	public List<User> findAll();
	public User findBySeq(long seq);
	public List<User> findByProjectSeq(long seq);
	public User findByUserNameAndProject(String userName, long projectseq);
	public User findByUserNamePassword(String userName, String password);
	public void changePassword(User user);
	
	public List<User> findSelectedByUserGroupSeq(long userGroupSeq);
	public List<User> findAvailableByUserGroupSeq(long userGroupSeq);
}