package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Campaign;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.Result;
import com.satya.BusinessObjects.Result.ResultQuestion;
import com.satya.BusinessObjects.Set;
import com.satya.BusinessObjects.User;
import com.satya.BusinessObjects.UserGroup;
import com.satya.Persistence.ResultsDataStoreI;
import com.satya.Persistence.RowMapper;

public class ResultsDataStore implements ResultsDataStoreI,RowMapper{
	
	Logger logger = Logger.getLogger(ResultsDataStore.class);
	private PersistenceMgr persistenceMgr;
	
	
	private final static String SAVE = "INSERT into results(userseq,gameseq,score,timetaken,createdon,campaignseq) " +
			" values(?,?,?,?,?,?)";
	private final static String SAVE_DETAIL = "INSERT into resultquestions(resultseq,questionseq,timetaken,selectedanswerseq,points,attempts,negativepoints) "+
			" values(?,?,?,?,?,?,?)";
	
	private final static String FIND_RESULTS_BY_CAMPAIGN ="select Distinct results.seq,results.createdon, Users.name username, sets.name as setname, games.title, " +
			" results.score, results.timetaken, usergroups.name as usergroup, campaigns.name as campaign from results" +
			" left join users on users.seq = results.userseq" +
			" left join games on games.seq = results.gameseq" +
			" left join campaignsets on campaignsets.seq = results.campaignsetseq" +
			" left join sets on sets.seq = campaignsets.setseq" +
			" left join usergroupusers on usergroupusers.userseq = users.seq" +
			" left join usergroups on usergroups.seq = usergroupusers.usergroupseq" +
			" left join campaigns on campaigns.seq = campaignsets.campaignseq" + 
			" where campaigns.seq = ?";
	private final static String FIND_RESULTS_BY_CAMPAIGN_GAME_USER = "select * from results where campaignseq=? and gameseq=? and userseq=?";

	
	public ResultsDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	
	@Override
	public void Save(Result result) {
		String SQL = SAVE;
		try{
			Object[] params  = new Object[6];
			
					params[0]= result.getUserId();
					params[1] = result.getGameId();
					params[2] = result.getTotalScore();
					params[3] = result.getTotalTime();
					params[4] = new Date();
					params[5] = result.getCampaign().getSeq();
			persistenceMgr.excecuteUpdate(SQL, params);
			result.setSeq(persistenceMgr.getLastUpdatedSeq());
			SaveResultQuestions(result);	
		}catch(Exception e){
			logger.error("Error during Save Results",e);
		}
	}
	
	private void SaveResultQuestions(Result result){
		for(ResultQuestion resultQuestions : result.getQuestions()){
			try{
				Object[] params  = new Object[7];
				
						params[0] = result.getSeq();
						params[1] = Integer.parseInt(resultQuestions.getId());
						params[2] = Integer.parseInt(resultQuestions.getTimeTaken());
						params[3] = Integer.parseInt(resultQuestions.getSelectedAnswerId());
						params[4] = Integer.parseInt(resultQuestions.getPoints());
						if(resultQuestions.getAttempts()!=null){
							params[5] = Integer.parseInt(resultQuestions.getAttempts());
						}else{
							params[5] = null;
						}
						if(resultQuestions.getNegativePoints()!=null){
							params[6] = Integer.parseInt(resultQuestions.getNegativePoints());
						}else{
							params[6] = null;
						}
						
						
				persistenceMgr.excecuteUpdate(SAVE_DETAIL, params);
				result.setSeq(persistenceMgr.getLastUpdatedSeq());
				
			}catch(Exception e){
				logger.error("Error during Save Result Questions",e);
			}
		}
		
	}

	@Override
	public List<Result> findByCampaign(long campaignSeq) {
		Object [] params = new Object [] {campaignSeq};
		return (List<Result>)persistenceMgr.executePSQuery(FIND_RESULTS_BY_CAMPAIGN, params, this);	
	}

	@Override
	public Result findByCampaignAndGameAndUser(long campaignSeq, long gameSeq,
			long userSeq) {
		Object [] params = new Object [] {campaignSeq,gameSeq,userSeq};
		return (Result)persistenceMgr.executeSingleObjectQuery(FIND_RESULTS_BY_CAMPAIGN_GAME_USER, params, this);	
	}
	@Override
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		Result result = new Result();
		try{			
			long seq = rs.getLong("seq");
			String userName = rs.getString("username");
			String setName = rs.getString("setname");
			String gameTitle = rs.getString("title");
			int score = rs.getInt("score");
			int timetaken = rs.getInt("timetaken");
			String userGroupName = rs.getString("usergroup");
			String campaignName = rs.getString("campaign");
			Date createdOn = rs.getDate("createdon");
			
			
			result.setSeq(seq);
			User user = new User();
			user.setName(userName);
			result.setUser(user);
			Set set = new Set();
			set.setName(setName);
			//result.setSet(set);
			Game game = new Game();
			game.setTitle(gameTitle);
			result.setGame(game);
			result.setTotalScore(String.valueOf(score));
			result.setTotalTime(String.valueOf(timetaken));
			UserGroup userGroup = new UserGroup();
			userGroup.setName(userGroupName);
			result.setUserGroup(userGroup);
			Campaign campaign = new Campaign();
			campaign.setName(campaignName);
			result.setCampaign(campaign);
			result.setCreatedOn(createdOn);
			
		}catch(Exception e){
			logger.error("ResultDatastore populate method error",e);
		}
		
		return result;
		
	}


}
