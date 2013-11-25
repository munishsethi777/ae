package com.satya;

import java.util.List;

public class ImportedSet {
	
	private List<Object>objList;
	public List<Object> getObjList() {
		return objList;
	}
	public void setObjList(List<Object> objList) {
		this.objList = objList;
	}
	private boolean hasErrors;
	public boolean getHasErrors() {
		return hasErrors;
	}
	public void setHasErrors(boolean error) {
		this.hasErrors = error;
	}
	
	private int failedRowCount;
	
	public int getFailedRowCount() {
		return failedRowCount;
	}
	public void setFailedRowCount(int failedRowCount) {
		this.failedRowCount = failedRowCount;
	}
	
	
}
