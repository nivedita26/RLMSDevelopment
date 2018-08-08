package com.rlms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsMemberMaster;

@Repository
public class MemberDaoImpl  implements MemberDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public RlmsMemberMaster getMemberById(int id){		
			 Session session = this.sessionFactory.getCurrentSession();
			 Criteria criteria = session.createCriteria(RlmsMemberMaster.class)
			.add(Restrictions.eq("memberId",id))
			 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
        	RlmsMemberMaster  memberMaster = (RlmsMemberMaster) criteria.uniqueResult();
			 return memberMaster;
	}
}
