package com.spring.sdm.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.sdm.dao.*;
import com.spring.sdm.vo.User;

@Service
@Transactional
public class userServiceImpl implements userService {
	
	@Autowired
	private userDao userDao;
	
	@Override
	public void saveUser(User user) {
		this.userDao.saveUser(user);
	}

	@Override
	public User getUserByUsername(String username) {
		User user=this.userDao.getUserByUsername(username);
		return user;
	}

	@Override
	public List<User> fetchAllUser() {
		List<User> ls=this.userDao.fetchAllUser();
		return ls;
	}

}
