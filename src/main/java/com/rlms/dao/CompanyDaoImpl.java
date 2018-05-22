package com.rlms.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsBranchMaster;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;

@Repository("companyDao")
public class CompanyDaoImpl implements CompanyDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public CompanyDaoImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void saveCompanyM(RlmsCompanyMaster rlmsCompanyMaster){
		
		this.sessionFactory.getCurrentSession().save(rlmsCompanyMaster);
	}
	
	public RlmsCompanyMaster getCompanyByEmailID(String emailID){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RlmsCompanyMaster.class);
		criteria.add(Restrictions.eq("emailId", emailID));
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		RlmsCompanyMaster companyMaster = (RlmsCompanyMaster) criteria.uniqueResult();
		return  companyMaster;
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsCompanyMaster> getAllCompanies(Integer companyId){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RlmsCompanyMaster.class);
		if(null != companyId){
			criteria.add(Restrictions.eq("companyId", companyId));
		}
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		List<RlmsCompanyMaster> listOfAllCompanies = criteria.list();
		return  listOfAllCompanies;
	}
	
	@SuppressWarnings("unchecked")
	public RlmsCompanyMaster getCompanyById(Integer companyId){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RlmsCompanyMaster.class);
		criteria.add(Restrictions.eq("companyId", companyId));
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		RlmsCompanyMaster companyMaster = (RlmsCompanyMaster) criteria.uniqueResult();
		return  companyMaster;
	}
	
	
	public RlmsCompanyBranchMapDtls getCompanyBranchMapById(Integer companyBranchMapId){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.add(Restrictions.eq("companyBranchMapId", companyBranchMapId));
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		RlmsCompanyBranchMapDtls companyBranchMapDtls = (RlmsCompanyBranchMapDtls) criteria.uniqueResult();
		return  companyBranchMapDtls;
	}
	
	public void updateCompanyM(RlmsCompanyMaster rlmsCompanyMaster){
		this.sessionFactory.getCurrentSession().update(rlmsCompanyMaster);
	}
	
	public void deleteCompanyM(CompanyDtlsDTO companyDtlsDTO,UserMetaInfo metaInfo){
		Query q = this.sessionFactory.getCurrentSession().createQuery("update RlmsCompanyMaster set activeFlag=:activeFlag,status=:status,updatedDate=:updatedDate,updatedBy=:updatedBy where companyId=:companyId");
		q.setParameter("companyId", companyDtlsDTO.getCompanyId());
		q.setParameter("activeFlag", RLMSConstants.INACTIVE.getId());
		q.setParameter("status", RLMSConstants.INACTIVE.getId());
		q.setParameter("updatedDate", new Date());
		q.setParameter("updatedBy", metaInfo.getUserId());
		q.executeUpdate();
	}
	
	public void updateBranchDetails(RlmsBranchMaster rlmsBranchMaster){
		this.sessionFactory.getCurrentSession().update(rlmsBranchMaster);
	}
	
	/*@SuppressWarnings("unchecked")
	public RlmsCompanyRoleMap getCompanyRole(Integer companyId, Integer spocRoleId){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RlmsCompanyRoleMap.class);
		criteria.add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId));
		criteria.add(Restrictions.eq("rlmsCompanyMaster.companyId", companyId));
		RlmsCompanyRoleMap companyRoleMap = (RlmsCompanyRoleMap) criteria.uniqueResult();
		return  companyRoleMap;
	}*/
	
	@SuppressWarnings("unchecked")
	public List<RlmsCompanyMaster> getAllCompaniesForDashboard(Integer companyId){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RlmsCompanyMaster.class);
		if(null != companyId){
			criteria.add(Restrictions.eq("companyId", companyId));
		};
		List<RlmsCompanyMaster> listOfAllCompanies = criteria.list();
		return  listOfAllCompanies;
	}
	
}
