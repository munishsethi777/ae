package com.satya.BusinessObjects;

import java.util.Date;
import java.util.List;

public class Result {
	
	private long seq;
	private String userId;
	private String gameId;
	private String totalScore;
	private String totalTime;
	private List<ResultQuestion> questions;
	private Date createdOn;
	
	
	private User user;
	private Game game;
	private Campaign campaign;
	private UserGroup userGroup;
	
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public List<ResultQuestion> getQuestions() {
		return questions;
	}
	public void setQuestions(List<ResultQuestion> questions) {
		questions = questions;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public Campaign getCampaign() {
		return campaign;
	}
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
	
	public class ResultQuestion{
		private String id;
		private String timeTaken;
		private String selectedAnswerId;
		private String points;
		private String attempts;
		private String negativePoints;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTimeTaken() {
			return timeTaken;
		}
		public void setTimeTaken(String timeTaken) {
			this.timeTaken = timeTaken;
		}
		public String getSelectedAnswerId() {
			return selectedAnswerId;
		}
		public void setSelectedAnswerId(String selectedAnswerId) {
			this.selectedAnswerId = selectedAnswerId;
		}
		public String getPoints() {
			return points;
		}
		public void setPoints(String points) {
			this.points = points;
		}
		public String getAttempts() {
			return attempts;
		}
		public void setAttempts(String attempts) {
			this.attempts = attempts;
		}
		public String getNegativePoints() {
			return negativePoints;
		}
		public void setNegativePoints(String negativePoints) {
			this.negativePoints = negativePoints;
		}
		

	}


}

