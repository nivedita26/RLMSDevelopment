package com.rlms.service;

import java.util.List;

import com.rlms.contract.BranchDtlsDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;

public interface CompanyService {

	public String validateAndSaveCompanyObj(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo) throws ValidationException;
	public List<RlmsCompanyMaster> getAllCompanies(UserMetaInfo metaInfo);
	
	public RlmsCompanyMaster getCompanyById(Integer companyId);
	
	public RlmsCompanyBranchMapDtls getCompanyBranchMapById(Integer companyBranchMapId);
	
	public List<RlmsCompanyBranchMapDtls> getAllBranches(Integer companyId);
	
	public String validateAndAddNewBranchInCompany(BranchDtlsDto dto,UserMetaInfo userMetaInfo) throws ValidationException;
	
	public List<RlmsBranchCustomerMap> getAllCustomersOfBranch(Integer commpBranchMapId);
	
	public List<BranchDtlsDto> getListOfBranchDtls(Integer companyId, UserMetaInfo metaInfo);
	
	//public RlmsCompanyRoleMap getCompanyRole(Integer companyId, Integer spocRoleId);
	
	public List<Integer> getListOfApplicableBranch(Integer userRoleId, UserMetaInfo metaInfo);
	
	public List<CompanyDtlsDTO> getAllCompanyDetails(UserMetaInfo metaInfo);
	
	public String validateAndUpdateCompanyObj(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo) throws ValidationException;
	
	public String validateAndDeleteCompanyObj(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo) throws ValidationException;
	
	public String editBranchInCompany(BranchDtlsDto dto,UserMetaInfo userMetaInfo);
	
	public List<CompanyDtlsDTO> getAllCompanyDetailsForDashboard(UserMetaInfo metaInfo);
	
}
