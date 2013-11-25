package validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.satya.AEObjectsMetaRegistry;
import com.satya.ApplicationContext;
import com.satya.BusinessObjects.AEObjectsMeta;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;
import com.satya.Persistence.UserDataStoreI;
import com.sun.org.apache.bcel.internal.generic.Type;

public class AEValidatorFactory {
	Logger logger = Logger.getLogger(AEValidatorFactory.class);
	
	public List<AEValidationMessage> validate(Object obj){
		List<AEValidationMessage> validationMessages = new ArrayList<AEValidationMessage>();
		
		AEObjectsMetaRegistry metaRegistry = ApplicationContext.getApplicationContext().getMetaRegistry();
		HashMap<String, AEObjectsMeta> objectMetaMap = metaRegistry.getMeta(obj.getClass());
		Iterator it = objectMetaMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String fieldName = (String)pairs.getKey();
	        AEObjectsMeta meta = (AEObjectsMeta)pairs.getValue();
	        Object fieldValue = getPropertyVal(obj,fieldName);
	        AEValidationMessage validationMessage = validateInternal(meta,fieldValue);
	        if(validationMessage.getErrorMessages()  != null && validationMessage.getErrorMessages().size() > 0){
	        	validationMessage.setObject(obj);
		        validationMessages.add(validationMessage);
	        }
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
		return validationMessages;
		
	}

	private AEValidationMessage validateInternal(AEObjectsMeta meta,Object value){
		AEValidationMessage message= new AEValidationMessage(meta.getFieldName());
		if(meta.isFieldNullable()==false){
			if(value instanceof  Integer){
				if(value.equals(0)){
					message.addErrorMessage(meta.getFieldName() +" Should be numeric and greater than 0");
					return message;
				}
			}
			if(value == null){
				message.addErrorMessage(meta.getFieldName() +" is a Required field");
				return message;
			}
		}else{
			if(value == null){
				return message;
			}
		}
		if(meta.getFieldMaxLength()!= null && meta.getFieldMaxLength() > 0){
			if(value.toString().length() > meta.getFieldMaxLength()){
				message.addErrorMessage(meta.getFieldName() +" can not be longer than "+ meta.getFieldMaxLength() +" characters");
			}
		}
		if(meta.isUnique()){
			boolean isExists = false;
			if (value.getClass().equals(User.class)){
				UserDataStoreI UDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
//				Project project = ApplicationContext.getApplicationContext().getAdminWorkspaceProject(request);
				User  user = UDS.findByUserNameAndProject((String)value,1);
				if(user != null){
					isExists = true;
				}
			}
			//check if the related object exists already in DB
			if(isExists){
				message.addErrorMessage(meta.getFieldClass().getCanonicalName() +" with "+ meta.getFieldName() + value +" already exists. Please enter some other value");
			}
		}
		return message;	
	}

	private Object getPropertyVal(Object obj, String fieldName){
		Object propertyVal = null;
		try{
			propertyVal = PropertyUtils.getProperty(obj, fieldName);
		}catch(Exception e){
			logger.error("Error occured while getting property value",e);
			return null;
		}
		return propertyVal;
	}
}
