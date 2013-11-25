package com.satya.meta;

import java.util.HashMap;

import com.satya.BusinessObjects.AEObjectsMeta;

public class QuestionAnswerMeta {
	private static final String TITLE = "answerTitle";
	public static HashMap<String, AEObjectsMeta> initMeta(){
		HashMap<String, AEObjectsMeta> questionAnsMetaMap = new HashMap<String, AEObjectsMeta>();
		questionAnsMetaMap.put(TITLE, new AEObjectsMeta(TITLE, String.class, 500, false,false));
		return questionAnsMetaMap;
	}
}
