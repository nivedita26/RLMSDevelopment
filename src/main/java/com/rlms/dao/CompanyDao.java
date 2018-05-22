package com.rlms.dao;

import java.util.List;

import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsBranchMaster;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCompanyRoleMap;

public interface CompanyDao {

	public void saveCompanyM(RlmsCompanyMaster rlmsCompanyMaster);
	public RlmsCompanyMaster getCompanyByEmailID(String emailID);
	
	public List<RlmsCompanyMaster> getAllCompanies(Integer companyId);
	public RlmsCompanyMaster getCompanyById(Integer companyId);
	public RlmsCompanyBranchMapDtls getCompanyBranchMapById(Integer companyBranchMapId);
	public void updateCompanyM(RlmsCompanyMaster rlmsCompanyMaster);
	//public RlmsCompanyRoleMap getCompanyRole(Integer companyId, Integer spocRoleId);
	public void deleteCompanyM(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo);
	public void updateBranchDetails(RlmsBranchMaster rlmsBranchMaster);
	public List<RlmsCompanyMaster> getAllCompaniesForDashboard(Integer companyId);
}
