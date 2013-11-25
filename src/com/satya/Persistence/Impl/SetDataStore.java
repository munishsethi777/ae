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
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.GameTemplates;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.Result;
import com.satya.BusinessObjects.Set;
import com.satya.Persistence.ProjectDataStoreI;
import com.satya.Persistence.RowMapper;
import com.satya.Persistence.SetDataStoreI;

public class SetDataStore implements SetDataStoreI, RowMapper {

	Logger logger = Logger.getLogger(SetDataStore.class);
	private final static String SELECT = "select * from sets";
	private final static String SELECT_BY_SEQ = SELECT + " where seq = ?";
	private final static String SELECT_BY_PROJECT_SEQ = SELECT
			+ " where projectseq=?";
	private final static String SAVE_SET = "insert into sets(name, description, "
			+ "projectseq,isenabled,lastmodifieddate,createdon) "
			+ "values (?,?,?,?,?,?)";

	private final static String UPDATE_SET = "update sets set name=?, description=?, "
			+ "projectseq=?,isenabled=?,lastmodifieddate=? where seq=?";

	private final static String DELETE = "delete from sets where seq=?";
	private final static String DELETE_SET_GAMES = "delete from setgames where setseq=?";
	private final static String SAVE_SET_GAMES = "insert into setgames(setseq, gameseq) values (?,?)";

	private final static String FIND_SETS_SELECTED_IN_CAMPAIGN = "select sets.* from sets left join campaignsets on "
			+ "campaignsets.setseq = sets.seq left join campaigns on campaigns.seq = campaignsets.campaignseq "
			+ "where campaigns.seq = ?";

	private final static String FIND_SETS_AVAILABLE_FOR_CAMPAIGN = "select Distinct sets.* from sets left join campaignsets on "
			+ "campaignsets.setseq = sets.seq and campaignsets.campaignseq = ? left join campaigns on campaigns.seq = campaignsets.campaignseq "
			+ "where campaignsets.campaignseq is null ";

	private final static String FIND_SETS_GAMES_BY_CAMPAIGN = 
			"select sets.*, " +
			"games.seq as gameSeq,games.title as gameTitle,games.description as gameDescription, " +
			"gametemplates.name as gameTemplateName, gametemplates.imagepath as gameTemplateImagePath," +
			"results.score,results.timetaken,results.createdon as resultCreatedOn from sets " +
			"left join setgames on sets.seq = setgames.setseq " +
			"left join games on games.seq = setgames.gameseq " +
			"left join campaignsets on campaignsets.setseq = sets.seq " +
			"left join gametemplates on gametemplates.seq = games.gametemplateseq " +
			"left join results on results.gameseq = setgames.gameseq " +
			"where campaignsets.campaignseq = ?";
	
	private PersistenceMgr persistenceMgr;

	public SetDataStore(PersistenceMgr psmgr) {
		this.persistenceMgr = psmgr;
	}

	@Override
	public List<Set> findAvailableForCampaign(long campaignSeq) {
		Object[] params = new Object[] { campaignSeq };
		return (List<Set>) persistenceMgr.executePSQuery(
				FIND_SETS_AVAILABLE_FOR_CAMPAIGN, params, this);
	}

	@Override
	public List<Set> findSelectedForCampaign(long campaignSeq) {
		Object[] params = new Object[] { campaignSeq };
		return (List<Set>) persistenceMgr.executePSQuery(
				FIND_SETS_SELECTED_IN_CAMPAIGN, params, this);
	}

	@Override
	public void Save(Set set) {
		String SQL = set.getSeq() != 0 ? UPDATE_SET : SAVE_SET;
		try {
			long projectSeq = 0;
			if (set.getProject() != null) {
				projectSeq = set.getProject().getSeq();
			}
			Object[] params = new Object[6];
			params[0] = set.getName();
			params[1] = set.getDescription();
			params[2] = projectSeq;
			params[3] = set.isEnable();
			params[4] = set.getLastModifiedDate();
			if (set.getSeq() != 0) {
				params[5] = set.getSeq();
			} else {
				params[5] = set.getCreatedOn();
			}

			persistenceMgr.excecuteUpdate(SQL, params);
			if (set.getSeq() == 0) {
				set.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
			saveSetGames(set);
		} catch (Exception e) {

		}
	}

	private void saveSetGames(Set set) {
		Object[] deleteParams = new Object[1];
		deleteParams[0] = set.getSeq();
		persistenceMgr.excecuteUpdate(DELETE_SET_GAMES, deleteParams);
		if (set.getGames() != null) {
			for (Game game : set.getGames()) {
				Object[] params = new Object[2];
				params[0] = set.getSeq();
				params[1] = game.getSeq();

				try {
					persistenceMgr.excecuteUpdate(SAVE_SET_GAMES, params);
				} catch (Exception e) {
					logger.error("Error setting games on a set : "
							+ e.getMessage());
				}
			}
		}

	}

	@Override
	public void Delete(long setSeq) {
		Object[] params = new Object[] { setSeq };
		persistenceMgr.excecuteUpdate(DELETE, params);
	}

	@Override
	public List<Set> findByProjectSeq(long seq) {
		Object[] params = new Object[] { seq };
		return (List<Set>) persistenceMgr.executePSQuery(SELECT_BY_PROJECT_SEQ,
				params, this);
	}

	public List<Set> findAll() {
		return (List<Set>) persistenceMgr.executePSQuery(SELECT, null, this);
	}

	@Override
	public Set findBySeq(long seq) {
		Object[] params = new Object[] { seq };
		return (Set) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ,
				params, this);
	}

	public void DeleteGamesFromSet(long setSeq) {
		Object[] params = new Object[] { setSeq };
		persistenceMgr.excecuteUpdate(DELETE_SET_GAMES, params);
	}

	
	private List<Set> getSetsAndGames(String sql,Object [] params){
		final Map map = new HashMap();
		final List<Set> sets = new ArrayList<Set>();
		persistenceMgr.executePSQuery(sql, params, new RowMapper() {
			
			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				Set set = (Set) populateObjectFromResultSet(rs);
				if(map.get(set.getSeq()) == null ){
					map.put(set.getSeq(), set);
				}else{
					Set mapSet = (Set)map.get(set.getSeq());
					mapSet.getGames().add(set.getGames().get(0));
				}
				return null;
			}
		});
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			Set set = (Set)mEntry.getValue();
			sets.add(set);
		}
		return sets;
	}
	
	
	@Override
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}

	protected Object populateObjectFromResultSet(ResultSet rs)
			throws SQLException {
		Set set = null;
		try {
			long seq = rs.getLong("seq");
			String name = rs.getString("name");
			String description = rs.getString("description");
			Date createdon = rs.getDate("createdon");
			long projectSeq = rs.getLong("projectseq");
			boolean isEnable = rs.getBoolean("isenabled");
			Date lastModifiedDate = rs.getDate("lastmodifieddate");
			
			set = new Set();
			set.setSeq(seq);
			set.setName(name);
			set.setDescription(description);
			set.setCreatedOn(createdon);
			set.setLastModifiedDate(lastModifiedDate);
			set.setEnable(isEnable);
			Project project = null;
			if (projectSeq != 0) {
				ProjectDataStoreI PDS = ApplicationContext
						.getApplicationContext().getDataStoreMgr()
						.getProjectDataStore();
				project = PDS.findBySeq(projectSeq);
			}
			set.setProject(project);
			
			//Games persistence
			try{
				long gameSeq = rs.getLong("gameSeq");
				String gameTitle = rs.getString("gameTitle");
				String gameDescription = rs.getString("gameDescription");
				String gameTemplateName = rs.getString("gameTemplateName");
				String gameTemplateImagePath = rs.getString("gameTemplateImagePath");
				int score = rs.getInt("score");
				int timeTaken = rs.getInt("timetaken");
				Date resultCreatedOn = rs.getDate("resultCreatedOn");
				
				Game game = new Game();
				game.setSeq(gameSeq);
				game.setTitle(gameTitle);
				game.setDescription(gameDescription);
				GameTemplates gameTemplate = new GameTemplates(gameTemplateName);
				gameTemplate.setImagePath(gameTemplateImagePath);
				game.setGameTemplate(gameTemplate);
				if(resultCreatedOn != null){
					Result result = new Result();
					result.setTotalScore(String.valueOf(score));
					result.setTotalTime(String.valueOf(timeTaken));
					result.setCreatedOn(resultCreatedOn);
					game.setGameResult(result);
				}
				List<Game> games = new ArrayList<Game>();
				games.add(game);
				set.setGames(games);
			}catch(Exception e){
				logger.error("Exception occured while finding games on set");
			}
		} catch (Exception e) {
			logger.error("SetDataStore populate method error", e);
		}
		return set;

	}

	@Override
	public List<Set> findByCampaign(long campaignSeq) {
		Object[] params = new Object[] { campaignSeq };
		return this.getSetsAndGames(FIND_SETS_GAMES_BY_CAMPAIGN, params);}

}
