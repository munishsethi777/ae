package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.GameTemplates;
import com.satya.Persistence.GameTemplatesDataStoreI;
import com.satya.Persistence.RowMapper;

public class GameTemplateDataStore implements GameTemplatesDataStoreI,RowMapper {
	Logger logger = Logger.getLogger(UserDataStore.class);
	private final static String SELECT = "select * from gametemplates ";
	private final static String SELECT_BY_SEQ = SELECT + "where seq = ?";
	private PersistenceMgr persistenceMgr;
	
	public GameTemplateDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	public List<GameTemplates> findAll(){
		return (List<GameTemplates>)persistenceMgr.executePSQuery(SELECT, null, this);
	}
	public GameTemplates findBySeq(long seq){
		Object [] params = new Object [] {seq};
		return (GameTemplates) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ, params, this);
	}
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	

	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		GameTemplates gameTemplates  = null;
		try{			
			long seq = rs.getLong("seq");
			String name = rs.getString("name");
			String path = rs.getString("path");
			String  imagePath = rs.getString("imagepath");			
			String description = rs.getString("description");
			int maxQuestions = rs.getInt("maxquestions");
			gameTemplates = new GameTemplates();
			gameTemplates.setSeq(seq);
			gameTemplates.setPath(path);
			gameTemplates.setName(name);
			gameTemplates.setImagePath(imagePath);
			gameTemplates.setDescription(description);
			gameTemplates.setMaxQuestions(maxQuestions);
		}catch(Exception e){
			logger.error("GamTemplatesDataStore populate method error",e);
		}
		return gameTemplates;

}
}
