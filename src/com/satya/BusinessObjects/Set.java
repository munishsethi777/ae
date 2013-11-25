package com.satya.BusinessObjects;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.IConstants;
	
public class Set {
	public static final String ID = "seq";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CREATEDON = "createdOn";
	
	private long seq;
	private String name;
	private String description;
	private Date createdOn;
	private Project project;
	private List<Game>games;
	private Date lastModifiedDate;
	private boolean isEnable;
	
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}
	public static JSONObject toJson(Set set){		
		JSONObject json = new JSONObject();
		try{
			json.put(ID, set.getSeq());
			json.put(NAME, set.getName());
			json.put(DESCRIPTION, set.getDescription());
			json.put(CREATEDON, set.getCreatedOn());
			json.put(IConstants.LAST_MODIFIED_DATE, set.getLastModifiedDate());
			json.put(IConstants.IS_ENABLED, set.isEnable());
			if(set.getGames() != null){
				JSONArray jsonGames = new JSONArray();
				for(Game game : set.getGames()){
					JSONObject gameJson = new JSONObject();
					gameJson.put(ID, game.getSeq());
					gameJson.put(NAME, game.getTitle());
					gameJson.put(DESCRIPTION, game.getDescription());
					if(game.getGameTemplate() != null){
						gameJson.put("gameTemplateName", game.getGameTemplate().getName());
						gameJson.put("gameTemplateImagePath", game.getGameTemplate().getImagePath());
					}
					if(game.getGameResult() != null){
						gameJson.put("totalScore",game.getGameResult().getTotalScore());
						gameJson.put("totalTime", game.getGameResult().getTotalTime());
						gameJson.put("resultDated", game.getGameResult().getCreatedOn());
					}
					jsonGames.put(gameJson);
				}
				json.put("games", jsonGames);
			}
		}catch( Exception e){
			
		}
		return json;
	}
	
	public static JSONArray getJSONArray(List<Set> sets){
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for(Set set: sets){ 
			jsonArr.put(Set.toJson(set));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
		}catch(Exception e){
			
		}
		return jsonArr;
	}
}
