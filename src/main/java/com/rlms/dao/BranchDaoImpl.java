package com.rlms.dao;

import java.util.List;









import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsBranchMaster;
import com.rlms.model.RlmsCompanyBranchMapDtls;

@Repository("branchDao")
public class BranchDaoImpl implements BranchDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public BranchDaoImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsCompanyBranchMapDtls> getAllBranches(Integer companyId){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.add(Restrictions.eq("rlmsCompanyMaster.companyId", companyId));
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = criteria.list();
		return listOfAllBranches;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsCompanyBranchMapDtls> getAllBranchesForCopanies(List<Integer> ListOfCompanyIds){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.add(Restrictions.in("rlmsCompanyMaster.companyId", ListOfCompanyIds));
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = criteria.list();
		return listOfAllBranches;
		
	}
	
	public RlmsCompanyBranchMapDtls getBranchByBranchName(String branchName, Integer companyId){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.createAlias("rlmsBranchMaster", "BM");
		criteria.createAlias("rlmsCompanyMaster", "CM");
		criteria.add(Restrictions.eq("BM.branchName", branchName));
		criteria.add(Restrictions.eq("CM.companyId", companyId));
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		RlmsCompanyBranchMapDtls companyBranchMapDtls = (RlmsCompanyBranchMapDtls) criteria.uniqueResult();
		return companyBranchMapDtls;
		
	}
	
	public Integer saveBranchM(RlmsBranchMaster rlmsBranchMaster){
		return (Integer) this.sessionFactory.getCurrentSession().save(rlmsBranchMaster);
	}
	
	public Integer saveCompanyBranchMapDtls(RlmsCompanyBranchMapDtls companyBranchMapDtls){
		return (Integer) this.sessionFactory.getCurrentSession().save(companyBranchMapDtls);
	}
	
	public Integer saveBranchCustomerMapDtls(RlmsBranchCustomerMap branchCustomerMap){
		return (Integer) this.sessionFactory.getCurrentSession().save(branchCustomerMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsBranchCustomerMap> getAllCustomersOfBranch(Integer commpBranchMapId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class)
				 .add(Restrictions.eq("companyBranchMapDtls.companyBranchMapId", commpBranchMapId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 		 //.add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", SpocRoleConstants.TECHNICIAN.getSpocRoleId()));
		 List<RlmsBranchCustomerMap> listAllCustomers =  criteria.list();
		 return listAllCustomers;
	}
	
	public RlmsCompanyBranchMapDtls getCompanyBranchMapDtls(Integer compBranchMapId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCompanyBranchMapDtls.class)
				 .add(Restrictions.eq("companyBranchMapId", compBranchMapId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 RlmsCompanyBranchMapDtls companyBranchMapDtls =  (RlmsCompanyBranchMapDtls) criteria.uniqueResult();
		 return companyBranchMapDtls;
	}
	
	@Override
	public RlmsBranchCustomerMap getBranchCustomerMapDtls(Integer branchCustomerMapId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class)
				 .add(Restrictions.eq("branchCustoMapId", branchCustomerMapId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 RlmsBranchCustomerMap branchCustomerMap =  (RlmsBranchCustomerMap) criteria.uniqueResult();
		 return branchCustomerMap;
	}
	
	@Override
	public RlmsBranchMaster getBranchByBranchId(Integer branchId){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsBranchMaster.class);
		criteria.add(Restrictions.eq("branchId", branchId));
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		RlmsBranchMaster branchMaster = (RlmsBranchMaster) criteria.uniqueResult();
		return branchMaster;
		
	}
}
