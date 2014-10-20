package com.qait.happyhours.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.qait.happyhours.dao.CategoryDao;
import com.qait.happyhours.domain.Category;
import com.qait.happyhours.domain.Deal;
import com.qait.happyhours.util.HappyHoursUtil;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoImpl<Category, Long> implements CategoryDao {

	private static final Logger logger = Logger.getLogger(CategoryDaoImpl.class);
	
	@Override
	public List<Category> getCategoryList() {
		List<Category> list = null;
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from Category";
			Query query = session.createQuery(queryString);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public Category loadCategoryID(Long categoryID) {
		Category category = null;
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			category = (Category) session.load(Category.class, categoryID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return category;
	}
}
