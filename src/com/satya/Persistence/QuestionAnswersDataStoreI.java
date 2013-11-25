package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.QuestionAnswers;


public interface QuestionAnswersDataStoreI {
	public void Save(QuestionAnswers questionAnswer);
	public void Delete(long QuesAnsSeq);
	public void DeleteByQuestionseq(long QuesSeq);
	public List<QuestionAnswers>findAll();
	public QuestionAnswers findBySeq(long seq);
	public List<QuestionAnswers> findByQuestionSeq(long questionseq);
	
}
