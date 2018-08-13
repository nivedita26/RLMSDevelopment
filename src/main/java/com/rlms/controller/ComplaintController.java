package com.rlms.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.service.CompanyService;
import com.rlms.service.ComplaintsService;
import com.rlms.service.CustomerService;
import com.rlms.service.DashboardService;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/complaint")
public class ComplaintController extends BaseController{

	@Autowired
	private ComplaintsService complaintsService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private DashboardService dashboardService;
		
	private static final Logger logger = Logger.getLogger(ComplaintController.class);
	
	@RequestMapping(value = "/getListOfComplaints", method = RequestMethod.POST)
	 public  @ResponseBody List<ComplaintsDto> getListOfComplaints(@RequestBody ComplaintsDtlsDto dto) throws RunTimeException{
		 List<ComplaintsDto> listOfComplaints = null;
    	List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
    		List<Integer> companyBranchMapIds = new ArrayList<>();
		 try{
	        	logger.info("Method :: getListOfComplaints");
	        	if(dto.getBranchCompanyMapId()==null ) {
	        		listOfAllBranches = companyService.getAllBranches(dto.getCompanyId());
	        		if(listOfAllBranches!=null  && !listOfAllBranches.isEmpty()) {
	        		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
	        			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
	        		}
	        		}
	        	}
	        	else {
	        		companyBranchMapIds.add(dto.getBranchCompanyMapId());
	        	}
	        		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
	        		List<Integer> liftCustomerMapIds = new ArrayList<>();
	        		if(allCustomersForBranch!=null && !allCustomersForBranch.isEmpty()) {
	        		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
	        			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
	        			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
	        			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
	        			if(list!=null && !list.isEmpty()) {
	        			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
	        				liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
	        			}
	        			}
	        			}
	        			}
	        		if(liftCustomerMapIds!=null && !liftCustomerMapIds.isEmpty()) {
	        			dto.setListOfLiftCustoMapId(liftCustomerMapIds);
	        		}
	        		if(dto.getFromDate()==null ||dto.getToDate()==null){
	        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        			Date pivotDate = DateUtils.addDaysToDate(new Date(), -30);
	        			Date today =new Date();
	        			pivotDate=sdf.parse(sdf.format(pivotDate));
	        			today = sdf.parse(sdf.format(today));
	        			dto.setFromDate(pivotDate);
	        			dto.setToDate(today);
	        		}
	        		listOfComplaints = this.complaintsService.getListOfComplaintsBy(dto);
		 }
	       	 
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	        return listOfComplaints;
	 }
	
	@RequestMapping(value = "/validateAndRegisterNewComplaint", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto validateAndRegisterNewComplaint(@RequestBody ComplaintsDtlsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
		 try{
	        	logger.info("Method :: validateAndRegisterNewComplaint");
	        	reponseDto.setStatus(true);
	        	reponseDto.setResponse(this.complaintsService.validateAndRegisterNewComplaint(dto, this.getMetaInfo()));
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	        return reponseDto;
	 }
	
	@RequestMapping(value = "/assignComplaint", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto assignComplaint(@RequestBody ComplaintsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
		 
		 try{
	        	logger.info("Method :: assignComplaint");
	        	reponseDto.setStatus(true);
	        	reponseDto.setResponse(this.complaintsService.assignComplaint(dto, this.getMetaInfo()));
	        	
	        } catch(ValidationException e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(e.getExceptionCode(), e.getExceptionMessage());
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	 }
	
	@RequestMapping(value = "/getAllApplicableLifts", method = RequestMethod.POST)
	public @ResponseBody List<LiftDtlsDto> getAllApplicableLifts(@RequestBody LiftDtlsDto dto) throws RunTimeException{
		List<LiftDtlsDto> listOfLifts = null;
		try{
        	logger.info("Method :: assignComplaint");
        	listOfLifts = this.complaintsService.getAllLiftsForBranchsOrCustomer(dto);
        }
        catch(Exception e){
        	logger.error(ExceptionUtils.getFullStackTrace(e));
        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
		return listOfLifts;
	}
	
	@RequestMapping(value = "/getAllTechniciansToAssignComplaint", method = RequestMethod.POST)
	public @ResponseBody List<UserRoleDtlsDTO> getAllTechniciansToAssignComplaint(@RequestBody ComplaintsDtlsDto complaintsDtlsDto){
		List<UserRoleDtlsDTO> listOftechnicians = null;
		try{
        	logger.info("Method :: assignComplaint");
        	listOftechnicians = this.complaintsService.getAllTechniciansToAssignComplaint(complaintsDtlsDto);
        	
        }
        catch(Exception e){
        	logger.error(ExceptionUtils.getFullStackTrace(e));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
		return listOftechnicians;
	}
	
	@RequestMapping(value = "/getCustomerByName", method = RequestMethod.POST)
	public @ResponseBody List<CustomerDtlsDto> getCustomerByName(@RequestBody CustomerDtlsDto customerDtlsDto){
		List<CustomerDtlsDto> listOFAllCustomers = null;
		try{
        	logger.info("Method :: getCustomerByName");
        	listOFAllCustomers = this.customerService.getCustomerByName(customerDtlsDto.getCustomerName(), this.getMetaInfo());
        	
        }
        catch(Exception e){
        	logger.error(ExceptionUtils.getFullStackTrace(e));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
		return listOFAllCustomers;
	}
	
	@RequestMapping(value = "/getCustomerDtlsById", method = RequestMethod.POST)
	public @ResponseBody CustomerDtlsDto getCustomerDtlsById(@RequestBody CustomerDtlsDto dto){
		CustomerDtlsDto customerDtlsDto = null;
		try{
        	logger.info("Method :: getCustomerDtlsById");
        	customerDtlsDto = this.customerService.getCustomerDtlsById(dto.getBranchCustomerMapId());
        }
        catch(Exception e){
        	logger.error(ExceptionUtils.getFullStackTrace(e));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
		return customerDtlsDto;
	}
	
	@RequestMapping(value = "/validateAndUpdateComplaint", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto validateAndUpdateComplaint(@RequestBody ComplaintsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
		 try{
	        	logger.info("Method :: validateAndRegisterNewComplaint");
	        	reponseDto.setStatus(true);
	        	reponseDto.setResponse(this.complaintsService.validateAndUpdateComplaint(dto, this.getMetaInfo()));
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	        return reponseDto;
	 }
	
	@RequestMapping(value = "/validateAndUpdateComplaints", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto validateAndUpdateComplaints(@RequestBody ComplaintsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
		 try{
	        	logger.info("Method :: validateAndRegisterNewComplaint");
	        	reponseDto.setStatus(true);
	        	reponseDto.setResponse(this.complaintsService.validateAndUpdateComplaints(dto, this.getMetaInfo()));
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	        return reponseDto;
	 }
}
