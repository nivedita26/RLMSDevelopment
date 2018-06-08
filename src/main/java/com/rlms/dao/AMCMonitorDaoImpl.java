package com.rlms.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.model.RlmsLiftAmcDtls;

@Repository
public class AMCMonitorDaoImpl implements AMCMonitorDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	/*public AMCMonitorDaoImpl() {
		super();
	}
*/
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<RlmsLiftAmcDtls> getAllAMCDetails() {
     Session session= this.sessionFactory.getCurrentSession();
	 Criteria criteria = session.createCriteria(RlmsLiftAmcDtls.class);
     List<RlmsLiftAmcDtls> amcDtlsList = criteria.list();
		return amcDtlsList;
	}
}
