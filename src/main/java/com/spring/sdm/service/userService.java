package com.spring.sdm.service;

import java.util.List;

import com.spring.sdm.vo.User;

public interface userService {
	public void saveUser(User user);
	public User getUserByUsername(String username);
	public List<User> fetchAllUser();
}
