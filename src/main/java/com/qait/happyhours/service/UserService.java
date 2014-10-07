package com.qait.happyhours.service;

import java.util.List;

import javax.ws.rs.core.Response;

import com.qait.happyhours.domain.User;

public interface UserService {

	public Response saveUser(User user);
	
	public User authenticateUser(String userId, String password);
	
	public List<Object[]> getMatchingUserID(String str);
	
	public User getUserByUserId(String userId);

	public boolean updateUser(User updatedUser);

	public Boolean checkAuthorizationByToken(String authorizationValue);
}
