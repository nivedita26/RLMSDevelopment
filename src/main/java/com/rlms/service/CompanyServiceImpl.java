package com.rlms.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.fabric.xmlrpc.base.Array;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.SpocRoleConstants;
import com.rlms.contract.BranchDtlsDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.BranchDao;
import com.rlms.dao.CompanyDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsBranchMaster;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCompanyRoleMap;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsUserRoles;
import com.rlms.utils.PropertyUtils;

@Service("companyService")
@Transactional
public class CompanyServiceImpl implements CompanyService{
	
	@Autowired
	private CompanyDao companyDao;
	
	@Autowired
	private BranchDao branchDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LiftService liftService;
	
	

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveCompanyM(RlmsCompanyMaster rlmsCompanyMaster){
		this.companyDao.saveCompanyM(rlmsCompanyMaster);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndSaveCompanyObj(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo) throws ValidationException{
		String statusMesage = this.validateCompanyDtls(companyDtlsDTO);
		if(null == statusMesage){
			RlmsCompanyMaster companyMaster = this.constructCompanyM(companyDtlsDTO, metaInfo);
			this.saveCompanyM(companyMaster);
			statusMesage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPANY_REG_SUCCESFUL.getMessage());
		}
		return statusMesage;
	}
	
	private String validateCompanyDtls(CompanyDtlsDTO companyDtlsDTO) throws ValidationException{
		
        String errorMessage = null;
		if(null != companyDtlsDTO.getEmailId() && !companyDtlsDTO.getEmailId().isEmpty()){
			RlmsCompanyMaster companyMaster = this.getCompanyByEmailID(companyDtlsDTO.getEmailId());	
			if(null != companyMaster){
				throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.USER_ALREADY_REGISTERED.getMessage()));
			}
		}else{
			//errorMessage = "Please provide emailID";
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.PLEASE_PROVIDE_EMAILID.getMessage()));
		}
		
		return errorMessage;
		
	}
	
	private RlmsCompanyMaster constructCompanyM(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo){
		RlmsCompanyMaster companyMaster = new RlmsCompanyMaster();
		companyMaster.setCompanyName(companyDtlsDTO.getCompanyName());
		companyMaster.setAddress(companyDtlsDTO.getAddress());
		companyMaster.setContactNumber(companyDtlsDTO.getContactNumber());
		companyMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		companyMaster.setEmailId(companyDtlsDTO.getEmailId());
		companyMaster.setPanNumber(companyDtlsDTO.getPanNumber());
		companyMaster.setStatus(RLMSConstants.ACTIVE.getId());
		companyMaster.setTinNumber(companyDtlsDTO.getTinNumber());
		companyMaster.setVatNumber(companyDtlsDTO.getVatNumber());
		companyMaster.setCity(companyDtlsDTO.getCity());
		companyMaster.setArea(companyDtlsDTO.getArea());
		companyMaster.setPincode(companyDtlsDTO.getPinCode());
		companyMaster.setOwnerNAme(companyDtlsDTO.getOwnerName());
		companyMaster.setOwnerNumber(companyDtlsDTO.getOwnerNumber());
		companyMaster.setOwnerEmailId(companyDtlsDTO.getOwnerEmail());
		companyMaster.setCreatedDate(new Date());
		companyMaster.setCretedBy(metaInfo.getUserId());
		companyMaster.setUpdatedDate(new Date());
		companyMaster.setUpdatedBy(metaInfo.getUserId());
		return companyMaster;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsCompanyMaster getCompanyByEmailID(String emailID){
		return this.companyDao.getCompanyByEmailID(emailID);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsCompanyMaster> getAllCompanies(UserMetaInfo metaInfo){
		RlmsUserRoles userRole = metaInfo.getUserRole();
		List<RlmsCompanyMaster> listOfAllCompanies = null;
		if(null != userRole){
			if(SpocRoleConstants.INDITECH_ADMIN.getSpocRoleId().equals(userRole.getRlmsSpocRoleMaster().getSpocRoleId()) ||
					SpocRoleConstants.INDITECH_OPERATOR.getSpocRoleId().equals(userRole.getRlmsSpocRoleMaster().getSpocRoleId())){
				listOfAllCompanies = this.companyDao.getAllCompanies(null);
			}else{
				listOfAllCompanies = this.companyDao.getAllCompanies(userRole.getRlmsCompanyMaster().getCompanyId());
			}
		}
		return listOfAllCompanies;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CompanyDtlsDTO> getAllCompanyDetails(UserMetaInfo metaInfo){
		List<CompanyDtlsDTO> listOfCompanyDtos = new ArrayList<CompanyDtlsDTO>();
		
		List<RlmsCompanyMaster> listOfCompanies = this.getAllCompanies(metaInfo);
		
		//Iterate over applicable companies to get details of each company
		for (RlmsCompanyMaster rlmsCompanyMaster : listOfCompanies) {
			CompanyDtlsDTO companyDto = new CompanyDtlsDTO();
			List<Integer> listOFCompanyIds = new ArrayList<Integer>();
			listOFCompanyIds.add(rlmsCompanyMaster.getCompanyId());
			List<UserDtlsDto> listOfAllTechniciansOfCompany = new ArrayList<UserDtlsDto>();
			List<LiftDtlsDto> listOfAllLiftsUnderCompany = new ArrayList<LiftDtlsDto>();
			
			//Set company Details
			companyDto.setCompanyName(rlmsCompanyMaster.getCompanyName());
			companyDto.setContactNumber(rlmsCompanyMaster.getContactNumber());
			companyDto.setAddress(rlmsCompanyMaster.getAddress());
			companyDto.setEmailId(rlmsCompanyMaster.getEmailId());
			companyDto.setArea(rlmsCompanyMaster.getArea());
			companyDto.setCity(rlmsCompanyMaster.getCity());
			companyDto.setPinCode(rlmsCompanyMaster.getPincode());
			
			companyDto.setOwnerName(rlmsCompanyMaster.getOwnerNAme());
			companyDto.setOwnerNumber(rlmsCompanyMaster.getOwnerNumber());
			companyDto.setOwnerEmail(rlmsCompanyMaster.getOwnerEmailId());
			companyDto.setPanNumber(rlmsCompanyMaster.getPanNumber());
			companyDto.setTinNumber(rlmsCompanyMaster.getTinNumber());
			companyDto.setVatNumber(rlmsCompanyMaster.getVatNumber());
			companyDto.setCompanyId(rlmsCompanyMaster.getCompanyId());
			
			//Get All branches for company
			List<BranchDtlsDto> listOfDtos = new ArrayList<BranchDtlsDto>();
			List<RlmsCompanyBranchMapDtls> listOfAllBranches = this.branchDao.getAllBranchesForCopanies(listOFCompanyIds);
			for (RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls : listOfAllBranches) {
				BranchDtlsDto dto = new BranchDtlsDto();
				dto.setBranchName(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchName());
				dto.setBranchAddress(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchAddress());
				dto.setCompanyName(rlmsCompanyBranchMapDtls.getRlmsCompanyMaster().getCompanyName());
				List<UserDtlsDto> listOfAllTech = this.getListOFAllTEchnicians(rlmsCompanyBranchMapDtls.getCompanyBranchMapId());
				dto.setListOfAllTechnicians(listOfAllTech);
				if(null != listOfAllTech && !listOfAllTech.isEmpty()){
					dto.setNumberOfTechnicians(listOfAllTech.size());
				}
				List<LiftDtlsDto> listOfAllLifts = this.getListOfAllLifts(rlmsCompanyBranchMapDtls.getCompanyBranchMapId());
				if(null != listOfAllLifts){
					dto.setListOfAllLifts(listOfAllLifts);
				}
				if(null != listOfAllLifts){
					dto.setNumberOfLifts(listOfAllLifts.size());
				}
				listOfDtos.add(dto);
				//List Of technicians under single branch of company
				listOfAllTechniciansOfCompany.addAll(listOfAllTech);
				
				//List of lifts under single branch of company
				listOfAllLiftsUnderCompany.addAll(listOfAllLifts);
			}
			
			//List of all branches of company
			companyDto.setListOfBranches(listOfDtos);
			if(null != listOfDtos && !listOfDtos.isEmpty()){
				companyDto.setNumberOfBranches(listOfDtos.size());
			}
			
			//List of all technicians of company
			companyDto.setListOfTechnicians(listOfAllTechniciansOfCompany);
			if(null != listOfAllTechniciansOfCompany && !listOfAllTechniciansOfCompany.isEmpty()){
				companyDto.setNumberOfTech(listOfAllTechniciansOfCompany.size());
			}
			
			//List of all lifts of company
			companyDto.setListOfAllLifts(listOfAllLiftsUnderCompany);
			if(null != listOfAllLiftsUnderCompany && !listOfAllLiftsUnderCompany.isEmpty()){
				companyDto.setNumberOfLifts(listOfAllLiftsUnderCompany.size());
			}
		  listOfCompanyDtos.add(companyDto);
		}
		
		return listOfCompanyDtos;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsCompanyMaster getCompanyById(Integer companyId){
		return this.companyDao.getCompanyById(companyId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsCompanyBranchMapDtls getCompanyBranchMapById(Integer companyBranchMapId){
		return this.companyDao.getCompanyBranchMapById(companyBranchMapId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsCompanyBranchMapDtls> getAllBranches(Integer companyId){
		return this.branchDao.getAllBranches(companyId);
	}
	 
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndAddNewBranchInCompany(BranchDtlsDto dto,UserMetaInfo userMetaInfo) throws ValidationException{
		RlmsCompanyBranchMapDtls companyBranchMapDtls = this.branchDao.getBranchByBranchName(dto.getBranchName(), dto.getCompanyId());
		String statusMessage = "";
		if(null == companyBranchMapDtls){
		
		RlmsBranchMaster branchMaster = this.constructBranchMaster(dto, userMetaInfo);
		
		Integer branchId =  this.branchDao.saveBranchM(branchMaster);
		
		branchMaster.setBranchId(branchId);
		
		RlmsCompanyBranchMapDtls newCompanyBranchMapDtls = this.constructCompBranchMap(dto, branchMaster, userMetaInfo);
		
		this.branchDao.saveCompanyBranchMapDtls(newCompanyBranchMapDtls);
		
		statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.BRANCH_CREATION_SUCCESSFUL.getMessage());
		}else{
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.BRANCH_CREATION_SUCCESSFUL.getMessage()));
		//	statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.BRANCH_ALREADY_EXISTS.getMessage());
		}
		return statusMessage;
	}
	
	private RlmsBranchMaster constructBranchMaster(BranchDtlsDto dto, UserMetaInfo userMetaInfo){
		RlmsBranchMaster branchMaster = new RlmsBranchMaster();
		branchMaster.setBranchName(dto.getBranchName());
		branchMaster.setBranchAddress(dto.getBranchAddress());
		branchMaster.setCity(dto.getCity());
		branchMaster.setArea(dto.getArea());
		branchMaster.setPincode(dto.getPinCode());
		branchMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		branchMaster.setCreatedBy(userMetaInfo.getUserId());
		branchMaster.setCreatedDate(new Date());
		branchMaster.setUpdatedBy(userMetaInfo.getUserId());
		branchMaster.setUdpatedDate(new Date());
		return branchMaster;
	}
	
	private RlmsCompanyBranchMapDtls constructCompBranchMap(BranchDtlsDto dto, RlmsBranchMaster branchMaster, UserMetaInfo userMetaInfo){
		
		RlmsCompanyMaster companyMaster = this.companyDao.getCompanyById(dto.getCompanyId());
		
		RlmsCompanyBranchMapDtls companyBranchMapDtls = new RlmsCompanyBranchMapDtls();
		companyBranchMapDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		companyBranchMapDtls.setRlmsBranchMaster(branchMaster);
		companyBranchMapDtls.setRlmsCompanyMaster(companyMaster);
		companyBranchMapDtls.setCreatedBy(userMetaInfo.getUserId());
		companyBranchMapDtls.setCreatedDate(new Date());
		companyBranchMapDtls.setUdpatedDate(new Date());
		companyBranchMapDtls.setUpdatedBy(userMetaInfo.getUserId());
		companyBranchMapDtls.setStatus(RLMSConstants.ACTIVE.getId());
		return companyBranchMapDtls;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<BranchDtlsDto> getListOfBranchDtls(Integer companyId, UserMetaInfo metaInfo){
		List<Integer> listOfAllApplicableCompanies = new ArrayList<Integer>();
		List<Integer> listOFApplicableBranches = new ArrayList<Integer>();
		listOfAllApplicableCompanies.add(companyId);
		 List<RlmsCompanyBranchMapDtls> listOfAllBranches = this.branchDao.getAllBranchesForCopanies(listOfAllApplicableCompanies);
		  for (RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls : listOfAllBranches) {
			  listOFApplicableBranches.add(rlmsCompanyBranchMapDtls.getCompanyBranchMapId());
		  }
		  
		
		List<BranchDtlsDto> listOFBranchDtls = new ArrayList<BranchDtlsDto>();
		for (Integer companyBranchMapId : listOFApplicableBranches) {
			BranchDtlsDto branchDtlsDto = new BranchDtlsDto();
			RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls = this.branchDao.getCompanyBranchMapDtls(companyBranchMapId);
			branchDtlsDto.setId(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchId());
			branchDtlsDto.setBranchName(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchName());
			branchDtlsDto.setBranchAddress(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchAddress());
			branchDtlsDto.setArea(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getArea());
			branchDtlsDto.setCity(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getCity());
			branchDtlsDto.setPinCode(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getPincode());
			branchDtlsDto.setCompanyName(rlmsCompanyBranchMapDtls.getRlmsCompanyMaster().getCompanyName());
			List<UserDtlsDto> listOfAllTech = this.getListOFAllTEchnicians(companyBranchMapId);
			branchDtlsDto.setListOfAllTechnicians(listOfAllTech);
			if(null != listOfAllTech && !listOfAllTech.isEmpty()){
				branchDtlsDto.setNumberOfTechnicians(listOfAllTech.size());
			}
			List<LiftDtlsDto> listOfAllLifts = this.getListOfAllLifts(companyBranchMapId);
			if(null != listOfAllLifts){
				branchDtlsDto.setListOfAllLifts(listOfAllLifts);
			}
			if(null != listOfAllLifts){
				branchDtlsDto.setNumberOfLifts(listOfAllLifts.size());
			}
			listOFBranchDtls.add(branchDtlsDto);			
		}
		return listOFBranchDtls;
	}
	
	private List<LiftDtlsDto> getListOfAllLifts(Integer companyBranchMapId){
		List<LiftDtlsDto> listOfUserDtls = new ArrayList<LiftDtlsDto>();
		
		List<RlmsLiftCustomerMap> listOfAllLifts = this.liftService.getAllLiftsForBranch(companyBranchMapId);
		for (RlmsLiftCustomerMap rlmsLiftCustomerMap : listOfAllLifts) {
			LiftDtlsDto liftDtlsDto = new LiftDtlsDto();
			liftDtlsDto.setAddress(rlmsLiftCustomerMap.getLiftMaster().getAddress());
			liftDtlsDto.setCustomerName(rlmsLiftCustomerMap.getBranchCustomerMap().getCustomerMaster().getCustomerName());
			liftDtlsDto.setLiftNumber(rlmsLiftCustomerMap.getLiftMaster().getLiftNumber());
			listOfUserDtls.add(liftDtlsDto);
		}
		return listOfUserDtls;
	}
	private List<UserDtlsDto> getListOFAllTEchnicians(Integer companyBranchMapId){
		List<UserDtlsDto> listOfUserDtls = new ArrayList<UserDtlsDto>();
		List<RlmsUserRoles> listOfAlltech = this.userService.getListOfTechniciansForBranch(companyBranchMapId);
		for (RlmsUserRoles rlmsUserRoles : listOfAlltech) {
			UserDtlsDto userDtlsDto = new UserDtlsDto();
			userDtlsDto.setAddress(rlmsUserRoles.getRlmsUserMaster().getAddress());
			if(null != rlmsUserRoles.getRlmsCompanyMaster()){
				userDtlsDto.setCompanyName(rlmsUserRoles.getRlmsCompanyMaster().getCompanyName());
			}
			userDtlsDto.setContactNumber(rlmsUserRoles.getRlmsUserMaster().getContactNumber());
			userDtlsDto.setFirstName(rlmsUserRoles.getRlmsUserMaster().getFirstName());
			userDtlsDto.setLastName(rlmsUserRoles.getRlmsUserMaster().getLastName());
			listOfUserDtls.add(userDtlsDto);
		}
		return listOfUserDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Integer> getListOfApplicableBranch(Integer userRoleId, UserMetaInfo metaInfo){
		List<Integer> listOfAllApplicableCompanies = new ArrayList<Integer>();
		List<Integer> listOfApplicableBranch = new ArrayList<Integer>();
		if(SpocRoleConstants.ROLE_LEVEL_ONE.getSpocRoleId().equals(metaInfo.getUserRole().getRlmsSpocRoleMaster().getRoleLevel()) || SpocRoleConstants.ROLE_LEVEL_TWO.getSpocRoleId().equals(metaInfo.getUserRole().getRlmsSpocRoleMaster().getRoleLevel())){
			
			if(SpocRoleConstants.ROLE_LEVEL_ONE.getSpocRoleId().equals(metaInfo.getUserRole().getRlmsSpocRoleMaster().getRoleLevel())){
				List<RlmsCompanyMaster> listOfAllCompnies = this.getAllCompanies(metaInfo);
				for (RlmsCompanyMaster rlmsCompanyMaster : listOfAllCompnies) {
					listOfAllApplicableCompanies.add(rlmsCompanyMaster.getCompanyId());
				}	
			}else{
				listOfAllApplicableCompanies.add(metaInfo.getUserRole().getRlmsCompanyMaster().getCompanyId());
			}
		  List<RlmsCompanyBranchMapDtls> listOfAllBranches = this.branchDao.getAllBranchesForCopanies(listOfAllApplicableCompanies);
		  for (RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls : listOfAllBranches) {
			  listOfApplicableBranch.add(rlmsCompanyBranchMapDtls.getCompanyBranchMapId());
		  }
		}else{
			listOfApplicableBranch.add(metaInfo.getUserRole().getRlmsCompanyBranchMapDtls().getCompanyBranchMapId());
		}
		
		return listOfApplicableBranch;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsBranchCustomerMap> getAllCustomersOfBranch(Integer commpBranchMapId){
	  return this.branchDao.getAllCustomersOfBranch(commpBranchMapId);
	}
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	public RlmsCompanyRoleMap getCompanyRole(Integer companyId, Integer spocRoleId){
		return this.companyDao.getCompanyRole(companyId, spocRoleId);
	}*/
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndUpdateCompanyObj(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo) throws ValidationException{
		String statusMesage = null;
		RlmsCompanyMaster companyMaster=this.getCompanyById(companyDtlsDTO.getCompanyId());
		this.constructCompanyForUpdate(companyMaster,companyDtlsDTO, metaInfo);
		this.updateCompanyM(companyMaster);
		statusMesage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPANY_UPDATE_SUCCESFUL.getMessage());
		return statusMesage;
	}
	
	private RlmsCompanyMaster constructCompanyForUpdate(RlmsCompanyMaster companyMaster, CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo){
		companyMaster.setCompanyName(companyDtlsDTO.getCompanyName());
		companyMaster.setAddress(companyDtlsDTO.getAddress());
		companyMaster.setContactNumber(companyDtlsDTO.getContactNumber());
		companyMaster.setEmailId(companyDtlsDTO.getEmailId());
		companyMaster.setPanNumber(companyDtlsDTO.getPanNumber());
		companyMaster.setTinNumber(companyDtlsDTO.getTinNumber());
		companyMaster.setVatNumber(companyDtlsDTO.getVatNumber());
		companyMaster.setCity(companyDtlsDTO.getCity());
		companyMaster.setArea(companyDtlsDTO.getArea());
		companyMaster.setPincode(companyDtlsDTO.getPinCode());
		companyMaster.setOwnerNAme(companyDtlsDTO.getOwnerName());
		companyMaster.setOwnerNumber(companyDtlsDTO.getOwnerNumber());
		companyMaster.setOwnerEmailId(companyDtlsDTO.getOwnerEmail());
		companyMaster.setUpdatedDate(new Date());
		companyMaster.setUpdatedBy(metaInfo.getUserId());
		return companyMaster;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCompanyM(RlmsCompanyMaster rlmsCompanyMaster){
		this.companyDao.updateCompanyM(rlmsCompanyMaster);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndDeleteCompanyObj(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo) throws ValidationException{
		String statusMesage = null;
		this.deleteCompanyM(companyDtlsDTO,metaInfo);
		statusMesage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPANY_DELETE_SUCCESFUL.getMessage());
		return statusMesage;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCompanyM(CompanyDtlsDTO companyDtlsDTO, UserMetaInfo metaInfo){
		this.companyDao.deleteCompanyM(companyDtlsDTO,metaInfo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String editBranchInCompany(BranchDtlsDto dto,UserMetaInfo userMetaInfo){
		String statusMessage = "";
		RlmsBranchMaster branchMaster = this.branchDao.getBranchByBranchId(dto.getId());
		branchMaster.setBranchName(dto.getBranchName());
		branchMaster.setBranchAddress(dto.getBranchAddress());
		branchMaster.setCity(dto.getCity());
		branchMaster.setArea(dto.getArea());
		branchMaster.setPincode(dto.getPinCode());
		branchMaster.setUpdatedBy(userMetaInfo.getUserId());
		branchMaster.setUdpatedDate(new Date());
		this.companyDao.updateBranchDetails(branchMaster);
		statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.BRANCH_CREATION_SUCCESSFUL.getMessage());
		return statusMessage;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CompanyDtlsDTO> getAllCompanyDetailsForDashboard(UserMetaInfo metaInfo){
		List<CompanyDtlsDTO> listOfCompanyDtos = new ArrayList<CompanyDtlsDTO>();
		
		List<RlmsCompanyMaster> listOfCompanies = this.getAllCompaniesForDashboard(metaInfo);
		
		//Iterate over applicable companies to get details of each company
		for (RlmsCompanyMaster rlmsCompanyMaster : listOfCompanies) {
			CompanyDtlsDTO companyDto = new CompanyDtlsDTO();
			List<Integer> listOFCompanyIds = new ArrayList<Integer>();
			listOFCompanyIds.add(rlmsCompanyMaster.getCompanyId());
			List<UserDtlsDto> listOfAllTechniciansOfCompany = new ArrayList<UserDtlsDto>();
			List<LiftDtlsDto> listOfAllLiftsUnderCompany = new ArrayList<LiftDtlsDto>();
			
			//Set company Details
			companyDto.setCompanyName(rlmsCompanyMaster.getCompanyName());
			companyDto.setContactNumber(rlmsCompanyMaster.getContactNumber());
			companyDto.setAddress(rlmsCompanyMaster.getAddress());
			companyDto.setEmailId(rlmsCompanyMaster.getEmailId());
			companyDto.setArea(rlmsCompanyMaster.getArea());
			companyDto.setCity(rlmsCompanyMaster.getCity());
			companyDto.setPinCode(rlmsCompanyMaster.getPincode());
			
			companyDto.setOwnerName(rlmsCompanyMaster.getOwnerNAme());
			companyDto.setOwnerNumber(rlmsCompanyMaster.getOwnerNumber());
			companyDto.setOwnerEmail(rlmsCompanyMaster.getOwnerEmailId());
			companyDto.setPanNumber(rlmsCompanyMaster.getPanNumber());
			companyDto.setTinNumber(rlmsCompanyMaster.getTinNumber());
			companyDto.setVatNumber(rlmsCompanyMaster.getVatNumber());
			companyDto.setCompanyId(rlmsCompanyMaster.getCompanyId());
			companyDto.setActiveFlag(rlmsCompanyMaster.getActiveFlag());
			
			//Get All branches for company
			List<BranchDtlsDto> listOfDtos = new ArrayList<BranchDtlsDto>();
			List<RlmsCompanyBranchMapDtls> listOfAllBranches = this.branchDao.getAllBranchesForCopanies(listOFCompanyIds);
			for (RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls : listOfAllBranches) {
				BranchDtlsDto dto = new BranchDtlsDto();
				dto.setBranchName(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchName());
				dto.setBranchAddress(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchAddress());
				dto.setCompanyName(rlmsCompanyBranchMapDtls.getRlmsCompanyMaster().getCompanyName());
				List<UserDtlsDto> listOfAllTech = this.getListOFAllTEchnicians(rlmsCompanyBranchMapDtls.getCompanyBranchMapId());
				dto.setListOfAllTechnicians(listOfAllTech);
				if(null != listOfAllTech && !listOfAllTech.isEmpty()){
					dto.setNumberOfTechnicians(listOfAllTech.size());
				}
				List<LiftDtlsDto> listOfAllLifts = this.getListOfAllLifts(rlmsCompanyBranchMapDtls.getCompanyBranchMapId());
				if(null != listOfAllLifts){
					dto.setListOfAllLifts(listOfAllLifts);
				}
				if(null != listOfAllLifts){
					dto.setNumberOfLifts(listOfAllLifts.size());
				}
				listOfDtos.add(dto);
				//List Of technicians under single branch of company
				listOfAllTechniciansOfCompany.addAll(listOfAllTech);
				
				//List of lifts under single branch of company
				listOfAllLiftsUnderCompany.addAll(listOfAllLifts);
			}
			
			//List of all branches of company
			companyDto.setListOfBranches(listOfDtos);
			if(null != listOfDtos && !listOfDtos.isEmpty()){
				companyDto.setNumberOfBranches(listOfDtos.size());
			}
			
			//List of all technicians of company
			companyDto.setListOfTechnicians(listOfAllTechniciansOfCompany);
			if(null != listOfAllTechniciansOfCompany && !listOfAllTechniciansOfCompany.isEmpty()){
				companyDto.setNumberOfTech(listOfAllTechniciansOfCompany.size());
			}
			
			//List of all lifts of company
			companyDto.setListOfAllLifts(listOfAllLiftsUnderCompany);
			if(null != listOfAllLiftsUnderCompany && !listOfAllLiftsUnderCompany.isEmpty()){
				companyDto.setNumberOfLifts(listOfAllLiftsUnderCompany.size());
			}
		  listOfCompanyDtos.add(companyDto);
		}
		
		return listOfCompanyDtos;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsCompanyMaster> getAllCompaniesForDashboard(UserMetaInfo metaInfo){
		RlmsUserRoles userRole = metaInfo.getUserRole();
		List<RlmsCompanyMaster> listOfAllCompanies = null;
		if(null != userRole){
			if(SpocRoleConstants.INDITECH_ADMIN.getSpocRoleId().equals(userRole.getRlmsSpocRoleMaster().getSpocRoleId()) ||
					SpocRoleConstants.INDITECH_OPERATOR.getSpocRoleId().equals(userRole.getRlmsSpocRoleMaster().getSpocRoleId())){
				listOfAllCompanies = this.companyDao.getAllCompaniesForDashboard(null);
			}
		}
		return listOfAllCompanies;
	}
}