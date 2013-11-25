package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.GameTemplates;

public interface GameTemplatesDataStoreI {
	public List<GameTemplates> findAll();
	public GameTemplates findBySeq(long seq);
}
