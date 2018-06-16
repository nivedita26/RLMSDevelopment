package com.rlms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsSpocRoleMaster;
import com.rlms.model.RlmsUserApplicationMapDtls;
import com.rlms.model.RlmsUserRoles;


@Repository
public class UserRoleDaoImpl implements
UserRoleDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public RlmsUserRoles getUserRoleObj(Integer userID, String userName, String password){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("rlmsUserMaster.userId", userID))
				  .add(Restrictions.eq("userName", userName))
				   .add(Restrictions.eq("password", password))
				 .add(Restrictions.eq("activeFlag", 1));
		 
		 return (RlmsUserRoles)criteria.uniqueResult();
	}
	
	public RlmsUserRoles getUserRoleByUserId(Integer userID){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("rlmsUserMaster.userId", userID))
				 .add(Restrictions.eq("activeFlag", 1));
		 
		 return (RlmsUserRoles)criteria.uniqueResult();
	}
	
	public RlmsUserRoles getUserIFRoleisAssigned(Integer userID){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("rlmsUserMaster.userId", userID));
		 
		 return (RlmsUserRoles)criteria.uniqueResult();
	}
	
	public List<RlmsSpocRoleMaster> getAllRoles(UserMetaInfo metaInfo){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsSpocRoleMaster.class)
				 .add(Restrictions.eq("activeFlag", 1))
			  	  .add(Restrictions.ne("spocRoleId", 8));
				// .add(Restrictions.ge("roleLevel", metaInfo.getUserRole().getRlmsSpocRoleMaster().getRoleLevel()));
				 if(metaInfo.getUserRole().getRlmsSpocRoleMaster().getSpocRoleId()==1) {//||metaInfo.getUserRole().getRlmsSpocRoleMaster().getSpocRoleId()==3||metaInfo.getUserRole().getRlmsSpocRoleMaster().getSpocRoleId()==5) {
					 criteria.add(Restrictions.gt("roleLevel", metaInfo.getUserRole().getRlmsSpocRoleMaster().getRoleLevel()));
				 }
				 else  {
					 criteria.add(Restrictions.ge("roleLevel", metaInfo.getUserRole().getRlmsSpocRoleMaster().getRoleLevel()));
					 criteria.add(Restrictions.ne("spocRoleId", metaInfo.getUserRole().getRlmsSpocRoleMaster().getSpocRoleId()));
				 }
		 List<RlmsSpocRoleMaster> listOfAllRoles = criteria.list();
		 return listOfAllRoles;
	}
	
	public Integer saveUserRole(RlmsUserRoles userRole){
		Integer userRoleId = (Integer) this.sessionFactory.getCurrentSession().save(userRole);
		return userRoleId;
	}

	public RlmsSpocRoleMaster getSpocRoleById(Integer spocRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsSpocRoleMaster.class)
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()))
		 		 .add(Restrictions.eq("spocRoleId", spocRoleId));
		 RlmsSpocRoleMaster spocRole = (RlmsSpocRoleMaster) criteria.uniqueResult();
		 return spocRole;
	}
	
	public RlmsUserRoles getUserRole(Integer spocRoleId, Integer userID){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("rlmsUserMaster.userId", userID))
				 .add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 RlmsUserRoles userRole = (RlmsUserRoles) criteria.uniqueResult();
		 return userRole;
	}
	
	public RlmsUserRoles getUserRole(Integer userRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("userRoleId", userRoleId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 RlmsUserRoles userRole = (RlmsUserRoles) criteria.uniqueResult();
		 return userRole;
	}
	
	public RlmsUserRoles getUserRoleToRegister(Integer userRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("userRoleId", userRoleId));
		 RlmsUserRoles userRole = (RlmsUserRoles) criteria.uniqueResult();
		 return userRole;
	}
	
	/*public RlmsCompanyRoleMap getTechnicianRoleForBranch(Integer commpBranchMapId){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCompanyRoleMap.class)
				 .add(Restrictions.eq("rlmsCompanyBranchMapDtls.companyBranchMapId", commpBranchMapId))
				 .add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", SpocRoleConstants.TECHNICIAN.getSpocRoleId()));
		 		 //.add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", SpocRoleConstants.TECHNICIAN.getSpocRoleId()));
		 RlmsCompanyRoleMap companyRoleMap = (RlmsCompanyRoleMap) criteria.uniqueResult();
		 return companyRoleMap;
	}*/
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<RlmsUserRoles> getAllUserWithRoleForBranch(Integer commpBranchMapId, Integer spocRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("rlmsCompanyBranchMapDtls.companyBranchMapId", commpBranchMapId))
				 .add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 		 //.add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", SpocRoleConstants.TECHNICIAN.getSpocRoleId()));
		 List<RlmsUserRoles> listOfAllTechnicians =  criteria.list();
		 return listOfAllTechnicians;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public RlmsUserRoles getUserWithRoleForCompany(Integer companyId, Integer spocRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("rlmsCompanyMaster.companyId", companyId))
				 .add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 		 //.add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", SpocRoleConstants.TECHNICIAN.getSpocRoleId()));
		 RlmsUserRoles userRoles =  (RlmsUserRoles) criteria.uniqueResult();
		 return userRoles;
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsUserRoles> getAllUserWithRoleForBranch(Integer commpBranchMapId, Integer companyId, Integer spocRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class);
				 if(null != commpBranchMapId){
					criteria.add(Restrictions.eq("rlmsCompanyBranchMapDtls.companyBranchMapId", commpBranchMapId));
				 }
				 if(null != companyId){
					criteria.add(Restrictions.eq("rlmsCompanyMaster.companyId", companyId));
				 }
				 criteria.add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId));
				 criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 List<RlmsUserRoles> listOfAllTechnicians =  criteria.list();
		 return listOfAllTechnicians;
	}
	
	public RlmsUserRoles getUserByUserName(String username)
	{
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("username", username))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsUserRoles)criteria.uniqueResult();
	}
	
	@Override
	public RlmsUserRoles getUserRoleForCompany(Integer cmpanyId, Integer spocRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .add(Restrictions.eq("rlmsCompanyMaster.companyId", cmpanyId))
		 	     .add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId))
		 	     .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 RlmsUserRoles userRole = (RlmsUserRoles) criteria.uniqueResult();
		 return userRole;
	}
	
	@Override
	public RlmsUserRoles getTechnicianRoleObjByMblNo(String mblNumber, Integer spocRoleId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .createAlias("rlmsUserMaster", "um")
		 		 .createAlias("rlmsSpocRoleMaster", "sm")
				 .add(Restrictions.eq("um.contactNumber", mblNumber))
		 	     .add(Restrictions.eq("sm.spocRoleId", spocRoleId))
		 	     .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 RlmsUserRoles userRole = (RlmsUserRoles) criteria.uniqueResult();
		 return userRole;
	}
	
	@Override
	public void saveUserAppDlts(RlmsUserApplicationMapDtls userApplicationMapDtls){
		 this.sessionFactory.getCurrentSession().save(userApplicationMapDtls);
	}
	
	@Override
	public void mergeUserAppDlts(RlmsUserApplicationMapDtls userApplicationMapDtls){
		 this.sessionFactory.getCurrentSession().merge(userApplicationMapDtls);
	}

	@Override
	public RlmsUserRoles getTechnicianRoleObjByUserNameAndPassword(UserDtlsDto dtlsDto, int roleId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsUserRoles.class)
				 .createAlias("rlmsUserMaster", "um")
		 		 .createAlias("rlmsSpocRoleMaster", "sm")
				 .add(Restrictions.eq("um.userName", dtlsDto.getUserName()))
				 .add(Restrictions.eq("um.password", dtlsDto.getPassword()))
		 	     .add(Restrictions.eq("sm.spocRoleId", roleId))
		 	     .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 RlmsUserRoles userRole = (RlmsUserRoles) criteria.uniqueResult();
		 return userRole;
	}
	
}
