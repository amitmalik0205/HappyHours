package com.qait.happyhours.enums;

public enum UserStatus {

	USER_ID_EXISTS("UserId already exists"), 
	EMAIL_EXISTS("Email already exists"), 
	SUCCESSFUL("User regestration is successful"), 
	UNSUCCESSFUL("User regestration is unsuccessful");

	String value;

	private UserStatus(String val) {
		this.value = val;
	}
}
