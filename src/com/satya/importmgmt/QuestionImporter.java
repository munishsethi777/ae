package com.satya.importmgmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import validator.AEValidationMessage;
import validator.AEValidatorFactory;

import com.satya.ApplicationContext;
import com.satya.ImportedSet;
import com.satya.RowImporterI;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.Managers.Impl.QuestionsMgr;
import com.satya.Persistence.QuestionDataStoreI;
import com.satya.Utils.ImportUtils;

public class QuestionImporter implements RowImporterI {

	private static final String[] columns = 
		new String[]{"QuestionTitle","Description","Hint","Answer1","Answer2","Answer3",
							"Answer4","Correct answer (please enter 1, 2, 3 or 4)","Points","NegativePoints","ExtraAttempts","MaxSecondsAllowed"};
	public List<Object> successImported;
	public List<AEValidationMessage> validationMessages;
	
	private Project currentProject;
	public QuestionImporter(Project project){
		currentProject = project;
	}
	public Questions saveData(Map<String, String> data) {
		int colIndex = 0;
		successImported = new ArrayList<Object>();
		validationMessages = new ArrayList<AEValidationMessage>();
		String colHeader = columns[colIndex];
		String title = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String description = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String hint = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String answer1 = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String answer2 = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String answer3 = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String answer4 = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String correctAnswerStr = data.get(colHeader);
		int correctAnsId = getIntegerValue(correctAnswerStr);
		colIndex++;
		colHeader = columns[colIndex];
		String points = data.get(colHeader);
		
		
		colIndex++;
		colHeader = columns[colIndex];
		String negativePoints = data.get(colHeader);
		
		
		colIndex++;
		colHeader = columns[colIndex];
		String extraAttemptsAllowed = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String maxSecondsAllowed = data.get(colHeader);
		
		Questions question = new Questions();
		question.setTitle(title);
		question.setDescription(description);
		question.setHint(hint);	
		
		QuestionAnswers answerObj1 = new QuestionAnswers();
		answerObj1.setAnswerTitle(answer1);
		
		QuestionAnswers answerObj2 = new QuestionAnswers();
		answerObj2.setAnswerTitle(answer2);
		
		QuestionAnswers answerObj3 = new QuestionAnswers();
		answerObj3.setAnswerTitle(answer3);		
		
		
		QuestionAnswers answerObj4 = new QuestionAnswers();
		answerObj4.setAnswerTitle(answer4);
		boolean isCorrect = false;
		if(correctAnsId == 1){
			isCorrect = true;
			answerObj1.setCorrect(isCorrect);
		}else if(correctAnsId == 2){
			isCorrect = true;
			answerObj2.setCorrect(isCorrect);
		}else if(correctAnsId == 3){
			isCorrect = true;
			answerObj3.setCorrect(isCorrect);
		}else if(correctAnsId == 4){
			isCorrect =true;
			answerObj4.setCorrect(isCorrect);
		}
		
		
		question.setPoints(getIntegerValue(points));
		question.setNegativePoints(getIntegerValue(negativePoints));
		question.setExtraAttemptsAllowed(getIntegerValue(extraAttemptsAllowed));
		question.setMaxSecondsAllowed(getIntegerValue(maxSecondsAllowed));
		question.setIsEnabled(true);
		question.setProject(currentProject);
		List<QuestionAnswers>questionAnswers= new ArrayList<QuestionAnswers>();
		questionAnswers.add(answerObj1);
		questionAnswers.add(answerObj2);
		questionAnswers.add(answerObj3);
		questionAnswers.add(answerObj4);
		answerObj1.setQuestion(question);
		answerObj2.setQuestion(question);
		answerObj3.setQuestion(question);
		answerObj4.setQuestion(question);
		
		question.setQuestionAnswers(questionAnswers);
		QuestionDataStoreI QDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getQuestionDataStore();
		
		
		AEValidatorFactory validator = ApplicationContext.getApplicationContext().getValidatorFactory();
        validationMessages = validator.validate(question);
        List<AEValidationMessage> validationMessages1 = validator.validate(answerObj1);
        List<AEValidationMessage> validationMessages2 = validator.validate(answerObj2);
        List<AEValidationMessage> validationMessages3 = validator.validate(answerObj3);
        List<AEValidationMessage> validationMessages4 = validator.validate(answerObj4);
        validationMessages.addAll(validationMessages1);
        validationMessages.addAll(validationMessages2);
        validationMessages.addAll(validationMessages3);
        validationMessages.addAll(validationMessages4);
        
        if(!isCorrect){
        	AEValidationMessage message= new AEValidationMessage("Correct answer");
        	message.addErrorMessage("Invalid Correct Answer.Please put 1,2,3 or 4 is Correct Answer Column");
        	validationMessages.add(message);
        }
        if(validationMessages.size() == 0){
        	QDS.Save(question);
        	successImported.add(question);
        	return question;
        }
		return null;
	}
	@Override
	public List<Object> getSuccessImportedObjects() {
		return successImported;
	}
	@Override
	public List<AEValidationMessage> getValidationMessages() {
		return validationMessages;
	}
	
	private int getIntegerValue(String val){
		if(val != null && !val.equals("")){
			try{
				Double n = Double.parseDouble(val);
				return n.intValue();
			}catch(NumberFormatException e){
				return 0;
			}
		}
		return 0;
	}
	
	public JSONObject importFromXls(HttpServletRequest request, HttpServletResponse response)throws Exception{
		JSONArray jsonArr = new JSONArray();
		ImportUtils importUtils = new ImportUtils(this);
		ImportedSet importedSet = importUtils.importFromXls(request, response);
		JSONObject mainJsonObject = new JSONObject();
		List<Object>objList = importedSet.getObjList(); 
		for(Object obj: objList){ 
			Questions question = (Questions)obj;
			jsonArr.put(QuestionsMgr.toJson(question));
		}
		try{
			mainJsonObject.put("jsonArr", jsonArr);
			mainJsonObject.put("hasErrors", importedSet.getHasErrors());
			mainJsonObject.put("failedRowCount", importedSet.getFailedRowCount());
		}catch(Exception e){
			
		}
		return mainJsonObject;
	}
}
