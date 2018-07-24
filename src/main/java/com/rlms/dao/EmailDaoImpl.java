package com.rlms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.model.EmailTemplate;

@Repository("emailDao")
public class EmailDaoImpl implements EmailDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public EmailDaoImpl(){
		
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public EmailTemplate getEmailTemplate(Integer templateId){
		 Session session = this.sessionFactory.openSession();
		 Criteria criteria = session.createCriteria(EmailTemplate.class)
		         .add(Restrictions.eq("templateId", templateId));
		 
		 EmailTemplate emailTemplate = (EmailTemplate) criteria.uniqueResult();
		 session.close();
		 return emailTemplate;
	}
}
