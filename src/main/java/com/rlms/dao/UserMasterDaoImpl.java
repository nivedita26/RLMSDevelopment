package com.rlms.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;

@Repository("cbUserMasterDao")
public class UserMasterDaoImpl implements


UserMasterDao{
   
	@Autowired
	private SessionFactory sessionFactory;
	
	public UserMasterDaoImpl(){
		
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	
	public RlmsUsersMaster getUserByEmailID(String emailId)
	{
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUsersMaster.class)
				 .add(Restrictions.eq("emailId", emailId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsUsersMaster)criteria.uniqueResult();
	}
	
	public RlmsUsersMaster getUserByUserId(Integer userId)
	{
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUsersMaster.class)
				 .add(Restrictions.eq("userId", userId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsUsersMaster)criteria.uniqueResult();
	}
	
	public RlmsUsersMaster getUserByUserNameAndPassword(String username, String password)
	{
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUsersMaster.class)
				 .add(Restrictions.eq("username", username))
		         .add(Restrictions.eq("password", password));
		 
		 return (RlmsUsersMaster)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unused")
	public List<RlmsUsersMaster> getAllUsersForCompany(Integer companyId){
		 Session session = this.sessionFactory.getCurrentSession();
		Criteria query = session.createCriteria(RlmsUsersMaster.class);
		query.add(Restrictions.eq("rlmsCompanyMaster.companyId", companyId));
		query.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		List<RlmsUsersMaster> listOfAllUsers = query.list();
		return listOfAllUsers;
		
	}

	public void saveUser(RlmsUsersMaster usermMaster){
		this.sessionFactory.getCurrentSession().save(usermMaster);
	}
	
	public void updateUser(RlmsUsersMaster usermMaster){
		this.sessionFactory.getCurrentSession().update(usermMaster);
	}
	
	public void mergerUser(RlmsUsersMaster usermMaster){
		this.sessionFactory.getCurrentSession().merge(usermMaster);
	}
	
	public void mergerUserRole(RlmsUserRoles rlmsUserRoles){
		this.sessionFactory.getCurrentSession().merge(rlmsUserRoles);
	}
	
	public void deleteUser(UserDtlsDto dto, UserMetaInfo metaInfo){
		Query q = this.sessionFactory.getCurrentSession().createQuery("update RlmsUsersMaster set activeFlag=:activeFlag,updatedDate=:updatedDate,updatedBy=:updatedBy where userId=:userId");
		q.setParameter("userId", dto.getUserId());
		q.setParameter("activeFlag", RLMSConstants.INACTIVE.getId());
		q.setParameter("updatedDate", new Date());
		q.setParameter("updatedBy", metaInfo.getUserId());
		q.executeUpdate();
	}
	
}
