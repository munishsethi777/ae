package validator;

import java.util.ArrayList;
import java.util.List;

public class AEValidationMessage {
	private Object object;
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	private String fieldName;
	private List<String> errorMessages;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	public AEValidationMessage(String fieldName){
		this.setFieldName(fieldName);
	}
	public void addErrorMessage(String message){
		if(this.getErrorMessages() == null){
			List<String> errMessages = new ArrayList<String>();
			this.setErrorMessages(errMessages);
		}
		this.getErrorMessages().add(message);
	}
}
