package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.Questions;
import com.satya.BusinessObjects.Set;
import com.satya.BusinessObjects.UserGroup;

public interface GameDataStoreI {
	public void Save(Game game);
	public List<Game>FindAll(boolean isEnable);
	public List<Game>FindByProject(long projectSeq);
	public void Delete(long gameSeq);
	public Game findBySeq(long seq);
	public List<Game> findBySeqs(boolean isEnable,Long[] gameSeqs);
	public Game findBySeqWithQuesAnswers(long seq);
	
	public List<Game> findAvailableForSet(long setSeq);
	public List<Game> findSelectedForSet(long setSeq);
	
	public boolean isGameByCampaignSetGameUser(long campaignSeq, long setSeq, long gameSeq, long userSeq);
	public boolean isGameByCampaignGameUser(long campaignSeq, long gameSeq, long userSeq);
	public List<Game> findSelectedForCampaign(long campaignSeq);
	public void saveGameQuestions(Game game,boolean isDeleteEarlierQuestionRelations);
	
	public void deleteGameQuestion(long gameSeq, long questionSeq);
}
