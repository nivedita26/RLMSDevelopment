package com.rlms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.CompanyManualDto;
import com.rlms.model.RlmsCompanyManual;
import com.rlms.model.RlmsMemberMaster;

@Repository
public class LiftManualDaoImpl  implements LiftManualDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveCompanyManual(RlmsCompanyManual companyManual) {
		this.sessionFactory.getCurrentSession().save(companyManual);
	}

	@Override
	public RlmsCompanyManual getCompanyManual(int companyId,int liftType) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCompanyManual.class)
		.add(Restrictions.eq("companyMaster,companyId ",companyId))
		.add(Restrictions.eq("liftType ",liftType))
		 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
    	RlmsCompanyManual   rlmsCompanyManual =  (RlmsCompanyManual) criteria.uniqueResult();
		 return rlmsCompanyManual;
     }
}
