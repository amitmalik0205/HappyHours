package com.qait.happyhours.rest.service;

import java.io.Serializable;

public class HappyHoursServiceResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String message;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
