package com.qait.happyhours.dao;

import java.util.List;

import com.qait.happyhours.domain.Category;

public interface CategoryDao {
	
	public List<Category> getCategoryList();

	public Category loadCategoryID(Long categoryID);
}
