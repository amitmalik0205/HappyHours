package com.qait.happyhours.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qait.happyhours.dao.CategoryDao;
import com.qait.happyhours.domain.Category;
import com.qait.happyhours.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public List<Category> getCategoryList() {
		return categoryDao.getCategoryList();
	}

	@Override
	public Category loadCategoryID(Long categoryID) {
		return categoryDao.loadCategoryID(categoryID);
	}
}
