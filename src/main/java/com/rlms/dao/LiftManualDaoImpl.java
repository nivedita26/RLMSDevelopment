package com.rlms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.CompanyManualDto;
import com.rlms.model.RlmsCompanyManual;
import com.rlms.model.RlmsLiftManualMapDtls;

@Repository
public class LiftManualDaoImpl  implements LiftManualDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	@Override
	public void saveCompanyManual(RlmsCompanyManual companyManual) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(companyManual);
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT)
	public RlmsCompanyManual getCompanyManual(CompanyManualDto companyManualDto) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCompanyManual.class)
		.add(Restrictions.eq("rlmsCompanyMaster.companyId",companyManualDto.getCompanyId()))
		.add(Restrictions.eq("liftType",companyManualDto.getLiftType()))
		.add(Restrictions.eq("modelVersion",companyManualDto.getModelVersion()))
	     .add(Restrictions.eq("activeFlag",RLMSConstants.ACTIVE.getId()));
		 RlmsCompanyManual   rlmsCompanyManual =  (RlmsCompanyManual) criteria.uniqueResult();
		 return rlmsCompanyManual;
     }

	@Transactional
	@Override
	public void saveLiftManualMapDtls(RlmsLiftManualMapDtls liftManualMapDtls) {
		this.sessionFactory.getCurrentSession().save(liftManualMapDtls);
	}

	@Override
	public RlmsLiftManualMapDtls getLiftManualMapDtls(int liftCustomerMapId) {
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsLiftManualMapDtls.class)
		.add(Restrictions.eq("rlmsLiftCustomerMap.liftCustomerMapId",liftCustomerMapId))
		 .add(Restrictions.eq("activeFlag",RLMSConstants.ACTIVE.getId()));
         RlmsLiftManualMapDtls   manualMapDtls  =   (RlmsLiftManualMapDtls) criteria.uniqueResult();
		 return manualMapDtls;
	}
}
