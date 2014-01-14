package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.satya.ApplicationContext;
import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.UserGroup;
import com.satya.Persistence.CampaignDataStoreI;
import com.satya.Persistence.ProjectDataStoreI;
import com.satya.Persistence.RowMapper;

public class CampaignDataStore implements CampaignDataStoreI, RowMapper {

	Logger logger = Logger.getLogger(CampaignDataStore.class);

	private final static String SELECT = "select campaigns.*, games.seq as gameSeq, games.title as gameTitle, games.description as gameDescription," +
			" games.projectseq as gameProjectSeq, games.lastmodifieddate as gameLastModifiedDate, games.isenabled as gameIsEnabled,games.ispublished as ispublished " +
			" ,games.imagepath as imagepath from campaigns" +
			" left join campaigngames on campaigngames.campaignseq = campaigns.seq left join games on games.seq = campaigngames.gameseq ";
	
	private final static String SELECT_BY_SEQ = SELECT + "where campaigns.seq = ?";
	private final static String SELECT_BY_PROJECT_SEQ = SELECT + "where campaigns.projectseq = ?";
	private final static String SAVE = "insert into campaigns(projectseq,name,description,validitydays " + 
		",isenabled,lastmodifieddate,startdate,validtilldate,launchMessage,createdon) " +
		"values (?,?,?,?,?,?,?,?,?,?)";
	private final static String UPDATE = "update campaigns set projectseq=?, " +
		"name=?,description=?, validitydays=?, isenabled=? ,lastmodifieddate=?, startdate=?,validtilldate=?,launchMessage=? where seq=?";


	
	private final static String DELETE = "delete from campaigns where seq = ?";

	private final static String SAVE_USERGROUPS = "insert into campaignusergroups(campaignseq, usergroupseq) values (?,?)";
	private final static String DELETE_USERGROUPS = "delete from campaignusergroups where campaignseq = ?";

	private final static String SAVE_CAMPAIGN_GAMES = "insert into campaigngames(campaignseq, gameseq) values (?,?)";

	private final static String DELETE_GAMES = "delete from campaigngames where campaignseq = ?";

	private final static String FIND_BY_USER_SEQ = "select distinct campaigns.* from campaigns "
			+ "left join campaignusergroups on campaignusergroups.campaignseq = campaigns.seq "
			+ "left join usergroups on usergroups.seq = campaignusergroups.usergroupseq "
			+ "left join usergroupusers on usergroupusers.usergroupseq = usergroups.seq "
			+ "left join users on users.seq = usergroupusers.userseq "
			+ "where users.seq=?";

	private final static String PUBLISH_CAMPAIGN = "update campaigns set ispublished=? where seq=?";
	private final static String COUNT_CAMPAIGN_BY_NAME = "select count(*) from Campaigns where name = ? and projectseq=?";
	private PersistenceMgr persistenceMgr;

	public CampaignDataStore(PersistenceMgr psmgr) {
		this.persistenceMgr = psmgr;
	}

	@Override
	public void Save(Campaign campaign) {
		String SQL = campaign.getSeq() != 0 ? UPDATE : SAVE;
		try {
			long projectSeq = 0;
			if (campaign.getProject() != null) {
				projectSeq = campaign.getProject().getSeq();
			}
			Object[] params = new Object[10];

			params[0] = projectSeq;
			params[1] = campaign.getName();
			params[2] = campaign.getDescription();
			params[3] = campaign.getValidityDays();
			params[4] = campaign.isEnabled();
			params[5] = campaign.getLastModifiedDate();
			params[6] = campaign.getStartDate();
			params[7] = campaign.getValidTillDate();
			params[8] = campaign.getLaunchMessage();
			if (campaign.getSeq() != 0) {
				params[9] = campaign.getSeq();
			} else {
				params[9] = campaign.getCreatedOn();
			}

			persistenceMgr.excecuteUpdate(SQL, params);
			if (campaign.getSeq() == 0) {
				campaign.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
			// saveUserGroups(campaign);
			// saveSets(campaign);
		} catch (Exception e) {
			logger.error("Error occured while saving a campaign "
					+ e.getMessage());
		}
	}

	@Override
	public void saveUserGroups(Campaign campaign) {
		Object[] deleteParams = new Object[1];
		deleteParams[0] = campaign.getSeq();
		persistenceMgr.excecuteUpdate(DELETE_USERGROUPS, deleteParams);

		for (UserGroup userGroup : campaign.getUserGroups()) {
			Object[] params = new Object[2];
			params[0] = campaign.getSeq();
			params[1] = userGroup.getSeq();
			try {
				persistenceMgr.excecuteUpdate(SAVE_USERGROUPS, params);
			} catch (Exception e) {
				logger.error("Error setting usergroups on campaign"
						+ e.getMessage());
			}
		}

	}

	@Override
	public void saveGames(Campaign campaign, boolean isDeleteEarlierFirst) {
		if (isDeleteEarlierFirst) {
			Object[] deleteParams = new Object[1];
			deleteParams[0] = campaign.getSeq();
			persistenceMgr.excecuteUpdate(DELETE_GAMES, deleteParams);
		}
		for (Game game : campaign.getGames()) {
			Object[] params = new Object[2];
			params[0] = campaign.getSeq();
			params[1] = game.getSeq();
			try {
				persistenceMgr.excecuteUpdate(SAVE_CAMPAIGN_GAMES, params);
			} catch (Exception e) {
				logger.error("Error setting sets on campaign" + e.getMessage());
			}
		}

	}

	@Override
	public void Delete(long campaignSeq) {
		Object[] params = new Object[] { campaignSeq };
		persistenceMgr.excecuteUpdate(DELETE, params);
	}

	@Override
	public List<Campaign> findByProjectSeq(long seq) {
		Object[] params = new Object[] { seq };
		return this.getCampaignSets(SELECT_BY_PROJECT_SEQ, params);
		// return (List<Campaign>)
		// persistenceMgr.executePSQuery(SELECT_BY_PROJECT_SEQ, params, this);
	}

	public List<Campaign> findAll() {
		return this.getCampaignSets(SELECT, null);
		// return (List<Campaign>)persistenceMgr.executePSQuery(SELECT, null,
		// this);
	}

	@Override
	public Campaign findBySeq(long seq) {
		Object[] params = new Object[] { seq };
		List<Campaign> campaigns = this.getCampaignSets(SELECT_BY_SEQ, params);
		return campaigns.get(0);
		// return (Campaign)
		// persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ, params, this);
	}

	/*
	 * USER LOGGIN METHODS STARTS
	 */
	@Override
	public List<Campaign> findByUserSeq(long seq) {
		Object[] params = new Object[] { seq };
		return (List<Campaign>) persistenceMgr.executePSQuery(FIND_BY_USER_SEQ,
				params, this);
	}

	@Override
	public int getCampaignSetSeq(long campaignSeq, long setSeq) {
		Object[] params = new Object[] { campaignSeq, setSeq };
		String sql = "select seq from campaignsets where campaignseq = ? and setseq=?";
		Object campaignSetSeq = persistenceMgr.executeSingleObjectQuery(sql,
				params, new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs) throws SQLException {
						return rs.getObject("seq");
					}
				});
		return (Integer) campaignSetSeq;
	}

	/*
	 * COMMON INFRASTRUCTURAL METHODS STARTS
	 */
	private List<Campaign> getCampaignSets(String sql, Object[] params) {
		final Map map = new HashMap();
		final List<Campaign> campaigns = new ArrayList<Campaign>();
		persistenceMgr.executePSQuery(sql, params, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				Campaign campaign = (Campaign) populateObjectFromResultSet(rs);
				if (map.get(campaign.getSeq()) == null) {
					map.put(campaign.getSeq(), campaign);
				} else {
					Campaign mapCampaign = (Campaign) map.get(campaign.getSeq());
					mapCampaign.getGames().add(campaign.getGames().get(0));
				}
				return null;
			}
		});
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			Campaign campaign = (Campaign) mEntry.getValue();
			campaigns.add(campaign);
		}
		return campaigns;
	}

	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}

	protected Object populateObjectFromResultSet(ResultSet rs)
			throws SQLException {
		Campaign campaign = null;
		List<Game> games = new ArrayList<Game>();
		try {
			long seq = rs.getLong("seq");
			String name = rs.getString("name");
			String  description = rs.getString("description");			
			Timestamp createdOn = rs.getTimestamp("createdon");
			String validityDays = rs.getString("validitydays");
			boolean isEnable = rs.getBoolean("isenabled");
			long projectSeq = rs.getLong("projectseq");
			Timestamp lastModifiedDate = rs.getTimestamp("lastmodifieddate");
			Timestamp startDate = rs.getTimestamp("startdate");
			Timestamp validTillDate = rs.getTimestamp("validtilldate");
			String launchMessage=rs.getString("launchMessage");
			campaign = new Campaign();
			campaign.setSeq(seq);
			campaign.setName(name);
			campaign.setDescription(description);
			campaign.setCreatedOn(createdOn);
			campaign.setValidityDays(validityDays);
			campaign.setEnabled(isEnable);
			campaign.setStartDate(startDate);
			campaign.setValidTillDate(validTillDate);
			campaign.setLaunchMessage(launchMessage);
			Project project = null;
			campaign.setLastModifiedDate(lastModifiedDate);
			if (projectSeq != 0) {
				ProjectDataStoreI PDS = ApplicationContext
						.getApplicationContext().getDataStoreMgr()
						.getProjectDataStore();
				project = PDS.findBySeq(projectSeq);
			}
			campaign.setProject(project);
			try {
				long gameSeq = rs.getLong("gameSeq");
				String gameTitle = rs.getString("gameTitle");
				String gameDescription = rs.getString("gameDescription");
				boolean gameIsEnabled = rs.getBoolean("gameIsEnabled");
				long gameProjectSeq = rs.getLong("gameProjectSeq");
				Date gameLastModifiedDate = rs.getDate("gameLastModifiedDate");
				String imagePath = rs.getString("imagepath");
				Game game = new Game();
				game.setSeq(gameSeq);
				game.setTitle(gameTitle);
				game.setDescription(gameDescription);
				game.setEnable(gameIsEnabled);
				game.setProject(new Project(gameProjectSeq));
				game.setLastModifiedDate(gameLastModifiedDate);
				game.setImagePath(imagePath);
				games.add(game);
				campaign.setGames(games);
			} catch (Exception e) {
			}

		} catch (Exception e) {
			logger.error("CampaignDataStore populate method error", e);
		}
		return campaign;

	}

	@Override
	public void publishCampaign(long seq) {
		Object[] params = new Object[] { true, seq };
		persistenceMgr.excecuteUpdate(PUBLISH_CAMPAIGN, params);
	}

	@Override
	public boolean isAlreadyExist(String campaignName, long projectSeq) {
		Object[] params = new Object[] { campaignName, projectSeq };
		int count = persistenceMgr.executeCountQuery(COUNT_CAMPAIGN_BY_NAME,
				params);
		return count > 0;
	}

}
