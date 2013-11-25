package com.satya;

import java.util.List;
import java.util.Map;

import validator.AEValidationMessage;

public interface RowImporterI {
	public Object saveData(Map<String, String> data);
	public List<Object> getSuccessImportedObjects();
	public List<AEValidationMessage> getValidationMessages();
}
