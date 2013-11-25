package com.satya.Persistence.Impl;

import com.satya.Persistence.AdminDataStoreI;
import com.satya.Persistence.CampaignDataStoreI;
import com.satya.Persistence.DataStoreMgr;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.GameTemplatesDataStoreI;
import com.satya.Persistence.ProjectDataStoreI;
import com.satya.Persistence.QuestionAnswersDataStoreI;
import com.satya.Persistence.QuestionDataStoreI;
import com.satya.Persistence.ResultsDataStoreI;
import com.satya.Persistence.SetDataStoreI;
import com.satya.Persistence.UserDataStoreI;
import com.satya.Persistence.UserGroupDataStoreI;


public class JDBCDataStoreMgr implements DataStoreMgr {

	private UserDataStoreI userDataStore;
	private ProjectDataStoreI projectDataStore;
	private SetDataStoreI setDataStore;
	private GameDataStoreI gameDataStore;
	private UserGroupDataStoreI userGroupDataStore;
	private AdminDataStoreI adminDataStore;
	private CampaignDataStoreI campaignDataStore;
	public QuestionDataStoreI questionDataStore;
	public GameTemplatesDataStoreI gameTemplateDataStore;
	public QuestionAnswersDataStoreI questionAnswersDataStore;
	public ResultsDataStoreI resultsDataStore;
	
	

	public JDBCDataStoreMgr(PersistenceMgr persistenceMgr){
		userDataStore = new UserDataStore(persistenceMgr);
		projectDataStore = new ProjectDataStore(persistenceMgr);
		setDataStore = new SetDataStore(persistenceMgr);
		gameDataStore = new GameDataStore(persistenceMgr);
		userGroupDataStore = new UserGroupDataStore(persistenceMgr);
		adminDataStore = new AdminDataStore(persistenceMgr);
		campaignDataStore = new CampaignDataStore(persistenceMgr);
		questionDataStore = new QuestionDataStore(persistenceMgr);
		gameTemplateDataStore = new GameTemplateDataStore(persistenceMgr);
		questionAnswersDataStore = new QuestionAnswersDataStore(persistenceMgr);
		resultsDataStore = new ResultsDataStore(persistenceMgr);
	}

	@Override
	public UserDataStoreI getUserDataStore() {
		return userDataStore;
	}
	@Override
	public ProjectDataStoreI getProjectDataStore() {
		return projectDataStore;
	}
	
	@Override
	public SetDataStoreI getSetDataStore() {
		return setDataStore;
	}
	
	@Override
	public GameDataStoreI getGameDataStore() {
		return gameDataStore;
	}
	
	@Override
	public UserGroupDataStoreI getUserGroupDataStore() {
		return userGroupDataStore;
	}
	@Override
	public AdminDataStoreI getAdminDataStore() {
		return adminDataStore;
	}
	@Override
	public CampaignDataStoreI getCampaignDataStore() {
		return campaignDataStore;
	}
	@Override
	public QuestionDataStoreI getQuestionDataStore(){
		return questionDataStore;
	}
	@Override
	public GameTemplatesDataStoreI getGameTemplateDataStore(){
		return gameTemplateDataStore;
	}
	@Override
	public QuestionAnswersDataStoreI getQuestionAnswersDataStore(){
		return questionAnswersDataStore;
	}

	@Override
	public ResultsDataStoreI getResultsDataStore() {
		return resultsDataStore;
	}
	

}
