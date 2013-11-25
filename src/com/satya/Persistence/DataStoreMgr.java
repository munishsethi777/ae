package com.satya.Persistence;

public interface DataStoreMgr {

	public UserDataStoreI getUserDataStore();
	public ProjectDataStoreI getProjectDataStore();
	public SetDataStoreI getSetDataStore();
	public GameDataStoreI getGameDataStore();
	public UserGroupDataStoreI getUserGroupDataStore();
	public AdminDataStoreI getAdminDataStore();
	public CampaignDataStoreI getCampaignDataStore();
	public QuestionDataStoreI getQuestionDataStore();
	public GameTemplatesDataStoreI getGameTemplateDataStore();
	public QuestionAnswersDataStoreI getQuestionAnswersDataStore();
	public ResultsDataStoreI getResultsDataStore();
}
