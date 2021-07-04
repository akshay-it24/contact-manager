package com.spring.sdm.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.sdm.vo.Contact;


@Repository
public class contactDaoImpl implements contactDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Contact> findContactByUser(int userid,int start,int n) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Contact where user="+userid);
		
		
		query.setFirstResult(start);
		query.setMaxResults(n);
		List<Contact> ls=query.list();
		return ls;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int findContactListSize(int userid) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Contact where user="+userid);
		List<Contact> ls=query.list();
		return ls.size();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Contact findContactById(int id) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Contact where cid="+id);
		List<Contact> ls=query.list();
		return ls.get(0);
	}

	@Override
	public void deleteContact(Contact contact) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(contact);
	}

	@Override
	public void saveContact(Contact contact) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(contact);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Contact> searchContact(String name,int id){
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Contact where name LIKE '%"+name+"%' and user.id="+id);
		List<Contact> ls=query.list();
		return ls;
	}

}
