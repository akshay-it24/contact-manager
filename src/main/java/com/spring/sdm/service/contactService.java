package com.spring.sdm.service;

import java.util.List;

import com.spring.sdm.vo.Contact;

public interface contactService {
	public List<Contact> findContactByUser(int userid, int start, int n);
	public int findContactListSize(int userid);
	public Contact findContactById(int id);
	public void deleteContact(Contact contact);
	public void saveContact(Contact contact);
	public List<Contact> searchContact(String name,int id);
}
