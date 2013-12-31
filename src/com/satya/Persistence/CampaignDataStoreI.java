package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.Campaign;

public interface CampaignDataStoreI {
	public void Save(Campaign campaign);
	public void Delete(long campaignSeq);
	public List<Campaign> findAll();
	public Campaign findBySeq(long seq);
	public List<Campaign> findByProjectSeq(long seq);
	public void publishCampaign(long seq);
	//User Methods
	public List<Campaign> findByUserSeq(long seq);
	public int getCampaignSetSeq(long campaignSeq, long setSeq);
	public void saveGames(Campaign campaign, boolean isDeleteEarlierFirst);
	public void saveUserGroups(Campaign campaign);
}
