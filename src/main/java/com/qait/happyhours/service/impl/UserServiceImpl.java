package com.qait.happyhours.service.impl;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qait.happyhours.dao.UserDao;
import com.qait.happyhours.domain.User;
import com.qait.happyhours.rest.service.HappyHoursServiceResponse;
import com.qait.happyhours.service.UserService;
import com.qait.happyhours.util.HappyHoursPropertiesFileReaderUtil;
import com.qait.happyhours.util.HappyHoursUtil;

@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	public Response saveUser(User user) {
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();
		
		user.setToken(HappyHoursUtil.getAuthToken());
		response.setCode("rsgisterUser001");
		
		String status = HappyHoursPropertiesFileReaderUtil
				.getPropertyValue("rsgisterUser001");
		
		if (userDao.getUserByUserId(user.getUserName()) != null) {
			response.setCode("rsgisterUser003");
			status = HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("rsgisterUser003");
		} else if (userDao.getUserByEmail(user.getEmail()) != null) {
			response.setCode("rsgisterUser004");
			status = HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("rsgisterUser004");
		} else if (!userDao.saveUser(user)) {

			response.setCode("rsgisterUser002");
			status = HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("rsgisterUser002");
		} else {
			response.setToken(user.getToken());
		}

		response.setMessage(status);
		return Response.status(200).entity(response).build();
	}

	public User authenticateUser(String userId, String password) {
		return userDao.authenticateUser(userId, password);
	}

	public List<Object[]> getMatchingUserID(String str) {
		return userDao.getMatchingUserID(str);
	}

	public User getUserByUserId(String userId) {
		return userDao.getUserByUserId(userId);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean updateUser(User updatedUser) {
		return userDao.updateUser(updatedUser);
	}

	@Override
	public Boolean checkAuthorizationByToken(String authorizationValue) {
		return userDao.checkAuthorizationByToken(authorizationValue);
	}
}
