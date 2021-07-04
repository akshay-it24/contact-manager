package com.spring.sdm.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.sdm.dao.contactDao;
import com.spring.sdm.vo.Contact;

@Service
@Transactional
public class contactServiceImpl implements contactService {

	@Autowired
	private contactDao contactDao;
	
	public List<Contact> findContactByUser(int userid,int start,int n) {
		List<Contact> ls=this.contactDao.findContactByUser(userid,start,n);
		return ls;
	}

	@Override
	public int findContactListSize(int userid) {
		int size=this.contactDao.findContactListSize(userid);
		return size;
	}

	@Override
	public Contact findContactById(int id) {
		Contact contact=this.contactDao.findContactById(id);
		return contact;
	}

	@Override
	public void deleteContact(Contact contact) {
		this.contactDao.deleteContact(contact);
	}

	@Override
	public void saveContact(Contact contact) {
		// TODO Auto-generated method stub
		this.contactDao.saveContact(contact);
	}

	@Override
	public List<Contact> searchContact(String name, int id) {
		// TODO Auto-generated method stub
		List<Contact> ls=this.contactDao.searchContact(name, id);
		return ls;
	}

}
