package com.qait.happyhours.service;

import java.util.List;

import com.qait.happyhours.domain.Category;

public interface CategoryService {

	public List<Category> getCategoryList();
	
	public Category loadCategoryID(Long categoryID);
}
