package com.satya.xmlObjects;

public class Success {
	public double getSuccessPercentage() {
		return successPercentage;
	}

	public void setSuccessPercentage(double successPercentage) {
		this.successPercentage = successPercentage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private double successPercentage;
	private String message;

}
