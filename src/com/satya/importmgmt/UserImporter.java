package com.satya.importmgmt;

import java.util.ArrayList;
import java.util.Date;
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
import com.satya.BusinessObjects.User;
import com.satya.Managers.Impl.UserMgr;
import com.satya.Persistence.UserDataStoreI;
import com.satya.Utils.ImportUtils;

public class UserImporter implements RowImporterI{
	
	private Project currentProject;
	public UserImporter(Project project){
		currentProject = project;
	}
	private static final String[] columns = 
		new String[]{"Name","Email","UserName","Password","Mobile","Location","Department"};
	public List<Object> successImportedUsers;
	public List<AEValidationMessage> validationMessages;
	@Override
	public User saveData(Map<String, String> data) {
		successImportedUsers = new ArrayList<Object>();
		validationMessages = new ArrayList<AEValidationMessage>();
		
		int colIndex = 0;
		String colHeader = columns[colIndex];
		String name = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String email = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String userName = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String password = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String mobile = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String location = data.get(colHeader);
		
		colIndex++;
		colHeader = columns[colIndex];
		String department = data.get(colHeader);
		


		
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setLocation(location);
		user.setCreatedOn(new Date());
		user.setLastModifiedDate(new Date());
		user.setUsername(userName);
		user.setDepartment(department);	
		user.setEnabled(true);
		user.setProject(currentProject);
		UserDataStoreI UDS = ApplicationContext.getApplicationContext().
							getDataStoreMgr().getUserDataStore();
		AEValidatorFactory validator = ApplicationContext.getApplicationContext().getValidatorFactory();
       validationMessages = validator.validate(user);
        if(validationMessages.size() == 0){
        	UDS.Save(user);
        	successImportedUsers.add(user);
        	return user;
        }
		return null;
	}
	@Override
	public List<Object> getSuccessImportedObjects() {
		return successImportedUsers;
	}
	@Override
	public List<AEValidationMessage> getValidationMessages() {
		return validationMessages;
	}
	
	public JSONObject importFromXls(HttpServletRequest request, HttpServletResponse response)throws Exception{
		JSONArray jsonArr = new JSONArray();
		ImportUtils importUtils = new ImportUtils(this);
		ImportedSet importedSet = importUtils.importFromXls(request, response);
		JSONObject mainJsonObject = new JSONObject();
		List<Object>objList = importedSet.getObjList(); 
		for(Object obj: objList){ 
			User user = (User)obj;
			jsonArr.put(UserMgr.toJson(user));
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
