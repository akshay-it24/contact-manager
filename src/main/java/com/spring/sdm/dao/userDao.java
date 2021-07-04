package com.spring.sdm.dao;

import java.util.List;

import com.spring.sdm.vo.User;

public interface userDao {
	public void saveUser(User user);
	public User getUserByUsername(String username);
	public List<User> fetchAllUser();
}
