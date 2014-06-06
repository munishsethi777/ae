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
import org.apache.poi.util.StringUtil;

import com.mysql.jdbc.StringUtils;
import com.satya.ApplicationContext;
import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.GameTemplates;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.QuestionDataStoreI;
import com.satya.Persistence.RowMapper;

public class GameDataStore implements GameDataStoreI, RowMapper {

	Logger logger = Logger.getLogger(GameDataStore.class);
	private PersistenceMgr persistenceMgr;
	private final static String SELECT = "select * from games where isenabled=?";
	private final static String DELETE = "delete from games where seq = ?";
	private final static String SELECT_BY_PROJECT_SEQ = "select * from games where projectseq = ?";

	private final static String SAVE = "insert into games(title, description, gametemplateseq, "
			+ "projectseq,isenabled,lastmodifieddate,maxsecondsallowed,ispublished,maxquestions,imagepath,createdon) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?)";
	private final static String UPDATE = "update games set title=?, description=?, gametemplateseq=?, "
			+ " projectseq=?, isenabled=?,lastmodifieddate=?,maxsecondsallowed=?,ispublished=?,maxquestions=?,imagepath=? where seq=?";

	private final static String UPDATE_GAME_DETAILS = "update games set title=?, description=? where seq=?";
	private final static String SAVE_QUESTIONS = "insert into gamequestions(gameseq, questionseq) values (?,?)";

	private final static String DELETE_QUESTIONS = "delete from gamequestions where gameseq = ?";

	private final static String FIND_GAMES_SELECTED_IN_SET = "select games.* from games left join setgames on setgames.gameseq = games.seq"
			+ " left join sets on sets.seq = setgames.setseq where sets.seq = ?";

	private final static String FIND_GAMES_AVAILABLE_FOR_SET = "select Distinct games.* from games"
			+ " left join setgames on setgames.gameseq = games.seq and setgames.setseq = ?"
			+ " left join sets on sets.seq = setgames.setseq where setgames.setseq is null and games.isenabled = 1";

	private final static String SELECT_BY_SEQ = "select games.*,gametemplates.seq as gameTemplateSeq, " +
			"gametemplates.name as gameTemplateName,gametemplates.path as gameTemplatePath,gametemplates.imagepath as gameTemplateImagePath " +
			",gametemplates.description as gameTemplateDescriotion from games "
			+ "left join gametemplates on games.gametemplateseq=gametemplates.seq where games.seq = ?";

	private final static String FIND_GAME_BY_GAMESEQ_SETSEQ_CAMPSEQ_USERSEQ = "select distinct games.* from games "
			+ "left join setgames on setgames.gameseq = games.seq "
			+ "left join sets on sets.seq = setgames.setseq "
			+ "left join campaignsets on campaignsets.setseq = sets.seq "
			+ "left join campaigns on campaigns.seq = campaignsets.campaignseq "
			+ "left join campaignusergroups on campaignusergroups.campaignseq = campaigns.seq "
			+ "left join usergroups on usergroups.seq =campaignusergroups.usergroupseq "
			+ "left join usergroupusers on usergroupusers.usergroupseq = campaignusergroups.usergroupseq "
			+ "left join users on users.seq = usergroupusers.userseq "
			+ "where campaigns.seq = ? and sets.seq = ? and games.seq = ? and users.seq = ? ";
	
	private final static String FIND_GAME_BY_GAMESEQ_CAMPSEQ_USERSEQ = "select distinct games.* from games " +
			"left join campaigngames on campaigngames.gameseq = games.seq " +
			"left join campaigns on campaigns.seq = campaigngames.campaignseq " +
			"left join campaignusergroups on campaignusergroups.campaignseq = campaigns.seq " +
			"left join usergroups on usergroups.seq =campaignusergroups.usergroupseq " +
			"left join usergroupusers on usergroupusers.usergroupseq = campaignusergroups.usergroupseq " +
			"left join users on users.seq = usergroupusers.userseq " +
			"where campaigns.seq = ?  and games.seq = ? and users.seq = ? ";
	//particularly used for XML generation
	private static String FIND_GAME_WITH_QUESTION_ANSWERS_COMMON = 
					"select distinct games.*, "+
					"questions.seq as questionseq,questions.title as questiontitle, questions.description as questiondescription,questions.negativepoints, "+
					"questions.maxsecondsallowed as questionmaxsecondsallowed, "+
					"questionanswers.seq as answerseq, questionanswers.title as answertitle, questionanswers.iscorrect "+
					"from games "+
					"left join gamequestions on gamequestions.gameseq = games.seq "+
					"left join questions on questions.seq = gamequestions.questionseq "+
					"left join questionanswers on questionanswers.questionseq = gamequestions.questionseq ";
					
	
	private static String FIND_GAME_WITH_QUESTION_ANSWERS =
			FIND_GAME_WITH_QUESTION_ANSWERS_COMMON + 	"where games.seq = ?";
	
	private static String FIND_GAMES_BY_CAMPAIGN = 
//			"select distinct games.*, "+
//			"questions.seq as questionseq,questions.title as questiontitle, questions.description as questiondescription,questions.negativepoints, "+
//			"questions.maxsecondsallowed as questionmaxsecondsallowed, "+
//			"questionanswers.seq as answerseq, questionanswers.title as answertitle, questionanswers.iscorrect "+
//			"from games "+
//			"left join gamequestions on gamequestions.gameseq = games.seq "+
//			"left join questions on questions.seq = gamequestions.questionseq "+
//			"left join questionanswers on questionanswers.questionseq = gamequestions.questionseq "+
//			"left join campaigngames on campaigngames.gameseq = games.seq "+
//			"where campaigngames.campaignseq = ?";
			"select distinct games.*, "+
			"questions.seq as questionseq "+
			"from games "+
			"left join gamequestions on gamequestions.gameseq = games.seq "+
			"left join questions on questions.seq = gamequestions.questionseq "+
			"left join campaigngames on campaigngames.gameseq = games.seq "+
			"where campaigngames.campaignseq = ?";
	
	private static String REMOVE_QUESTION_FROM_GAME ="delete from gamequestions where gameseq = ? and questionseq=?";
	
	public GameDataStore(PersistenceMgr psmgr) {
		this.persistenceMgr = psmgr;
	}

	public void Save(Game game) {
		String SQL = game.getSeq() != 0 ? UPDATE : SAVE;
		try {
			long projectSeq = 0;
			if (game.getProject() != null) {
				projectSeq = game.getProject().getSeq();
			}
			long templateSeq = 0;
			if (game.getGameTemplate() != null) {
				templateSeq = game.getGameTemplate().getSeq();
			}

			Object[] params = new Object[11];
			
			params[0] = game.getTitle();
			params[1] = game.getDescription();
			params[2] = templateSeq;
			params[3] = projectSeq;
			params[4] = game.isEnable();
			params[5] = game.getLastModifiedDate();
			params[6] = game.getMaxSecondsAllowed();
			params[7] = game.isPublished();
			params[8] = game.getMaxQuestions();
			params[9] = game.getImagePath();
			if (game.getSeq() != 0) {
				params[10] = game.getSeq();
			} else {
				params[10] = game.getCreatedOn();
			}

			persistenceMgr.excecuteUpdate(SQL, params);
			if (game.getSeq() == 0) {
				game.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
			saveGameQuestions(game,true);
		} catch (Exception e) {
			logger.error("Error during Save Questions", e);
		}
	}
	
	@Override
	public void saveGameQuestions(Game game,boolean isDeleteEarlierQuestionRelations) {
		if(isDeleteEarlierQuestionRelations){
			Object[] deleteParams = new Object[1];
			deleteParams[0] = game.getSeq();		
			persistenceMgr.excecuteUpdate(DELETE_QUESTIONS, deleteParams);
		}
		if (game.getQuestions() != null) {
			for (Questions question : game.getQuestions()) {
				Object[] params = new Object[2];
				params[0] = game.getSeq();
				params[1] = question.getSeq();

				try {
					persistenceMgr.excecuteUpdate(SAVE_QUESTIONS, params);
				} catch (Exception e) {
					logger.error("Error setting questions on a game : "
							+ e.getMessage());
				}
			}
		}

	}

	public List<Game> FindAll(boolean isEnable) {
		Object[] params = new Object[] { isEnable };
		return (List<Game>) persistenceMgr.executePSQuery(SELECT, params, this);
	}

	@Override
	public void Delete(long gameSeq) {
		Object[] params = new Object[] { gameSeq };
		persistenceMgr.excecuteUpdate(DELETE, params);
	}

	@Override
	public Game findBySeq(long seq) {//with game template details
		Object[] params = new Object[] { seq };
		return (Game) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ,
				params, new RowMapper() {
					
					@Override
					public Object mapRow(ResultSet rs) throws SQLException {
						return populateObjectFromResultSet(rs, true);
					}
				});
	}

	@Override
	public List<Game> FindByProject(long projectSeq) {
		Object[] params = new Object[] {projectSeq };
		return (List<Game>) persistenceMgr.executePSQuery(
				SELECT_BY_PROJECT_SEQ, params, this);
	}

	@Override
	public List<Game> findAvailableForSet(long setSeq) {
		Object[] params = new Object[] { setSeq };
		return (List<Game>) persistenceMgr.executePSQuery(
				FIND_GAMES_AVAILABLE_FOR_SET, params, this);
	}

	@Override
	public List<Game> findSelectedForSet(long setSeq) {
		Object[] params = new Object[] { setSeq };
		return (List<Game>) persistenceMgr.executePSQuery(
				FIND_GAMES_SELECTED_IN_SET, params, this);
	}
	
	//get games with total questions and no answers
	@Override
	public List<Game> findSelectedForCampaign(long campaignSeq) {
		Object[] params = new Object[] { campaignSeq };
		final Map questionsMap = new HashMap();
		final Map<Long, Game> gamesMap = new HashMap();
		final List<Questions> questions = new ArrayList<Questions>();
		final List<Game> games = new ArrayList<Game>();
		persistenceMgr.executePSQuery(
				FIND_GAMES_BY_CAMPAIGN, params, new RowMapper(){
					@Override
					public Object mapRow(ResultSet rs) throws SQLException {
						try{
							game = (Game)populateFullObjectFromResultSet(rs,false);
							Questions question = null;
							if(game.getQuestions() != null){
								question = game.getQuestions().get(0);
							}
							if(!gamesMap.containsKey(game.getSeq())){
								gamesMap.put(game.getSeq(),game);
							}else{
								if(question != null){
									Game mapGame = (Game)gamesMap.get(game.getSeq());
									mapGame.getQuestions().add(question);
									gamesMap.put(mapGame.getSeq(),mapGame);
								}
							}
						}catch(Exception e){
							logger.error(e);
						}
						
						return null;
					}
				});
		Iterator iter = gamesMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			Game game = (Game)mEntry.getValue();
			games.add(game);
		}
		return games;
	}
	
	// Method used to make sure if game is for this user alloted
	@Override
	public boolean isGameByCampaignSetGameUser(long campaignSeq, long setSeq,
			long gameSeq, long userSeq) {
		Object[] params = new Object[] { campaignSeq, setSeq, gameSeq, userSeq };
		List<Game> games = (List<Game>) persistenceMgr.executePSQuery(
				FIND_GAME_BY_GAMESEQ_SETSEQ_CAMPSEQ_USERSEQ, params, this);
		if (games != null && games.size()!=0){
			return true;
		}
		return false;
	}
	// Method used to make sure if game is for this user alloted
		@Override
		public boolean isGameByCampaignGameUser(long campaignSeq, long gameSeq, long userSeq) {
			Object[] params = new Object[] { campaignSeq, gameSeq, userSeq };
			List<Game> games = (List<Game>) persistenceMgr.executePSQuery(
					FIND_GAME_BY_GAMESEQ_CAMPSEQ_USERSEQ, params, this);
			if (games != null && games.size()!=0){
				return true;
			}
			return false;
		}
	
	//Method used to get a Games Questions with Answers also
	Game game = null;
	public Game findBySeqWithQuesAnswers(long seq){
		Object[] params = new Object[] { seq };
		final Map questionsMap = new HashMap();
		final List<Questions> questions = new ArrayList<Questions>();
		final List<QuestionAnswers> answers = new ArrayList<QuestionAnswers>();
		
		persistenceMgr.executePSQuery(FIND_GAME_WITH_QUESTION_ANSWERS, params, new RowMapper() {
			
			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				game = (Game)populateFullObjectFromResultSet(rs,true);
				Questions question = game.getQuestions().get(0);
				if(question != null){
					if(questionsMap.get(question.getSeq()) == null){
						questionsMap.put(question.getSeq(), question);
					}else{
						Questions mapQuestion = (Questions) questionsMap.get(question.getSeq());
						mapQuestion.getQuestionAnswers().add(question.getQuestionAnswers().get(0));
					}
				}
				return null;
			}
		});
		game.setQuestions(new ArrayList<Questions>());
		Iterator iter = questionsMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			Questions question = (Questions)mEntry.getValue();
			for(QuestionAnswers answer : question.getQuestionAnswers()){
				if(answer.isCorrect()){
					question.setAnswerId(answer.getSeq());
				}
			}
			game.getQuestions().add(question);
		}
		return game;
	}
	
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs,false);
	}

	protected Object populateObjectFromResultSet(ResultSet rs,boolean isWithTemplateDetails)
			throws SQLException {
		Game game = null;
		try {
			long seq = rs.getLong("seq");
			String title = rs.getString("title");
			String description = rs.getString("description");
			boolean isEnable = rs.getBoolean("isenabled");
			Date lastModifiedDate = rs.getDate("lastmodifieddate");
			int maxSecondsAllowed = rs.getInt("maxsecondsallowed");
			boolean isPublished = rs.getBoolean("ispublished");
			int maxQuestions = rs.getInt("maxquestions");
			String imagePath = rs.getString("imagepath");
			game = new Game();
			game.setSeq(seq);
			game.setTitle(title);
			game.setDescription(description);
			game.setEnable(isEnable);
			game.setLastModifiedDate(lastModifiedDate);
			game.setMaxSecondsAllowed(maxSecondsAllowed);
			game.setPublished(isPublished);
			game.setMaxQuestions(maxQuestions);
			game.setImagePath(imagePath);
		} catch (Exception e) {
			logger.error("GameDataStore populate method error", e);
		}
		GameTemplates gameTemplate = new GameTemplates();
		if(isWithTemplateDetails){
			//Capture Game Template
			try {
				long templateSeq = rs.getLong("gameTemplateSeq");
				if(templateSeq != 0){
					gameTemplate = new GameTemplates(templateSeq);
					String gameTemplateName = rs.getString("gameTemplateName");
					String path = rs.getString("gameTemplatePath");
					String templateImagePath = rs.getString("gameTemplateImagePath");
					String templateDescription = rs.getString("gameTemplateDescriotion");
					
					gameTemplate.setName(gameTemplateName);
					gameTemplate.setImagePath(templateImagePath);
					gameTemplate.setPath(path);
					gameTemplate.setDescription(templateDescription);
				}
				
			} catch (Exception e) {
				logger.error("Error occured while grabbing game template details",e);
			}finally{
				if(gameTemplate != null){
					game.setGameTemplate(gameTemplate);
				}
			}
		}
		return game;

	}
	protected Object populateFullObjectFromResultSet(ResultSet rs,boolean isWithQuestionAnswers)
			throws SQLException {
		Game game = null;
		try {
			long seq = rs.getLong("seq");
			String title = rs.getString("title");
			String description = rs.getString("description");
			boolean isEnable = rs.getBoolean("isenabled");
			Date lastModifiedDate = rs.getDate("lastmodifieddate");
			int maxSecondsAllowed = rs.getInt("maxsecondsallowed");
			boolean isPublished = rs.getBoolean("ispublished");
			int maxQuestions = rs.getInt("maxquestions");
			String imagePath = rs.getString("imagepath");
			game = new Game();
			game.setSeq(seq);
			game.setTitle(title);
			game.setDescription(description);
			game.setEnable(isEnable);
			game.setLastModifiedDate(lastModifiedDate);
			game.setMaxSecondsAllowed(maxSecondsAllowed);
			game.setPublished(isPublished);
			game.setMaxQuestions(maxQuestions);
			game.setImagePath(imagePath);
			GameTemplates gameTemplates = null;
			//Capture Game Template
			try {
				long templateSeq = rs.getLong("gameTemplateSeq");
				if(templateSeq != 0){
					gameTemplates = new GameTemplates(templateSeq);
					/*String gameTemplateName = rs.getString("gameTemplateName");
					String path = rs.getString("gameTemplatePath");
					String templateImagePath = rs.getString("gameTemplateImagePath");
					String templateDescription = rs.getString("gameTemplateDescriotion");
					
					gameTemplates.setName(gameTemplateName);
					gameTemplates.setImagePath(templateImagePath);
					gameTemplates.setPath(path);
					gameTemplates.setDescription(templateDescription);
					game.setGameTemplate(gameTemplates);*/
				}
				
			} catch (Exception e) {
				logger.error("Error occured while grabbing game template details",e);
			}finally{
				if(gameTemplates != null){
					game.setGameTemplate(gameTemplates);
				}
			}
			
				Questions question = null;
				//Capture Question
				try {
					long questionSeq = rs.getLong("questionseq");
					if(questionSeq != 0){
						question = new Questions();
						question.setSeq(questionSeq);
						if(isWithQuestionAnswers){//if its QA mode, then will fetch complete q and ans
							String quesTitle = rs.getString("questiontitle");
							question.setTitle(quesTitle);
							String quesDescription = rs.getString("questiondescription");
							question.setDescription(quesDescription);
							int negativePoints = rs.getInt("negativepoints");
							question.setNegativePoints(negativePoints);
							int quesMaxSecondsAllowed= rs.getInt("questionmaxsecondsallowed");
							question.setMaxSecondsAllowed(quesMaxSecondsAllowed);
						}
					}
				} catch (Exception e) {
					logger.error("Error Occured while grabbing question for game",e);
				}
				finally{
					if(question != null){
						game.setQuestions(new ArrayList<Questions>());
						game.getQuestions().add(question);
					}
				}
				if(isWithQuestionAnswers){
					QuestionAnswers answer = null;
					//Capture QuestionAnswer
					try {
						long answerSeq = rs.getLong("answerseq");
						if(answerSeq != 0){
							answer = new QuestionAnswers();
							String answerTitle = rs.getString("answertitle");
							boolean isCorrect = rs.getBoolean("iscorrect");
							
							answer.setSeq(answerSeq);
							answer.setAnswerTitle(answerTitle);
							answer.setCorrect(isCorrect);
							question.setQuestionAnswers(new ArrayList<QuestionAnswers>());
							question.getQuestionAnswers().add(answer);
						}
						
					} catch (Exception e) {
		
					}
				}

		} catch (Exception e) {
			logger.error("GameDataStore populate method error", e);
		}
		return game;

	}
	@Override
	public List<Game> findBySeqs(boolean isEnable, Long[] gameSeqs) {
		StringBuilder seqsStr = new StringBuilder();
		for(int i = 0 ; i<gameSeqs.length ; i++){
			seqsStr.append(gameSeqs[i]);
			if(i < (gameSeqs.length-1)){
				seqsStr.append(",");
			}
		}
		String sql = "select * from games where isenabled = 1 and seq in ("+ seqsStr +")";
		if(isEnable){
			sql = "select * from games where isenabled = 1 and seq in ("+ seqsStr +")";
		}
		return (List<Game>) persistenceMgr.executePSQuery(sql, this);
	}

	@Override
	public void saveGameDetails(Game game) {
		Object[] params = new Object[] { game.getSeq() };
		persistenceMgr.excecuteUpdate(UPDATE_GAME_DETAILS,  params);
	}

	@Override
	public void deleteGameQuestion(long gameSeq, long questionSeq) {
		Object[] params = new Object[] { gameSeq, questionSeq };
		persistenceMgr.excecuteUpdate(REMOVE_QUESTION_FROM_GAME,  params);
	}

}
