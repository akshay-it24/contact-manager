package com.spring.sdm.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.spring.sdm.service.contactService;
import com.spring.sdm.service.userService;
import com.spring.sdm.vo.Contact;
import com.spring.sdm.vo.User;

@RestController
public class searchController {
	
	@Autowired
	private userService userService;
	@Autowired
	private contactService contactService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchContact(@PathVariable("query") String query,Principal principal){
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		List<Contact> ls=this.contactService.searchContact(query, user.getId());
		return ResponseEntity.ok(ls);
	}
}
