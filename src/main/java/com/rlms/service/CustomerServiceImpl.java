package com.rlms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.fabric.xmlrpc.base.Array;
import com.rlms.constants.AMCType;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.SpocRoleConstants;
import com.rlms.contract.AddNewUserDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.BranchDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.LiftDao;
import com.rlms.dao.UserRoleDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCustomerMaster;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsUserApplicationMapDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private BranchDao branchDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	
	public RlmsCustomerMaster getCustomerByEmailId(String emailId){
		return this.customerDao.getCustomerByEmailId(emailId);
	}
	
	
	private boolean validateCustomerDtls(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo) throws ValidationException{
		boolean isValidUser = true;
		
		RlmsCustomerMaster customerMaster = this.customerDao.getCustomerByEmailId(customerDtlsDto.getEmailID());
		if(null != customerMaster){
			isValidUser = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.CUSTOMER_ALREADY_ADDED.getMessage()));
		}else{
		
			RlmsCompanyBranchMapDtls companyBranchMapDtls = this.branchDao.getCompanyBranchMapDtls(customerDtlsDto.getBranchCompanyMapId());
			
			if(null == companyBranchMapDtls){
				isValidUser = false;
				throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.INVALID_BRANCH_NAME.getMessage()));
			}
		}
		
		return isValidUser;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndRegisterNewCustomer(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo) throws ValidationException{
		String statusMessage = "";
		if(this.validateCustomerDtls(customerDtlsDto, metaInfo)){
			RlmsCustomerMaster  customerMaster = this.constructCustomerObj(customerDtlsDto, metaInfo);
			Integer customerId = this.customerDao.saveCustomerM(customerMaster);
			customerMaster.setCustomerId(customerId);
			
			RlmsBranchCustomerMap branchCustomerMap = this.constructBranchCustMap(customerMaster, customerDtlsDto, metaInfo);
			this.branchDao.saveBranchCustomerMapDtls(branchCustomerMap);
			
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.USER_REG_SUCCESFUL.getMessage());
		}
		return statusMessage;
		
	}
	
	private RlmsBranchCustomerMap constructBranchCustMap(RlmsCustomerMaster customerMaster, CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo){
		RlmsBranchCustomerMap branchCustomerMap =  new RlmsBranchCustomerMap();
		RlmsCompanyBranchMapDtls companyBranchMapDtls = this.companyService.getCompanyBranchMapById(customerDtlsDto.getBranchCompanyMapId());
		branchCustomerMap.setActiveFlag(RLMSConstants.ACTIVE.getId());
		branchCustomerMap.setCompanyBranchMapDtls(companyBranchMapDtls);
		branchCustomerMap.setCustomerMaster(customerMaster);
		branchCustomerMap.setCreatedBy(metaInfo.getUserId());
		branchCustomerMap.setCreatedDate(new Date());
		branchCustomerMap.setUpdatedBy(metaInfo.getUserId());
		branchCustomerMap.setUpdatedDate(new Date());
		return branchCustomerMap;

	}
	private RlmsCustomerMaster constructCustomerObj(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo){
		RlmsCustomerMaster customerMaster = new RlmsCustomerMaster();
		customerMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		customerMaster.setAddress(customerDtlsDto.getAddress());
		customerMaster.setCntNumber(customerDtlsDto.getCntNumber());
		customerMaster.setCustomerName(customerDtlsDto.getFirstName());
		customerMaster.setCustomerType(customerDtlsDto.getCustomerType());
		customerMaster.setEmailID(customerDtlsDto.getEmailID());
		customerMaster.setPanNumber(customerDtlsDto.getPanNumber());
		customerMaster.setTinNumber(customerDtlsDto.getTinNumber());
		customerMaster.setCity(customerDtlsDto.getCity());
		customerMaster.setArea(customerDtlsDto.getArea());
		customerMaster.setPincode(customerDtlsDto.getPinCode());
		customerMaster.setVatNumber(customerDtlsDto.getVatNumber());
		customerMaster.setChairmanName(customerDtlsDto.getChairmanName());
		customerMaster.setChairmanNumber(customerDtlsDto.getChairmanNumber());
		customerMaster.setChairmanEmail(customerDtlsDto.getChairmanEmail());
		customerMaster.setTreasurerName(customerDtlsDto.getTreasurerName());
		customerMaster.setTreasurerNumber(customerDtlsDto.getTreasurerNumber());
		customerMaster.setTreasurerEmail(customerDtlsDto.getTreasurerEmail());
		customerMaster.setSecretaryName(customerDtlsDto.getSecretaryName());
		customerMaster.setSecretaryNumber(customerDtlsDto.getSecretaryNumber());
		customerMaster.setSecretaryEmail(customerDtlsDto.getSecretaryEmail());
		customerMaster.setWatchmenName(customerDtlsDto.getWatchmenName());
		customerMaster.setWatchmenNumber(customerDtlsDto.getWatchmenNumber());
		customerMaster.setWatchmenEmail(customerDtlsDto.getWatchmenEmail());
		customerMaster.setCreatedBy(metaInfo.getUserId());
		customerMaster.setCreatedDate(new Date());
		customerMaster.setUpdatedBy(metaInfo.getUserId());
		customerMaster.setUpdatedDate(new Date());
		return customerMaster;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDtlsDto> getAllApplicableCustomers(CustomerDtlsDto dto, UserMetaInfo metaInfo){
		//List<Integer> listOfApplicableBranchIds = this.companyService.getListOfApplicableBranch(metaInfo.getUserRole().getUserRoleId(), metaInfo);
		List<Integer> listOfApplicableBranchIds = new ArrayList<Integer>();
		listOfApplicableBranchIds.add(dto.getBranchCompanyMapId());
		List<RlmsBranchCustomerMap> listOfAllCustomers = this.customerDao.getAllCustomersForBranches(listOfApplicableBranchIds);
		return this.constructListOfCustomerDtlsDto(listOfAllCustomers);
	}
	
	private List<CustomerDtlsDto> constructListOfCustomerDtlsDto(List<RlmsBranchCustomerMap> listOfCustomers){
		List<CustomerDtlsDto> listOFDtos = new ArrayList<CustomerDtlsDto>();
		for (RlmsBranchCustomerMap branchCustomerMap : listOfCustomers) {
			List<Integer> listOfCustomer = new ArrayList<Integer>();
			listOfCustomer.add(branchCustomerMap.getCustomerMaster().getCustomerId());
			CustomerDtlsDto dto = new CustomerDtlsDto();
			List<RlmsLiftCustomerMap> listOfLifts = this.liftDao.getAllLiftsForCustomers(listOfCustomer);
			dto.setAddress(branchCustomerMap.getCustomerMaster().getAddress());
			dto.setArea(branchCustomerMap.getCustomerMaster().getArea());
			dto.setCity(branchCustomerMap.getCustomerMaster().getCity());
			dto.setPinCode(branchCustomerMap.getCustomerMaster().getPincode());
			dto.setCntNumber(branchCustomerMap.getCustomerMaster().getCntNumber());
			dto.setFirstName(branchCustomerMap.getCustomerMaster().getCustomerName());
			dto.setEmailID(branchCustomerMap.getCustomerMaster().getEmailID());
			dto.setPanNumber(branchCustomerMap.getCustomerMaster().getPanNumber());
			dto.setChairmanName(branchCustomerMap.getCustomerMaster().getChairmanName());
			dto.setChairmanNumber(branchCustomerMap.getCustomerMaster().getChairmanNumber());
			dto.setChairmanEmail(branchCustomerMap.getCustomerMaster().getChairmanEmail());
			dto.setSecretaryName(branchCustomerMap.getCustomerMaster().getSecretaryName());
			dto.setSecretaryNumber(branchCustomerMap.getCustomerMaster().getSecretaryNumber());
			dto.setSecretaryEmail(branchCustomerMap.getCustomerMaster().getSecretaryEmail());
			dto.setTreasurerName(branchCustomerMap.getCustomerMaster().getTreasurerName());
			dto.setTreasurerNumber(branchCustomerMap.getCustomerMaster().getTreasurerNumber());
			dto.setTreasurerEmail(branchCustomerMap.getCustomerMaster().getTreasurerEmail());
			dto.setWatchmenName(branchCustomerMap.getCustomerMaster().getWatchmenName());
			dto.setWatchmenNumber(branchCustomerMap.getCustomerMaster().getWatchmenNumber());
			dto.setWatchmenEmail(branchCustomerMap.getCustomerMaster().getWatchmenEmail());
			dto.setCustomerId(branchCustomerMap.getCustomerMaster().getCustomerId());
			dto.setActiveFlag(branchCustomerMap.getActiveFlag());
			if(null != listOfLifts && !listOfLifts.isEmpty()){
				dto.setTotalNumberOfLifts(listOfLifts.size());
			}
			dto.setBranchName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			dto.setCompanyName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			dto.setCustomerName(branchCustomerMap.getCustomerMaster().getCustomerName());
			listOFDtos.add(dto);
		}
		return listOFDtos;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDtlsDto> getAllCustomersForBranch(CustomerDtlsDto dto, UserMetaInfo metaInfo){
		List<CustomerDtlsDto> listOFDtos = new ArrayList<CustomerDtlsDto>();
		List<Integer> listOfApplicableBranchIds = new ArrayList<Integer>();
		listOfApplicableBranchIds.add(dto.getBranchCompanyMapId());
		List<RlmsBranchCustomerMap> listOfAllCustomers = this.customerDao.getAllCustomersForBranches(listOfApplicableBranchIds);
		for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOfAllCustomers) {
			CustomerDtlsDto customerDtlsDto = new CustomerDtlsDto();
			customerDtlsDto.setBranchCustomerMapId(rlmsBranchCustomerMap.getBranchCustoMapId());
			customerDtlsDto.setFirstName(rlmsBranchCustomerMap.getCustomerMaster().getCustomerName());
			customerDtlsDto.setCity(rlmsBranchCustomerMap.getCustomerMaster().getCity());
			customerDtlsDto.setArea(rlmsBranchCustomerMap.getCustomerMaster().getArea());
			customerDtlsDto.setPinCode(rlmsBranchCustomerMap.getCustomerMaster().getPincode());
			customerDtlsDto.setChairmanName(rlmsBranchCustomerMap.getCustomerMaster().getChairmanName());
			customerDtlsDto.setChairmanNumber(rlmsBranchCustomerMap.getCustomerMaster().getChairmanNumber());
			customerDtlsDto.setChairmanEmail(rlmsBranchCustomerMap.getCustomerMaster().getChairmanEmail());
			customerDtlsDto.setTreasurerName(rlmsBranchCustomerMap.getCustomerMaster().getTreasurerName());
			customerDtlsDto.setTreasurerNumber(rlmsBranchCustomerMap.getCustomerMaster().getTreasurerNumber());
			customerDtlsDto.setTreasurerEmail(rlmsBranchCustomerMap.getCustomerMaster().getTreasurerEmail());
			customerDtlsDto.setSecretaryName(rlmsBranchCustomerMap.getCustomerMaster().getSecretaryName());
			customerDtlsDto.setSecretaryNumber(rlmsBranchCustomerMap.getCustomerMaster().getSecretaryNumber());
			customerDtlsDto.setSecretaryEmail(rlmsBranchCustomerMap.getCustomerMaster().getSecretaryEmail());
			customerDtlsDto.setWatchmenName(rlmsBranchCustomerMap.getCustomerMaster().getWatchmenName());
			customerDtlsDto.setWatchmenNumber(rlmsBranchCustomerMap.getCustomerMaster().getWatchmenNumber());
			customerDtlsDto.setWatchmenEmail(rlmsBranchCustomerMap.getCustomerMaster().getWatchmenEmail());
			listOFDtos.add(customerDtlsDto);
		}
		return listOFDtos;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndRegisterNewMember(MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo) throws ValidationException{
		String statusMessage = "";
		if(this.validateMemberDtls(memberDtlsDto)){
			RlmsMemberMaster  memberMaster = this.constructMemberMaster(memberDtlsDto, metaInfo);
			Integer memberID = this.customerDao.saveMemberM(memberMaster);
			memberMaster.setMemberId(memberID);
			
			RlmsCustomerMemberMap customerMemberMap = this.constructCustoMemberMap(memberMaster, memberDtlsDto, metaInfo);
			this.customerDao.saveCustomerMemberMap(customerMemberMap);
			
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.MEMBER_REG_SUCCESSFUL.getMessage());
		}
		return statusMessage;
		
	}
	
	private boolean validateMemberDtls(MemberDtlsDto memberDtlsDto) throws ValidationException{
		boolean isValidMember = true;
		RlmsMemberMaster memberMaster = this.customerDao.getMemberByCntNo(memberDtlsDto.getContactNumber());
		if(null != memberMaster){
			isValidMember = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.MEMBER_WITH_SAME_CONTACT_NO.getMessage()));
		}
		
		return isValidMember;
	}
	
	private RlmsMemberMaster constructMemberMaster(MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo){
	
		RlmsMemberMaster memberMaster = new RlmsMemberMaster();
		memberMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		memberMaster.setAddress(memberDtlsDto.getAddress());
		memberMaster.setContactNumber(memberDtlsDto.getContactNumber());
		memberMaster.setEmailId(memberDtlsDto.getEmailId());
		memberMaster.setFirstName(memberDtlsDto.getFirstName());
		memberMaster.setLastName(memberDtlsDto.getLastName());
		memberMaster.setCity(memberDtlsDto.getCity());
		memberMaster.setArea(memberDtlsDto.getArea());
		memberMaster.setPincode(memberDtlsDto.getPinCode());
		memberMaster.setUpdatedBy(metaInfo.getUserId());
		memberMaster.setUpdatedDate(new Date());
		memberMaster.setCreatedBy(metaInfo.getUserId());
		memberMaster.setCreatedDate(new Date());
		memberMaster.setRegistrationDate(new Date());
		return memberMaster;
	}
	
	private RlmsCustomerMemberMap constructCustoMemberMap(RlmsMemberMaster memberMaster, MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo){
		RlmsCustomerMemberMap customerMemberMap =  new RlmsCustomerMemberMap();
		RlmsBranchCustomerMap branchCustomerMap = this.branchDao.getBranchCustomerMapDtls(memberDtlsDto.getBranchCustoMapId());
		customerMemberMap.setActiveFlag(RLMSConstants.ACTIVE.getId());
		customerMemberMap.setRlmsBranchCustomerMap(branchCustomerMap);
		customerMemberMap.setRlmsMemberMaster(memberMaster);
		customerMemberMap.setCreatedBy(metaInfo.getUserId());
		customerMemberMap.setCreatedDate(new Date());
		customerMemberMap.setUpdatedBy(metaInfo.getUserId());
		customerMemberMap.setUpdatedDate(new Date());
		return customerMemberMap;

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerDtlsDto getCustomerDtlsById(Integer branchCustomerMapId){
		RlmsBranchCustomerMap branchCustomerMap = this.branchDao.getBranchCustomerMapDtls(branchCustomerMapId);
		CustomerDtlsDto dto = new CustomerDtlsDto();
		if(null != branchCustomerMap){
			dto.setCompanyId(branchCustomerMap.getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyId());
			dto.setCompanyName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			dto.setBranchCompanyMapId(branchCustomerMap.getCompanyBranchMapDtls().getCompanyBranchMapId());
			dto.setBranchName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
		}
		return dto;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public MemberDtlsDto registerMemeberDeviceByMblNo(MemberDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException{
		RlmsMemberMaster memberMaster = this.customerDao.getMemberByCntNo(dto.getContactNumber());
		if(null == memberMaster){
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.INVALID_CONTACT_NUMBER.getMessage()));
		}
		this.registerUserDevice(dto, memberMaster, metaInfo);
		return this.constructMemberDltsSto(memberMaster);
		
	}
	
	
	private void registerUserDevice(MemberDtlsDto dto, RlmsMemberMaster memberMaster, UserMetaInfo metaInfo){
		
		RlmsUserApplicationMapDtls esistAppDtls = this.customerDao.getUserAppDtls(memberMaster.getMemberId(), RLMSConstants.MEMBER_TYPE.getId());
		if(null != esistAppDtls){
			if(!esistAppDtls.getAppRegId().equalsIgnoreCase(dto.getAppRegId())){
				esistAppDtls.setAppRegId(dto.getAppRegId());
				this.userRoleDao.mergeUserAppDlts(esistAppDtls);
			}
		}else{
			RlmsUserApplicationMapDtls userApplicationMapDtls = this.constructUserAppMapDtls(dto, memberMaster, metaInfo);
			this.userRoleDao.saveUserAppDlts(userApplicationMapDtls);
		}
		
	}
	
	private RlmsUserApplicationMapDtls constructUserAppMapDtls(MemberDtlsDto dto, RlmsMemberMaster memberMaster, UserMetaInfo metaInfo){
		RlmsUserApplicationMapDtls userApplicationMapDtls = new RlmsUserApplicationMapDtls();
		userApplicationMapDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		userApplicationMapDtls.setAppRegId(dto.getAppRegId());
		userApplicationMapDtls.setLatitude(dto.getLatitude());
		userApplicationMapDtls.setLongitude(dto.getLongitude());
		userApplicationMapDtls.setAddress(dto.getAddress());
		userApplicationMapDtls.setUserId(memberMaster.getMemberId());
		userApplicationMapDtls.setUserRefType(RLMSConstants.MEMBER_TYPE.getId());
		userApplicationMapDtls.setCreatedBy(metaInfo.getUserId());
		userApplicationMapDtls.setCreatedDate(new Date());
		userApplicationMapDtls.setUpdatedDate(new Date());
		userApplicationMapDtls.setUpdatedBy(metaInfo.getUserId());
		return userApplicationMapDtls;
	}
	
	private MemberDtlsDto constructMemberDltsSto(RlmsMemberMaster memberMaster){
		
		List<RlmsCustomerMemberMap> listOFAllCustomers = this.customerDao.getAllCustomersForMember(memberMaster.getMemberId());
		MemberDtlsDto memberDtlsDto = new MemberDtlsDto();
		memberDtlsDto.setFirstName(memberMaster.getFirstName());
		memberDtlsDto.setLastName(memberMaster.getLastName());
		memberDtlsDto.setContactNumber(memberMaster.getContactNumber());
		memberDtlsDto.setMemberId(memberMaster.getMemberId());
		memberDtlsDto.setListOfCustomerDtls(this.constructCustoDtlsDto(listOFAllCustomers));
		return memberDtlsDto;
		
	}
	
	private List<CustomerDtlsDto> constructCustoDtlsDto(List<RlmsCustomerMemberMap> listOfAllCustomers){
		List<CustomerDtlsDto> listOfCusoDtls = new ArrayList<CustomerDtlsDto>();
		for (RlmsCustomerMemberMap customerMemberMap : listOfAllCustomers) {
			RlmsBranchCustomerMap branchCustomerMap = this.customerDao.getBranchCustomerMapByCustoId(customerMemberMap.getRlmsBranchCustomerMap().getCustomerMaster().getCustomerId());
			CustomerDtlsDto customerDtlsDto =  new CustomerDtlsDto();
			customerDtlsDto.setBranchCompanyMapId(branchCustomerMap.getBranchCustoMapId());
			customerDtlsDto.setBranchName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			customerDtlsDto.setCustomerName(branchCustomerMap.getCustomerMaster().getCustomerName());
			customerDtlsDto.setCompanyName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			listOfCusoDtls.add(customerDtlsDto);
		}
		return listOfCusoDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MemberDtlsDto> getListOfAllMemberDtls(MemberDtlsDto memberDtlsDto){
		List<RlmsCustomerMemberMap> listOfAllMembers = this.customerDao.getAllMembersForCustomer(memberDtlsDto.getBranchCustoMapId());
		List<MemberDtlsDto> listOfMemberDtls= new ArrayList<MemberDtlsDto>();
		for (RlmsCustomerMemberMap rlmsCustomerMemberMap : listOfAllMembers) {
			
			MemberDtlsDto dto = new MemberDtlsDto();
			dto.setBranchName(rlmsCustomerMemberMap.getRlmsBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			dto.setCompanyName(rlmsCustomerMemberMap.getRlmsBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			dto.setCustomerName(rlmsCustomerMemberMap.getRlmsBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setContactNumber(rlmsCustomerMemberMap.getRlmsMemberMaster().getContactNumber());
			dto.setFirstName(rlmsCustomerMemberMap.getRlmsMemberMaster().getFirstName());
			dto.setLastName(rlmsCustomerMemberMap.getRlmsMemberMaster().getLastName());
			dto.setRegistrationDate(rlmsCustomerMemberMap.getRlmsMemberMaster().getRegistrationDate());
			dto.setCity(rlmsCustomerMemberMap.getRlmsMemberMaster().getCity());
			dto.setArea(rlmsCustomerMemberMap.getRlmsMemberMaster().getArea());
			dto.setPinCode(rlmsCustomerMemberMap.getRlmsMemberMaster().getPincode());
			dto.setEmailId(rlmsCustomerMemberMap.getRlmsMemberMaster().getEmailId());
			dto.setAddress(rlmsCustomerMemberMap.getRlmsMemberMaster().getAddress());
			
			listOfMemberDtls.add(dto);
		}
		return listOfMemberDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UserAppDtls getUserAppDtls(Integer userId, Integer userType){
		UserAppDtls userAppDtls = new UserAppDtls();
		RlmsUserApplicationMapDtls userApplicationMapDtls = this.customerDao.getUserAppDtls(userId, userType);
		if(null != userApplicationMapDtls){
			
			userAppDtls.setAppRegId(userApplicationMapDtls.getAppRegId());
			userAppDtls.setUserId(userId);
			userAppDtls.setUserType(userType);
			userAppDtls.setLatitude(userApplicationMapDtls.getLatitude());
			userAppDtls.setLongitude(userApplicationMapDtls.getLongitude());
			userAppDtls.setAddress(userApplicationMapDtls.getAddress());
		}
		return userAppDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsCustomerMemberMap> getAllMembersForCustomer(
			Integer branchCustomerMapId){
		return this.customerDao.getAllMembersForCustomer(branchCustomerMapId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getAllLiftsForMember(Integer memberId){
		List<LiftDtlsDto> listOfLiftDtls = new ArrayList<LiftDtlsDto>();
		List<RlmsCustomerMemberMap> listOfAllCustomers = this.customerDao.getAllCustomersForMember(memberId);
		List<Integer> listOfAllBranchCustomerMapIds = new ArrayList<Integer>();
		for (RlmsCustomerMemberMap customerMemberMap : listOfAllCustomers) {
			listOfAllBranchCustomerMapIds.add(customerMemberMap.getRlmsBranchCustomerMap().getBranchCustoMapId());
		}
		if(null != listOfAllBranchCustomerMapIds && !listOfAllBranchCustomerMapIds.isEmpty()){
			List<RlmsLiftCustomerMap> allLiftsForCustomes = this.liftDao.getAllLiftsForCustomres(listOfAllBranchCustomerMapIds);
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : allLiftsForCustomes) {
				LiftDtlsDto dto = new LiftDtlsDto();
				dto.setLiftCustomerMapId(rlmsLiftCustomerMap.getLiftCustomerMapId());
				dto.setLiftNumber(rlmsLiftCustomerMap.getLiftMaster().getLiftNumber());
				listOfLiftDtls.add(dto);
			}
		}
		return listOfLiftDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsMemberMaster getMemberById(Integer memeberId){
		return this.customerDao.getMemberById(memeberId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDtlsDto> getCustomerByName(String custoName, UserMetaInfo metaInfo){
		Integer companyBranchMapId = null;
		List<RlmsBranchCustomerMap> listOFAllCustomers = null;
		List<CustomerDtlsDto> listOFCustomerDtls = new ArrayList<CustomerDtlsDto>();
		if(null != metaInfo){
			if(SpocRoleConstants.INDITECH_ADMIN.getRoleLevel().equals(metaInfo.getUserRole().getRlmsSpocRoleMaster().getRoleLevel())){
				listOFAllCustomers = this.customerDao.getCustomerByName(custoName, null, null);
			}else {
				if(null != metaInfo.getUserRole().getRlmsCompanyBranchMapDtls()){
					companyBranchMapId = metaInfo.getUserRole().getRlmsCompanyBranchMapDtls().getCompanyBranchMapId();
				}
				listOFAllCustomers = this.customerDao.getCustomerByName(custoName, companyBranchMapId, metaInfo.getUserRole().getRlmsCompanyMaster().getCompanyId());
			}
		}
		
		if(null != listOFAllCustomers){
			for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOFAllCustomers) {
				CustomerDtlsDto dto = new CustomerDtlsDto();
				dto.setCustomerName(rlmsBranchCustomerMap.getCustomerMaster().getCustomerName());
				dto.setBranchCustomerMapId(rlmsBranchCustomerMap.getBranchCustoMapId());
				listOFCustomerDtls.add(dto);
			}
		}
		return listOFCustomerDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDtlsDto> getAllApplicableCustomersForDashboard(List<Integer> companyBranchIds, UserMetaInfo metaInfo){
		List<RlmsBranchCustomerMap> listOfAllCustomers = this.customerDao.getAllCustomersForDashboard(companyBranchIds);
		return this.constructListOfCustomerDtlsDto(listOfAllCustomers);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getAllLiftParameters(Integer liftCustomerMapId){
		List<LiftDtlsDto> listOfLiftDtls = new ArrayList<LiftDtlsDto>();
		List<Integer> liftCustMapIds = new ArrayList<>();
		liftCustMapIds.add(liftCustomerMapId);
		List<RlmsLiftCustomerMap> allLiftsForCustomes = this.liftDao
				.getAllLiftsByIds(liftCustMapIds);
		for (RlmsLiftCustomerMap liftCustomerMap : allLiftsForCustomes) {
			RlmsLiftMaster liftM = liftCustomerMap.getLiftMaster();
			LiftDtlsDto dto = new LiftDtlsDto();
			dto.setAccessControl(liftM.getAccessControl());
			dto.setAddress(liftM.getAddress());
			dto.setArea(liftM.getArea());
			dto.setPinCode(liftM.getPincode());
			dto.setCity(liftM.getCity());
			dto.setAlarm(liftM.getAlarm());
			dto.setAlarmBattery(liftM.getAlarmBattery());
			dto.setAmcAmount(liftM.getAmcAmount());
			dto.setAmcStartDate(liftM.getAmcStartDate());
			dto.setDoorType(liftM.getDoorType());
			dto.setLatitude(liftM.getLatitude());
			dto.setLongitude(liftM.getLongitude());
			dto.setNoOfStops(liftM.getNoOfStops());
			dto.setEngineType(liftM.getEngineType());
			dto.setMachineMake(liftM.getMachineMake());
			dto.setMachineCapacity(liftM.getMachineCapacity());
			dto.setMachineCurrent(liftM.getMachineCurrent());
			dto.setMachinePhoto(liftM.getMachinePhoto());
			dto.setBreakVoltage(liftM.getBreakVoltage());
			dto.setPanelMake(liftM.getPanelMake());
			dto.setPanelPhoto(liftM.getPanelPhoto());
			dto.setNoOfBatteries(liftM.getNoOfBatteries());
			dto.setCopMake(liftM.getCOPMake());
			dto.setCopPhoto(liftM.getCOPPhoto());
			dto.setLopMake(liftM.getLOPMake());
			dto.setLopPhoto(liftM.getLOPPhoto());
			dto.setCollectiveType(liftM.getCollectiveType());
			dto.setSimplexDuplex(liftM.getSimplexDuplex());
			dto.setCartopPhoto(liftM.getCartopPhoto());
			dto.setAutoDoorMake(liftM.getAutoDoorMake());
			dto.setWiringShceme(liftM.getWiringShceme());
			dto.setWiringPhoto(liftM.getWiringPhoto());
			dto.setFireMode(liftM.getFireMode());
			dto.setIntercomm(liftM.getIntercomm());
			dto.setLobbyPhoto(liftM.getLobbyPhoto());
			if(null != liftM.getAmcStartDate()){
				dto.setAmcStartDateStr(DateUtils.convertDateToStringWithoutTime(liftM.getAmcStartDate()));
			}
			dto.setAmcType(liftM.getAmcType());
			
			if(liftM.getAmcType()!=null){
				if (AMCType.COMPREHENSIVE.getId() == liftM.getAmcType()) {
					dto.setAmcTypeStr(AMCType.COMPREHENSIVE.getType());
				} else if (AMCType.NON_COMPREHENSIVE.getId() == liftM
						.getAmcType()) {
					dto.setAmcTypeStr(AMCType.NON_COMPREHENSIVE.getType());
				} else if (AMCType.ON_DEMAND.getId() == liftM.getAmcType()) {
					dto.setAmcTypeStr(AMCType.ON_DEMAND.getType());
				} else if (AMCType.OTHER.getId() == liftM.getAmcType()) {
					dto.setAmcTypeStr(AMCType.OTHER.getType());
				}
			}
			
			dto.setArd(liftM.getARD());
			dto.setArdPhoto(liftM.getARDPhoto());
			dto.setAutoDoorHeaderPhoto(liftM.getAutoDoorHeaderPhoto());
			dto.setBatteryCapacity(liftM.getBatteryCapacity());
			dto.setBatteryMake(liftM.getBatteryMake());
			dto.setBranchName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());;
			dto.setCustomerName(liftCustomerMap.getBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setDateOfInstallation(liftM.getDateOfInstallation());
			if(null != liftM.getDateOfInstallation()){
				dto.setDateOfInstallationStr(DateUtils.convertDateToStringWithoutTime(liftM.getDateOfInstallation()));
			}
			dto.setLiftNumber(liftM.getLiftNumber());
			dto.setServiceStartDate(liftM.getServiceStartDate());
			if(null != liftM.getServiceStartDate()){
				dto.setServiceStartDateStr(DateUtils.convertDateToStringWithoutTime(liftM.getServiceStartDate()));
			}
			dto.setServiceEndDate(liftM.getServiceEndDate());
			if(null != liftM.getServiceEndDate()){
				dto.setServiceEndDateStr(DateUtils.convertDateToStringWithoutTime(liftM.getServiceEndDate()));
			}
			listOfLiftDtls.add(dto);
		}
		return listOfLiftDtls;
	}
}

