package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.Set;
import com.satya.BusinessObjects.UserGroup;


public interface SetDataStoreI {
	//Admin Methods
	public void Save(Set set);
	public void Delete(long setSeq);
	public List<Set> findAll();
	public List<Set> findByProjectSeq(long seq);
	public Set findBySeq(long seq);
	public void DeleteGamesFromSet(long setSeq);
	
	public List<Set> findAvailableForCampaign(long campaignSeq);
	public List<Set> findSelectedForCampaign(long campaignSeq);
	
	//Public methods
	public List<Set> findByCampaign(long campaignSeq);
}
