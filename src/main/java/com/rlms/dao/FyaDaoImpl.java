package com.rlms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.model.RlmsFyaTranDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.constants.RLMSConstants;
import com.rlms.dao.FyaDao;

@Repository
public class FyaDaoImpl implements FyaDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveFyaTranDtls(RlmsFyaTranDtls fyaTranDtls){
		this.sessionFactory.getCurrentSession().save(fyaTranDtls);
	}
	
	@Override
	public void updateFyaTranDtls(RlmsFyaTranDtls fyaTranDtls){
		this.sessionFactory.getCurrentSession().update(fyaTranDtls);
	}
	
	@Override
	public RlmsFyaTranDtls getFyaForLift(Integer liftCustomerMapID, Integer status){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsFyaTranDtls.class)
				 .createAlias("liftCustomerMap", "lcm")
				 .add(Restrictions.eq("lcm.liftCustomerMapId", liftCustomerMapID))
				 .add(Restrictions.eq("status", status))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 RlmsFyaTranDtls fyaTranDtls = (RlmsFyaTranDtls) criteria.uniqueResult();
		 return fyaTranDtls;
	}
	
	@Override
	public RlmsFyaTranDtls getFyaByFyaTranIDt(Integer fyaTranId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsFyaTranDtls.class)
				 .add(Restrictions.eq("fyaTranId", fyaTranId));
		 RlmsFyaTranDtls fyaTranDtls = (RlmsFyaTranDtls) criteria.uniqueResult();
		 return fyaTranDtls;
	}
}
