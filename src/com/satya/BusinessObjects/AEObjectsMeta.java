package com.satya.BusinessObjects;

public class AEObjectsMeta {

	private String fieldName;
	private Class fieldClass;
	private Integer fieldMaxLength;
	private boolean isFieldNullable;
	private boolean isUnique;
	
	public AEObjectsMeta(String fieldName, Class fieldClass,
			Integer fieldMaxLength, boolean isFieldNullable,boolean isUnique) {
		this.setFieldName(fieldName);
		this.setFieldClass(fieldClass);
		this.setFieldMaxLength(fieldMaxLength);
		this.setFieldNullable(isFieldNullable);
		this.setUnique(isUnique);
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(Class fieldClass) {
		this.fieldClass = fieldClass;
	}

	public Integer getFieldMaxLength() {
		return fieldMaxLength;
	}

	public void setFieldMaxLength(Integer fieldMaxLength) {
		this.fieldMaxLength = fieldMaxLength;
	}

	public boolean isFieldNullable() {
		return isFieldNullable;
	}

	public void setFieldNullable(boolean isFieldNullable) {
		this.isFieldNullable = isFieldNullable;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
}
