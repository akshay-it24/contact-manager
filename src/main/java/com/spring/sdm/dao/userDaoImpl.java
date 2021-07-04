package com.spring.sdm.dao;

import java.util.List;


import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.sdm.vo.User;

@Repository
public class userDaoImpl implements userDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	

	public void saveUser(User user) {	
		Session session= this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(user);
	}

	
	
	@SuppressWarnings("unchecked")
	public User getUserByUsername(String username) {
		
		Session session=this.sessionFactory.getCurrentSession();
		Query<User> query=session.createQuery("from User where email='"+username+"'");
		List<User> ls=query.list();
		return ls.get(0);
	}



	@SuppressWarnings("unchecked")
	public List<User> fetchAllUser() {
		Session session=this.sessionFactory.getCurrentSession();
		Query<User> query=session.createQuery("from User");
		List<User> ls=query.list();
		return ls;
	}

}
