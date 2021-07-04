package com.spring.sdm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.spring.sdm.service.userService;
import com.spring.sdm.vo.User;

public class userDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private userService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user=this.userService.getUserByUsername(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Counld not found User");
		}
		
		customUserDetail customUserDetail=new customUserDetail(user);
		return customUserDetail;
	}

}
