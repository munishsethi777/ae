package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.Questions;

public interface QuestionDataStoreI {
	public void Save(Questions questions);

	public void Delete(long QuesSeq);

	public List<Questions> findAll();

	public Questions findBySeq(long seq);

	public List<Questions> findByProjectSeq(long seq);

	public List<Questions> findByGameSeq(long gameSeq);

	public List<Questions> findSelectedByGameSeq(long gameSeq);

	public List<Questions> findAvailableByGameSeq(long gameSeq);
	
	public int countQuestionsOnGame(long gameSeq);
}
