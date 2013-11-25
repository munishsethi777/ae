package com.satya;

import java.util.Date;
import java.util.HashMap;

import com.satya.BusinessObjects.AEObjectsMeta;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.BusinessObjects.User;
import com.satya.meta.QuestionAnswerMeta;
import com.satya.meta.QuestionMeta;

public class AEObjectsMetaRegistry {
	private HashMap<Class, HashMap<String, AEObjectsMeta>> AEMeta;
	public AEObjectsMetaRegistry(){
		HashMap<String, AEObjectsMeta> userObjectMetaMap = new HashMap<String, AEObjectsMeta>();
		userObjectMetaMap.put("name", new AEObjectsMeta("name", String.class, 150, false,false));
		userObjectMetaMap.put("email", new AEObjectsMeta("email", String.class, 100, false,false));
		userObjectMetaMap.put("mobile", new AEObjectsMeta("mobile", String.class, 20, false,false));
		userObjectMetaMap.put("location", new AEObjectsMeta("location", String.class, 150, false,false));
		userObjectMetaMap.put("username", new AEObjectsMeta("username", String.class, 100, false,true));
		userObjectMetaMap.put("password", new AEObjectsMeta("password", String.class, 100, false,false));
		userObjectMetaMap.put("department", new AEObjectsMeta("department", String.class, 200, true,false));
		
		
		AEMeta = new HashMap<Class, HashMap<String,AEObjectsMeta>>();
		AEMeta.put(User.class, userObjectMetaMap);
		AEMeta.put(Questions.class, QuestionMeta.initMeta());
		AEMeta.put(QuestionAnswers.class, QuestionAnswerMeta.initMeta());
		
	}
	public AEObjectsMeta getMeta(Class objClass, String fieldName){
		return this.AEMeta.get(objClass).get(fieldName);
	}
	public HashMap<String, AEObjectsMeta> getMeta(Class objClass){
		return this.AEMeta.get(objClass);
	}
}
