package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.satya.ApplicationContext;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.Managers.ProjectMgrI;
import com.satya.Managers.QuestionAnswersMgrI;
import com.satya.Persistence.QuestionDataStoreI;
import com.satya.Persistence.RowMapper;


public class QuestionDataStore implements QuestionDataStoreI,RowMapper {
	

	Logger logger = Logger.getLogger(QuestionDataStore.class);
	private final static String SELECT = "select questions.*,questionanswers.seq as answerSeq,questionanswers.title as " +
			"answerTitle,questionanswers.iscorrect as iscorrect from questions join questionanswers on questions.seq = questionanswers.questionseq and questions.isenabled=1 ";
	
	private final static String SELECT_BY_SEQ = SELECT + "where questions.seq = ?";
	private final static String SELECT_BY_PROJECT_SEQ = SELECT + "where projectseq = ?";
	
	private final static String SAVE = "insert into questions(title, description, points, " +
			"projectseq,isenabled,lastmodified,negativepoints,maxsecondsallowed,extraattemptsallowed,hint,createdon) " +
			"values (?,?,?,?,?,?,?,?,?,?,?)";
	private final static String UPDATE = "update questions set title=?, description=?, points=?, " +
			" projectseq=? ,isenabled=?,lastmodified=?,negativepoints=?,maxsecondsallowed=?,extraattemptsallowed=?, hint=? where seq=?";
	private final static String DELETE = "delete from questions where seq = ?";
	
	private final static String FIND_QUESTIONS_SELECTED_IN_GAME = "select questions.* from questions left join gamequestions on " +
			"gamequestions.questionseq = questions.seq left join games on games.seq = gamequestions.gameseq where games.seq = ? and questions.isenabled=1";
	
	private final static String FIND_QUESTIONS_AVAILABLE_FOR_GAME = "select DISTINCT questions.* from questions " +
			"left join gamequestions on gamequestions.questionseq = questions.seq and gamequestions.gameseq = ? " +
			"left join games on games.seq = gamequestions.gameseq where gamequestions.gameseq is null and questions.isenabled=1";
	
	
	
	
	private PersistenceMgr persistenceMgr;
	
	public QuestionDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	
	@Override
	public List<Questions> findSelectedByGameSeq(long gameSeq) {
		Object [] params = new Object [] {gameSeq};
		return (List<Questions>) persistenceMgr.executePSQuery(FIND_QUESTIONS_SELECTED_IN_GAME, params, this);
	}
	@Override
	public List<Questions> findAvailableByGameSeq(long gameSeq) {
		Object [] params = new Object [] {gameSeq};
		return (List<Questions>) persistenceMgr.executePSQuery(FIND_QUESTIONS_AVAILABLE_FOR_GAME, params, this);	
	}

	
	@Override
	public void Save(Questions questions){
		String SQL = questions.getSeq() != 0 ? UPDATE : SAVE;
		try{
			long projectSeq = 0;
			if(questions.getProject()!= null){
				projectSeq = questions.getProject().getSeq();
			}
			Object[] params  = new Object[11];
			
					params[0]= questions.getTitle();
					params[1] = questions.getDescription();
					params[2] = questions.getPoints();	
					params[3] = projectSeq;
					params[4] = questions.IsEnabled();
					params[5] = questions.getLastModified();
					params[6] = questions.getNegativePoints();
					params[7] = questions.getMaxSecondsAllowed();
					params[8] = questions.getExtraAttemptsAllowed();
					params[9] = questions.getHint();
					if(questions.getSeq() != 0){
						params[10] =  questions.getSeq();
					}else{
						params[10] = questions.getCreatedOn();
					}
		
			persistenceMgr.excecuteUpdate(SQL, params);
			if(questions.getSeq()==0){
				questions.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
			QuestionAnswersMgrI questionAnswerMgr = ApplicationContext.getApplicationContext().getQuestionAnswersMgr();
			questionAnswerMgr.deleteByQuestion(questions.getSeq());
			questionAnswerMgr.addQuestionAnswers(questions.getQuestionAnswers());
		}catch(Exception e){
			logger.error("Error during Save Questions",e);
		}
	}
	@Override
	public void Delete(long QuesSeq){
		Object [] params = new Object [] {QuesSeq};
		try{
		persistenceMgr.excecuteUpdate(DELETE, params);
		QuestionAnswersMgrI questionAnswerMgr = ApplicationContext.getApplicationContext().getQuestionAnswersMgr();
		questionAnswerMgr.deleteByQuestion(QuesSeq);
		}catch (Exception e){
			logger.error("Error during delete questions", e);
		}
	}
	public List<Questions> findAll(){		
		return getQuestionsAndAnswers(SELECT, null);
	}
	public Questions findBySeq(long seq){
		Object [] params = new Object [] {seq};
		List<Questions>questions =  getQuestionsAndAnswers(SELECT_BY_SEQ, params);
		return questions.get(0);
	}
	
	public List<Questions> findByProjectSeq(long seq){
		Object [] params = new Object [] {seq};
		return getQuestionsAndAnswers(SELECT_BY_PROJECT_SEQ, params);
	}
	
	private List<Questions> getQuestionsAndAnswers(String sql,Object [] params){
		final Map map = new HashMap();
		final List<Questions> questions = new ArrayList<Questions>();
		persistenceMgr.executePSQuery(sql, params, new RowMapper() {
			
			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				try{
					Questions question = (Questions) populateObjectFromResultSet(rs);
					if(map.get(question.getSeq()) == null ){
						map.put(question.getSeq(), question);
					}else{
						Questions mapQuestion = (Questions)map.get(question.getSeq());
						mapQuestion.getQuestionAnswers().add(question.getQuestionAnswers().get(0));
					}
				}catch(Exception e){
					logger.error(e);
				}
				return null;
			}
		});
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			Questions question = (Questions)mEntry.getValue();
			questions.add(question);
		}
		return questions;
	}
	
	
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	
	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		Questions questions  = null;
		try{			
			long seq = rs.getLong("seq");
			String title = rs.getString("title");
			String  description = rs.getString("description");	
			Integer points = rs.getInt("points");
			Integer negativePoints = rs.getInt("negativepoints");
			Integer maxSecondsAllowed = rs.getInt("maxsecondsallowed");
			Integer extraAttemptsAllowed = rs.getInt("extraattemptsallowed");
			long projectSeq = rs.getLong("projectseq");
			boolean isEnable = rs.getBoolean("isenabled");
			Date lastModifiedDate = rs.getDate("lastmodified");
			Date createdOn = rs.getDate("createdon");
			String hint = rs.getString("hint");
			List<QuestionAnswers> qanswers = new ArrayList<QuestionAnswers>();
			try{
				long answerseq = rs.getLong("answerSeq");
				String answerTitle = rs.getString("answerTitle");
				boolean isCorrect = rs.getBoolean("iscorrect");
				QuestionAnswers qa = new QuestionAnswers();
				qa.setSeq(answerseq);
				qa.setAnswerTitle(answerTitle);
				qa.setCorrect(isCorrect);
				
				qanswers.add(qa);
				
			}catch(Exception e){}
			
			
			questions = new Questions();
			questions.setQuestionAnswers(qanswers);
			questions.setSeq(seq);
			questions.setTitle(title);
			questions.setDescription(description);
			questions.setPoints(points);
			questions.setIsEnabled(isEnable);
			questions.setLastModified(lastModifiedDate);
			questions.setCreatedOn(createdOn);
			questions.setNegativePoints(negativePoints);
			questions.setExtraAttemptsAllowed(extraAttemptsAllowed);
			questions.setMaxSecondsAllowed(maxSecondsAllowed);
			questions.setHint(hint);
			Project project = null;
			if(projectSeq != 0 ){
				ProjectMgrI projectMgr = ApplicationContext.getApplicationContext().getProjectMgr();
				project = projectMgr.FindBySeq(projectSeq);
			}
			questions.setProject(project);
			
			
		}catch(Exception e){
			logger.error("QuestionsDataStore populate method error",e);
		}
		return questions;

}
}
