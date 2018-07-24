package com.rlms.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.model.RlmsLiftConfiguationP;
import com.rlms.model.RlmsLiftConfigurationA;
import com.rlms.model.RlmsLiftConfigurationBD;
import com.rlms.model.RlmsLiftConfigurationRTC;


@Repository
public class LiftConfigurationDaoImpl implements LiftConfigurationDao{

	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void saveLiftConfigA(RlmsLiftConfigurationA liftConfigurationA) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(liftConfigurationA);
		
	}

	@Override
	public void saveLiftConfigBD(RlmsLiftConfigurationBD liftConfigurationBD) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(liftConfigurationBD);
	}

	@Override
	public void saveLiftConfigP(RlmsLiftConfiguationP liftConfigurationP) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(liftConfigurationP);
	}

	@Override
	public void saveLiftConfigRTC(RlmsLiftConfigurationRTC liftConfigurationrtc) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(liftConfigurationrtc);
	}
	
	
}
