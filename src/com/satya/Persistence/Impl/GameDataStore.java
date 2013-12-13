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

import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.GameTemplates;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.RowMapper;

public class GameDataStore implements GameDataStoreI, RowMapper {

	Logger logger = Logger.getLogger(GameDataStore.class);
	private PersistenceMgr persistenceMgr;
	private final static String SELECT = "select * from games where isenabled=?";
	private final static String DELETE = "delete from games where seq = ?";
	private final static String SELECT_BY_PROJECT_SEQ = "select * from games where projectseq = ?";

	private final static String SAVE = "insert into games(title, description, gametemplateseq, "
			+ "projectseq,isenabled,lastmodifieddate,maxsecondsallowed,createdon,ispublished) "
			+ "values (?,?,?,?,?,?,?,?,?)";
	private final static String UPDATE = "update games set title=?, description=?, gametemplateseq=?, "
			+ " projectseq=?, isenabled=?,lastmodifieddate=?,maxsecondsallowed=?,ispublished=? where seq=?";

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
	//particularly used for XML generation
	private static String FIND_GAME_WITH_QUESTION_ANSWERS =
			"select distinct games.*, "+
			"questions.seq as questionseq,questions.title as questiontitle, questions.description as questiondescription,questions.negativepoints, "+
			"questions.maxsecondsallowed as questionmaxsecondsallowed, "+
			"questionanswers.seq as answerseq, questionanswers.title as answertitle, questionanswers.iscorrect "+
			"from games "+
			"left join gamequestions on gamequestions.gameseq = games.seq "+
			"left join questions on questions.seq = gamequestions.questionseq "+
			"left join questionanswers on questionanswers.questionseq = gamequestions.questionseq "+
			"where games.seq = ?";
	
	private static String FIND_GAMES_BY_CAMPAIGN = 
			"select games.* from games left join campaigngames on campaigngames.gameseq = games.seq "+
			"where campaigngames.campaignseq = ?";
	
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

			Object[] params = new Object[9];

			params[0] = game.getTitle();
			params[1] = game.getDescription();
			params[2] = templateSeq;
			params[3] = projectSeq;
			params[4] = game.isEnable();
			params[5] = game.getLastModifiedDate();
			params[6] = game.getMaxSecondsAllowed();
			params[7] = game.isPublished();
			if (game.getSeq() != 0) {
				params[8] = game.getSeq();
			} else {
				params[8] = game.getCreatedOn();
			}

			persistenceMgr.excecuteUpdate(SQL, params);
			if (game.getSeq() == 0) {
				game.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
			saveGameQuestions(game);
		} catch (Exception e) {
			logger.error("Error during Save Questions", e);
		}
	}

	private void saveGameQuestions(Game game) {
		Object[] deleteParams = new Object[1];
		deleteParams[0] = game.getSeq();
		persistenceMgr.excecuteUpdate(DELETE_QUESTIONS, deleteParams);
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
	public Game findBySeq(long seq) {
		Object[] params = new Object[] { seq };
		return (Game) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ,
				params, this);
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

	@Override
	public List<Game> findSelectedForCampaign(long campaignSeq) {
		Object[] params = new Object[] { campaignSeq };
		return (List<Game>) persistenceMgr.executePSQuery(
				FIND_GAMES_BY_CAMPAIGN, params, this);
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
				game = (Game)populateObjectFromResultSet(rs);
				Questions question = game.getQuestions().get(0);
				
				if(questionsMap.get(question.getSeq()) == null){
					questionsMap.put(question.getSeq(), question);
				}else{
					Questions mapQuestion = (Questions) questionsMap.get(question.getSeq());
					mapQuestion.getQuestionAnswers().add(question.getQuestionAnswers().get(0));
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
		return populateObjectFromResultSet(rs);
	}

	protected Object populateObjectFromResultSet(ResultSet rs)
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
			
			game = new Game();
			game.setSeq(seq);
			game.setTitle(title);
			game.setDescription(description);
			game.setEnable(isEnable);
			game.setLastModifiedDate(lastModifiedDate);
			game.setMaxSecondsAllowed(maxSecondsAllowed);
			game.setPublished(isPublished);
			GameTemplates gameTemplates = null;
			//Capture Game Template
			try {
				long templateSeq = rs.getLong("gameTemplateSeq");
				if(templateSeq != 0){
					gameTemplates = new GameTemplates(templateSeq);
					String gameTemplateName = rs.getString("gameTemplateName");
					String path = rs.getString("gameTemplatePath");
					String imagePath = rs.getString("gameTemplateImagePath");
					String templateDescription = rs.getString("gameTemplateDescriotion");
					
					gameTemplates.setName(gameTemplateName);
					gameTemplates.setImagePath(imagePath);
					gameTemplates.setPath(path);
					gameTemplates.setDescription(templateDescription);
					game.setGameTemplate(gameTemplates);
				}
				
			} catch (Exception e) {
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
					String quesTitle = rs.getString("questiontitle");
					String quesDescription = rs.getString("questiondescription");
					int negativePoints = rs.getInt("negativepoints");
					int quesMaxSecondsAllowed= rs.getInt("questionmaxsecondsallowed");
					
					question.setSeq(questionSeq);
					question.setTitle(quesTitle);
					question.setDescription(quesDescription);
					question.setNegativePoints(negativePoints);
					question.setMaxSecondsAllowed(quesMaxSecondsAllowed);
					
					game.setQuestions(new ArrayList<Questions>());
					game.getQuestions().add(question);
				}
				
			} catch (Exception e) {
				
			}
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

		} catch (Exception e) {
			logger.error("GameDataStore populate method error", e);
		}
		return game;

	}

}
