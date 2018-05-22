package com.rlms.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.AddNewUserDto;
import com.rlms.contract.BranchDtlsDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.RegisterDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchMaster;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsSpocRoleMaster;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.service.CompanyService;
import com.rlms.service.ComplaintsService;
import com.rlms.service.CustomerService;
import com.rlms.service.LiftService;
import com.rlms.service.UserService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LiftService liftService;
	
	@Autowired
	private ComplaintsService complaintsService;
	
	private static final Logger logger = Logger.getLogger(AdminController.class);
	
	 @RequestMapping(value = "/addNewCompany", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto addNewCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException, ValidationException {
	        System.out.println("Adding n ew Company");
	        ResponseDto reponseDto = new ResponseDto();
	        try{
	        	logger.info("In addNewCompany method");
	        	reponseDto.setResponse(this.companyService.validateAndSaveCompanyObj(companyDtlsDTO, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));	        	
	        	throw vex;
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	    }
	 
	 @RequestMapping(value = "/getApplicableRoles", method = RequestMethod.POST)
	    public @ResponseBody List<RlmsSpocRoleMaster> getApplicableRoles() throws RunTimeException {
	        List<RlmsSpocRoleMaster> listOfAllRoles = null;
	        
	        try{
	        	logger.info("Method :: getAllActiveRoles");
	        	listOfAllRoles =  this.userService.getAllRoles(this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfAllRoles;
	 }
	 
	 @RequestMapping(value = "/getAllUsersForCompany", method = RequestMethod.POST)
	    public @ResponseBody List<UserDtlsDto> getAllUsersForCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException {
	        List<UserDtlsDto> listOfAllUsers = null;
	        
	        try{
	        	logger.info("Method :: getAllUsersForCompany");
	        	listOfAllUsers =  this.userService.getAllUsersForCompany(companyDtlsDTO.getCompanyId());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfAllUsers;
	    }
	 
	 @RequestMapping(value = "/getAllRegisteredUsers", method = RequestMethod.POST)
	    public @ResponseBody List<UserDtlsDto> getAllRegisteredUsers(@RequestBody BranchDtlsDto dto) throws RunTimeException {
	        List<UserDtlsDto> listOfAllUsers = null;
	        
	        try{
	        	logger.info("Method :: getAllRegisteredUsers");
	        	listOfAllUsers =  this.userService.getAllRegisteredUsers(dto.getCompanyId(), this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfAllUsers;
	    }
	

	 @RequestMapping(value = "/getAllApplicableCompanies", method = RequestMethod.POST)
	    public @ResponseBody List<RlmsCompanyMaster> getAllApplicableCompanies() throws RunTimeException {
	        List<RlmsCompanyMaster> listOfApplicableCompanies = null;
	        
	        try{
	        	logger.info("Method :: getAllApplicableCompanies");
	        	listOfApplicableCompanies =  this.companyService.getAllCompanies(this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfApplicableCompanies;
	    }
	 //Thgis function will return details of each company to show on company page table
	 @RequestMapping(value = "/getAllCompanyDetails", method = RequestMethod.POST)
	    public @ResponseBody List<CompanyDtlsDTO> getAllCompanyDetails() throws RunTimeException {
	        List<CompanyDtlsDTO> listOfApplicableCompaniesDetails = null;
	        
	        try{
	        	logger.info("Method :: getAllCompanyDetails");
	        	listOfApplicableCompaniesDetails =  this.companyService.getAllCompanyDetails(this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfApplicableCompaniesDetails;
	    }
	 
	 @RequestMapping(value = "/assignRole", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto assignRole(@RequestBody UserRoleDtlsDTO userRoleDtlsDTO) throws RunTimeException, ValidationException {
	        
		 	ResponseDto reponseDto = new ResponseDto();
	        try{
	        	logger.info("Method :: assignRole");
	        	reponseDto.setResponse(this.userService.validateAndAssignRole(userRoleDtlsDTO, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));
	        	throw vex;
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	    }
	 
	 @RequestMapping(value = "/getAllBranchesForCompany", method = RequestMethod.POST)
	    public @ResponseBody List<RlmsCompanyBranchMapDtls> getAllBranchesForCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException {
	        List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
	        
	        try{
	        	logger.info("Method :: getAllBranchesForCompany");
	        	listOfAllBranches =  this.companyService.getAllBranches(companyDtlsDTO.getCompanyId());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfAllBranches;
	    }
	 
	 @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto registerUser(@RequestBody RegisterDto registerDto) throws RunTimeException {
		 	ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: registerUser");
	        	reponseDto.setResponse(this.userService.registerUser(registerDto));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 @RequestMapping(value = "/validateAndRegisterNewUser", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto validateAndRegisterNewUser(@RequestBody AddNewUserDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: validateAndRegisterNewUser");
	        	reponseDto.setResponse(this.userService.validateAndRegisterNewUser(dto, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));
	        	throw vex;
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 
	 @RequestMapping(value = "/validateAndRegisterNewCustomer", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto validateAndRegisterNewCustomer(@RequestBody CustomerDtlsDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: validateAndRegisterNewCustomer");
	        	reponseDto.setResponse(this.customerService.validateAndRegisterNewCustomer(dto, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));
	        	throw vex;
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 
	 @RequestMapping(value = "/addNewBranchInCompany", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto addNewBranchInCompany(@RequestBody BranchDtlsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: addNewBranchInCompany");
	        	reponseDto.setResponse(this.companyService.validateAndAddNewBranchInCompany(dto, this.getMetaInfo()));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	 }
	 
	 @RequestMapping(value = "/getListOfBranchDtls", method = RequestMethod.POST)
	 public @ResponseBody List<BranchDtlsDto> getListOfBranchDtls(@RequestBody BranchDtlsDto dto) throws RunTimeException{
		 List<BranchDtlsDto> listOfBranches = null;
	        
	        try{
	        	logger.info("Method :: getListOfBranchDtls");
	        	listOfBranches = this.companyService.getListOfBranchDtls(dto.getCompanyId(), this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        	
	        }
	 
	        return listOfBranches;
	 }
	 
	 @RequestMapping(value = "/getListOfCustomerDtls", method = RequestMethod.POST)
	 public @ResponseBody List<CustomerDtlsDto> getListOfCustomerDtls(@RequestBody CustomerDtlsDto customerDtlsDto) throws RunTimeException {
		 List<CustomerDtlsDto> listOfCustomers = null;
	        
	        try{
	        	logger.info("Method :: getListOfCustomerDtls");
	        	listOfCustomers = this.customerService.getAllApplicableCustomers(customerDtlsDto, this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        	
	        }
	 
	        return listOfCustomers;
	 }
	 
	 @RequestMapping(value = "/getAllCustomersForBranch", method = RequestMethod.POST)
	 public @ResponseBody List<CustomerDtlsDto> getAllCustomersForBranch(@RequestBody CustomerDtlsDto customerDtlsDto) throws RunTimeException {
		 List<CustomerDtlsDto> listOfCustomers = null;
	        
	        try{
	        	logger.info("Method :: getAllCustomersForBranch");
	        	listOfCustomers = this.customerService.getAllCustomersForBranch(customerDtlsDto, this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        	
	        }
	 
	        return listOfCustomers;
	 }
	 
	 @RequestMapping(value = "/validateAndRegisterNewLift", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto validateAndRegisterNewLift(@RequestBody LiftDtlsDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: validateAndRegisterNewCustomer");
	        	reponseDto.setResponse(this.liftService.validateAndAddNewLiftDtls(dto, this.getMetaInfo()));
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 @RequestMapping(value = "/getLiftsToBeApproved", method = RequestMethod.POST)
	    public @ResponseBody List<LiftDtlsDto> getLiftsToBeApproved() throws RunTimeException{
		 List<LiftDtlsDto> listOfInActiveLifts = new ArrayList<LiftDtlsDto>();
	        
	        try{
	        	logger.info("Method :: getLiftsToBeApproved");
	        	listOfInActiveLifts = this.liftService.getLiftsToBeApproved();
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfInActiveLifts;
	  }
	 
	 @RequestMapping(value = "/validateAndApproveLift", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto validateAndApproveLift(@RequestBody LiftDtlsDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: validateAndApproveLift");
	        	reponseDto.setResponse(this.liftService.approveLift(dto, this.getMetaInfo()));
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 @RequestMapping(value = "/getLiftDetailsForBranch", method = RequestMethod.POST)
	 public @ResponseBody List<LiftDtlsDto> getLiftDetailsForBranch(@RequestBody LiftDtlsDto liftDtlsDto) throws RunTimeException{
		 List<LiftDtlsDto> listOfLifts = new ArrayList<LiftDtlsDto>();
	        
	        try{
	        	logger.info("Method :: getLiftDetailsForBranch");
	        	listOfLifts = this.liftService.getLiftDetailsForBranch(liftDtlsDto, this.getMetaInfo());
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfLifts;
	 }
	 
	 @RequestMapping(value = "/validateAndRegisterNewMember", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto validateAndRegisterNewMember(@RequestBody MemberDtlsDto memberDtlsDto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: validateAndRegisterNewMember");
	        	reponseDto.setResponse(this.customerService.validateAndRegisterNewMember(memberDtlsDto, this.getMetaInfo()));
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	 }
	 
	 @RequestMapping(value = "/getListOfAllMemberDtls", method = RequestMethod.POST)
	 public @ResponseBody List<MemberDtlsDto> getListOfAllMemberDtls(@RequestBody MemberDtlsDto memberDtlsDto) throws RunTimeException{
		 List<MemberDtlsDto> listOFMembers = new ArrayList<MemberDtlsDto>();
	        
	        try{
	        	logger.info("Method :: getListOfAllMemberDtls");
	        	listOFMembers = this.customerService.getListOfAllMemberDtls(memberDtlsDto);
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOFMembers;
	 }
	 
	 @RequestMapping(value = "/getAddressDetailsOfLift", method = RequestMethod.POST)
	 public @ResponseBody CustomerDtlsDto getAddressDetailsOfLift(@RequestBody CustomerDtlsDto customerDtlsDto) throws RunTimeException{
		 CustomerDtlsDto dto = null;
		 
		 try{
	        	logger.info("Method :: getAddressDetailsOfLift");
	        	dto = this.liftService.getAddressDetailsOfLift(customerDtlsDto.getBranchCustomerMapId());
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return dto;
	 }
	 
	 @RequestMapping(value = "/getLiftMasterForType", method = RequestMethod.POST)
	 public @ResponseBody LiftDtlsDto getLiftMasterForType(@RequestBody LiftDtlsDto liftDtlsDto) throws RunTimeException{
		 LiftDtlsDto dto = null;
		 
		 try{
	        	logger.info("Method :: getAddressDetailsOfLift");
	        	dto = this.liftService.getLiftMasterForType(liftDtlsDto);
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return dto;
	 }
	 @RequestMapping(value = "/validateAndUpdateUser", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto validateAndUpdateUser(@RequestBody UserDtlsDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: validateAndRegisterNewUser");
	        	reponseDto.setResponse(this.userService.validateAndEditUser(dto, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));
	        	throw vex;
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto deleteUser(@RequestBody UserDtlsDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: deleteUser");
	        	reponseDto.setResponse(this.userService.deleteUserObj(dto, this.getMetaInfo()));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	        return reponseDto;
	  }
	 @RequestMapping(value = "/updateCompany", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto updateCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException, ValidationException {
	        System.out.println("Updating Company");
	        ResponseDto reponseDto = new ResponseDto();
	        try{
	        	logger.info("In updateCompany method");
	        	reponseDto.setResponse(this.companyService.validateAndUpdateCompanyObj(companyDtlsDTO, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));	        	
	        	throw vex;
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	    }
	 
	 @RequestMapping(value = "/deleteCompany", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto deleteCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException, ValidationException {
	        System.out.println("Deleting Company");
	        ResponseDto reponseDto = new ResponseDto();
	        try{
	        	logger.info("In deleteCompany method");
	        	reponseDto.setResponse(this.companyService.validateAndDeleteCompanyObj(companyDtlsDTO, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));	        	
	        	throw vex;
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	    }
	 
	 @RequestMapping(value = "/editBranchInCompany", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto editBranchInCompany(@RequestBody BranchDtlsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: addNewBranchInCompany");
	        	reponseDto.setResponse(this.companyService.editBranchInCompany(dto, this.getMetaInfo()));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	 }
}
