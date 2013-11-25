package com.satya.meta;

import java.util.HashMap;

import com.satya.BusinessObjects.AEObjectsMeta;
/*
 seq int notnull
title varchar - 500chars notnull  unique
description varchat - 1000chars nullable
hint varchar - 1000chars notnull
points int notnull
projectseq int notnull
createon datetime
lastmodified datetime
isenabled bit
negativepoints int nullable
maxsecondsallowed int nullable
extraattemptsallowed int nullable
 */
public class QuestionMeta {
	private static final String POINTS = "points";
	private static final String DESCRIPTION = "description";
	private static final String TITLE = "title";

	public static HashMap<String, AEObjectsMeta> initMeta(){
		HashMap<String, AEObjectsMeta> questionMetaMap = new HashMap<String, AEObjectsMeta>();
		questionMetaMap.put(TITLE, new AEObjectsMeta(TITLE, String.class, 500, false,true));
		questionMetaMap.put(DESCRIPTION, new AEObjectsMeta(DESCRIPTION, String.class, 1000, true,false));
		questionMetaMap.put(POINTS, new AEObjectsMeta(POINTS,Integer.class,null,false,false));
		return questionMetaMap;
	}
}
