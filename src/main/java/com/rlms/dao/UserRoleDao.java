package com.rlms.dao;

import java.util.List;

import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyRoleMap;
import com.rlms.model.RlmsSpocRoleMaster;
import com.rlms.model.RlmsUserApplicationMapDtls;
import com.rlms.model.RlmsUserRoles;



public interface UserRoleDao{

	public Integer saveUserRole(RlmsUserRoles userRoles);
	public RlmsUserRoles getUserRoleObj(Integer userID, String userName, String password);
	public List<RlmsSpocRoleMaster> getAllRoles(UserMetaInfo metaInfo);
	public RlmsSpocRoleMaster getSpocRoleById(Integer spocRoleId);
	public RlmsUserRoles getUserRole(Integer spocRoleId, Integer userID);
	public RlmsUserRoles getUserRole(Integer userRoleId);
	//public RlmsCompanyRoleMap getTechnicianRoleForBranch(Integer commpBranchMapId);
	public List<RlmsUserRoles> getAllUserWithRoleForBranch(Integer commpBranchMapId, Integer spocRoleId);
	public RlmsUserRoles getUserByUserName(String username);
	public RlmsUserRoles getUserRoleByUserId(Integer userID);
	public RlmsUserRoles getUserRoleToRegister(Integer userRoleId);
	public RlmsUserRoles getUserRoleForCompany(Integer cmpanyId, Integer spocRoleId);
	public RlmsUserRoles getTechnicianRoleObjByMblNo(String mblNumber,
			Integer spocRoleId);
	public void saveUserAppDlts(RlmsUserApplicationMapDtls userApplicationMapDtls);
	public void mergeUserAppDlts(RlmsUserApplicationMapDtls userApplicationMapDtls);
	public List<RlmsUserRoles> getAllUserWithRoleForBranch(Integer commpBranchMapId, Integer companyId, Integer spocRoleId);
	public RlmsUserRoles getUserIFRoleisAssigned(Integer userID);
	public RlmsUserRoles getUserWithRoleForCompany(Integer companyId, Integer spocRoleId);
}
