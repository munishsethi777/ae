package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.Result;

public interface ResultsDataStoreI {
	
	public void Save(Result result);
	public List<Result> findByCampaign(long campaignSeq);
	public Result findByCampaignAndGameAndUser(long campaignSeq, long gameSeq, long userSeq);
	
}
