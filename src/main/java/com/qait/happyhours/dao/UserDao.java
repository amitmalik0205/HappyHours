package com.qait.happyhours.dao;

import java.util.List;

import com.qait.happyhours.domain.User;

public interface UserDao {

	public boolean saveUser(User user);
	
	public User getUserByEmail(String email);
	
	public User getUserByUserId(String userId);
	
	public User authenticateUser(String userId, String password);
	
	public List<Object[]> getMatchingUserID(String str);

	public boolean updateUser(User updatedUser);

	public Boolean checkAuthorizationByToken(String authorizationValue);
}
