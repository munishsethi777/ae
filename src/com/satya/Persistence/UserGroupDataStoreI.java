package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.UserGroup;

public interface UserGroupDataStoreI {
	public void Save(UserGroup userGroup);
	public void Delete(long userGroupSeq);
	public List<UserGroup> findAll();
	public UserGroup findBySeq(long seq);
	public UserGroup findByNameAndProject(String name, long seq);
	public List<UserGroup> findByProjectSeq(long seq);
	
	public List<UserGroup> findAvailableForCampaign(long campaignSeq);
	public List<UserGroup> findSelectedForCampaign(long campaignSeq);
}
