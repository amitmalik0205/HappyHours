package com.qait.happyhours.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.qait.happyhours.dao.UserDao;
import com.qait.happyhours.domain.User;
import com.qait.happyhours.util.HappyHoursUtil;

@Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

	private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

	public boolean saveUser(User user) {
		boolean userSaved = true;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(user);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			userSaved = false;
			e.printStackTrace();
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.flush();
			session.close();
		}
		return userSaved;
	}

	public User getUserByEmail(String email) {
		Session session = null;
		User user = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from User where email = :email";
			Query query = session.createQuery(queryString);
			query.setString("email", email);
			user = (User) query.uniqueResult();
		} catch (Exception e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return user;
	}

	public User getUserByUserId(String id) {
		Session session = null;
		User user = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from User where userID = :id";
			Query query = session.createQuery(queryString);
			query.setString("id", id);
			user = (User) query.uniqueResult();
		} catch (Exception e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return user;
	}

	public User authenticateUser(String userId, String password) {
		Session session = null;
		User user = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from User where userName = :userId and password = :pwd";
			Query query = session.createQuery(queryString);
			query.setString("userId", userId);
			query.setString("pwd", password);
			user = (User) query.uniqueResult();
		} catch (Exception e) {
			logger.info("Login falied for userId=" + userId + " and Password="
					+ "" + password);
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return user;
	}

	public List<Object[]> getMatchingUserID(String str) {
		List<Object[]> list = new ArrayList<Object[]>();
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "Select u.userID from User u where u.userID like '"
					+ str + "%'";
			Query query = session.createQuery(queryString);
			list = query.list();
		} catch (Exception e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public boolean updateUser(User updatedUser) {
		boolean userUpdated = true;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(updatedUser);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			userUpdated = false;
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.flush();
			session.close();
		}
		return userUpdated;
	}

	@Override
	public Boolean checkAuthorizationByToken(String authorizationValue) {
		boolean isUserAuthorized = true;
		Session session = null;
		User user = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from User where token = :token";
			Query query = session.createQuery(queryString);
			query.setString("token", authorizationValue);
			user = (User) query.uniqueResult();
			if(user == null){
				isUserAuthorized=false;
			}
		} catch (Exception e) {
			logger.info("Login falied for token="
					+ "" + authorizationValue);
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
		} finally {
			session.close();
		}
		return isUserAuthorized;
	}
}
