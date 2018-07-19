package com.rlms.dao;

import java.util.List;



import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCustomerMaster;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsUserApplicationMapDtls;

@Repository("customerDao")
public class CustomerDaoImpl implements CustomerDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public RlmsCustomerMaster getCustomerByEmailId(String emailId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCustomerMaster.class)
				 .add(Restrictions.eq("emailID", emailId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsCustomerMaster)criteria.uniqueResult();
	}
	
	@Override
	public RlmsCustomerMaster getCustomerById(Integer customerId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCustomerMaster.class)
				 .add(Restrictions.eq("customerId", customerId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsCustomerMaster)criteria.uniqueResult();
	}
	
	@Override
	public List<RlmsBranchCustomerMap> getAllCustomersForBranches(List<Integer> listOfBranchCompanyMapId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class)
				 .add(Restrictions.in("companyBranchMapDtls.companyBranchMapId", listOfBranchCompanyMapId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 List<RlmsBranchCustomerMap> listOfCustomers = criteria.list();
		 return listOfCustomers;
	}

	@Override
	public Integer saveCustomerM(RlmsCustomerMaster customerMaster) {
		Integer customerId = (Integer) this.sessionFactory.getCurrentSession().save(customerMaster);
		return customerId;
		
	}
	
	@Override
	public Integer saveCustomerMemberMap(RlmsCustomerMemberMap customerMemberMap) {
		Integer customerId = (Integer) this.sessionFactory.getCurrentSession().save(customerMemberMap);
		return customerId;
		
	}
	
	@Override
	public Integer saveMemberM(RlmsMemberMaster memberMaster) {
		Integer customerId = (Integer) this.sessionFactory.getCurrentSession().save(memberMaster);
		return customerId;
		
	}
	
	@Override
	public RlmsMemberMaster getMemberByCntNo(String phoneNumber) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsMemberMaster.class)
				 .add(Restrictions.eq("contactNumber", phoneNumber))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsMemberMaster)criteria.uniqueResult();
	}
	

	public RlmsMemberMaster getMemberById(Integer memberId) {

		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsMemberMaster.class)
				 .add(Restrictions.eq("memberId", memberId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsMemberMaster)criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RlmsCustomerMemberMap> getAllCustomersForMember(Integer memberId){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCustomerMemberMap.class)
				 .add(Restrictions.eq("rlmsMemberMaster.memberId", memberId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return criteria.list();
	}
	
	@Override
	public RlmsBranchCustomerMap getBranchCustomerMapByCustoId(Integer customerId){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class)
				 .add(Restrictions.eq("customerMaster.customerId", customerId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsBranchCustomerMap) criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RlmsCustomerMemberMap> getAllMembersForCustomer(Integer branchCustomerMapId){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCustomerMemberMap.class)
				 .createAlias("rlmsBranchCustomerMap", "cm")
				 .add(Restrictions.eq("cm.branchCustoMapId", branchCustomerMapId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return criteria.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public RlmsUserApplicationMapDtls getUserAppDtls(Integer userId, Integer userType){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserApplicationMapDtls.class)
				 .add(Restrictions.eq("userId", userId))
				 .add(Restrictions.eq("userRefType", userType))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsUserApplicationMapDtls) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsBranchCustomerMap> getCustomerByName(String custoName, Integer companyBranchMapId, Integer companyId){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class);
		 criteria.createAlias("customerMaster", "cm");
		 criteria.createAlias("companyBranchMapDtls", "cbm");
		 criteria.createAlias("cbm.rlmsCompanyMaster", "cpm");
		 if(null != companyBranchMapId){
			 criteria.add(Restrictions.eq("cbm.companyBranchMapId", companyBranchMapId));
		 }
		 if(null != companyId){
			 criteria.add(Restrictions.eq("cpm.companyId", companyId));
		 }
		 criteria.add(Restrictions.like("cm.customerName", custoName, MatchMode.ANYWHERE));
				 
		 criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return criteria.list();
	}
	
	@Transactional 
	@Override
	public List<RlmsBranchCustomerMap> getAllCustomersForDashboard(List<Integer> listOfBranchCompanyMapId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class)
				 		.add(Restrictions.in("companyBranchMapDtls.companyBranchMapId", listOfBranchCompanyMapId));
		 List<RlmsBranchCustomerMap> listOfCustomers = criteria.list();
		 return listOfCustomers;
	}
	@Override
	public void updateCustomer(RlmsCustomerMaster customerMaster) {
		this.sessionFactory.getCurrentSession().update(customerMaster);
	}
	@Override
	public void updateMember(RlmsMemberMaster memberMaster) {
		this.sessionFactory.getCurrentSession().update(memberMaster);
	}

	@Transactional
	@Override
	public void deleteMember(RlmsMemberMaster memberMaster) {
		this.sessionFactory.getCurrentSession().update(memberMaster);

	}
}
