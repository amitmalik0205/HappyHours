package com.qait.happyhours.rest.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ImageData {

	private CommonsMultipartFile file;

	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
}
