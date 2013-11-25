package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.Persistence.QuestionAnswersDataStoreI;
import com.satya.Persistence.RowMapper;

public class QuestionAnswersDataStore implements QuestionAnswersDataStoreI,RowMapper {
	Logger logger = Logger.getLogger(QuestionAnswersDataStore.class);
	private final static String SELECT = "select * from questionanswers ";
	private final static String SELECT_BY_SEQ = SELECT + "where seq = ?";
	private final static String SELECT_BY_QUESTIONS_SEQ = SELECT + "where questionseq = ?";
	
	private final static String SAVE = "insert into questionanswers(questionseq, title, iscorrect) " +
			"values (?,?,?)";
	private final static String UPDATE = "update questionanswers set questionseq=?, title=?, iscorrect=?, where seq=?";
	private final static String DELETE = "delete from questionanswers where seq = ?";
	private final static String DELETE_BY_QUESTION= "delete from questionanswers where questionseq = ?";
	private PersistenceMgr persistenceMgr;
	
	public QuestionAnswersDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	@Override
	public void Save(QuestionAnswers questionAnswers){
		String SQL = questionAnswers.getSeq() != 0 ? UPDATE : SAVE;
		try{
			long questionSeq = 0;
			if(questionAnswers.getQuestion()!= null){
				questionSeq = questionAnswers.getQuestion().getSeq();
			}
			Object[] params  = new Object[questionAnswers.getSeq() != 0 ? 4 :3];
			
					params[0]= questionSeq;
					params[1] = questionAnswers.getAnswerTitle();
					params[2] = questionAnswers.isCorrect();			
					if(questionAnswers.getSeq() != 0){
						params[3] =  questionAnswers.getSeq();
					}
			persistenceMgr.excecuteUpdate(SQL, params);
			if(questionAnswers.getSeq() == 0){
				questionAnswers.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
		}catch(Exception e){
			logger.error("Error during Save QuestionAnswers",e);
		}
	}
	public void Delete(long QuesAnsSeq){
		Object [] params = new Object [] {QuesAnsSeq};
		persistenceMgr.excecuteUpdate(DELETE, params);	
	}
	public List<QuestionAnswers>findAll(){
		return (List<QuestionAnswers>)persistenceMgr.executePSQuery(SELECT, null, this);
	}
	public QuestionAnswers findBySeq(long seq){
		Object [] params = new Object [] {seq};
		return (QuestionAnswers) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ, params, this);
	}
	public List<QuestionAnswers> findByQuestionSeq(long questionseq){
		Object [] params = new Object [] {questionseq};
		return (List<QuestionAnswers>)persistenceMgr.executePSQuery(SELECT, params, this);
	}
	public void DeleteByQuestionseq(long QuesSeq){
		Object [] params = new Object [] {QuesSeq};
		persistenceMgr.excecuteUpdate(DELETE_BY_QUESTION, params);
	}
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	

	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		QuestionAnswers questionAnswers  = null;
		try{			
			long seq = rs.getLong("seq");
			String title = rs.getString("title");
			boolean  isCorrect = rs.getBoolean("iscorrect");			
			long questionseq = rs.getLong("questionseq");		
			questionAnswers = new QuestionAnswers();
			questionAnswers.setSeq(seq);
			questionAnswers.setCorrect(isCorrect);
			questionAnswers.setAnswerTitle(title);
			
			Questions questions = null;
			if(questionseq != 0 ){
				
			}
			questionAnswers.setQuestion(questions);
		}catch(Exception e){
			logger.error("QuestionsAnswersDataStore populate method error",e);
		}
		return questionAnswers;

}
	
}
