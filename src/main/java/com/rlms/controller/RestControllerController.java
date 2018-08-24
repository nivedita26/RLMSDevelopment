package com.rlms.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.rlms.constants.RLMSCallType;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.SiteVisitDtlsDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.CompanyDao;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftManualMapDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.ComplaintsService;
import com.rlms.service.CustomerService;
import com.rlms.service.DashboardService;
import com.rlms.service.LiftManualService;
import com.rlms.service.LiftService;
import com.rlms.service.RlmsLiftEventService;
import com.rlms.service.UserService;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;

@RestController
@RequestMapping(value="/API")
public class RestControllerController  extends BaseController {

	@Autowired
	private ComplaintsService ComplaintsService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LiftService liftService;
	
	@Autowired
	DashboardService dashboardService;
	
	@Autowired
	LiftDao liftDao;
	
	@Autowired
	RlmsLiftEventService rlmsLiftEventService;
	
	@Autowired
	LiftManualService liftManualService;
	
	@Autowired
	CompanyDao  companyDao;
	
	private static final Logger log = Logger.getLogger(RestControllerController.class);
	   
    
   /*@RequestMapping("/loginIntoApp")
    public @ResponseBody LoginDtlsDto loginIntoApp(@RequestBody LoginDtlsDto loginDtlsDto, HttpServletRequest request, HttpServletResponse response) {
    
    	 UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDtlsDto.getUserName(), loginDtlsDto.getPassword());

    	 LoginDtlsDto dto = new LoginDtlsDto();
    	 UserMetaInfo userMetaInfo = null;
         // generate session if one doesn't exist
         request.getSession();

         token.setDetails(new WebAuthenticationDetails(request));
         try{
        	    Authentication auth = authenticationManager.authenticate(token);

        	    SecurityContextHolder.getContext().setAuthentication(auth);
        	    
        	    SecurityContext context = new SecurityContextImpl();
        	    context.setAuthentication(auth);
        	    
        	    
        	    userMetaInfo =  this.getMetaInfo();
        	    dto.setCompanyId(userMetaInfo.getUserRole().getRlmsCompanyMaster().getCompanyId());
        	    dto.setUserId(userMetaInfo.getUserId());
        	    dto.setUserRoleId(userMetaInfo.getUserRole().getUserRoleId());
        	} catch(Exception e){
        	        e.printStackTrace();
        	}
         
         return dto;
        
    }*/
    
    @RequestMapping("/isLoggedIn")
    public @ResponseBody String isUserAlreadyLoggedIn()
    {
    	 try{
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	
	    	if (!(auth instanceof AnonymousAuthenticationToken)) {
	    	    /* The user is logged in :) */
	    	    return "1";
	    	}else{
	    		return "0";
	    	}
      }catch(Exception e)
    	 {
    	    return "0";
    	 }
    }   
    
    @RequestMapping(value = "/getAllComplaintsAssigned", method = RequestMethod.POST, produces="application/json")
    public @ResponseBody ResponseDto getAllComplaintsAssigned(@RequestBody ComplaintsDtlsDto complaintsDtlsDto) {
     	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto dto = new ResponseDto();
     	List<ComplaintsDto> listOfAllAssignedComplaints = null;
     	List<Integer> statusList = new ArrayList<Integer>();
     	if(complaintsDtlsDto.getStatus()==1) {
    	 statusList.add(Status.ASSIGNED.getStatusId());
    	 statusList.add(Status.INPROGESS.getStatusId());
       	 statusList.add(Status.REASSIGNED.getStatusId());
     	}
     	else {
     		 statusList.add(complaintsDtlsDto.getStatus());
     	}
    	 try {
    		 listOfAllAssignedComplaints =  this.ComplaintsService.getAllComplaintsAssigned(Integer.valueOf(complaintsDtlsDto.getUserRoleId()), statusList);
    		 if(null != listOfAllAssignedComplaints && !listOfAllAssignedComplaints.isEmpty()){
	    		 dto.setStatus(true);    
	    		 dto.setResponse(mapper.writeValueAsString(listOfAllAssignedComplaints));
	    	//	 dto.setResponse(listOfAllAssignedComplaints);
    		 }else{
    			 dto.setStatus(false);
    			 dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.NO_COMPLAINT_ASSIGNED.getMessage()));
    		 }
    	 }catch(Exception e){
    		 dto.setStatus(false);
    		 dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
    		 log.error("some Unknown exception occurs.");
    	 }
    	 return dto;
    }
    @RequestMapping(value = "/updateComplaintStatus", method = RequestMethod.POST)
    public @ResponseBody ResponseDto updateComplaintStatus(@RequestBody ComplaintsDto complaintsDto) {
    
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto dto = new ResponseDto();
    	 try {
    		 dto.setResponse(mapper.writeValueAsString(this.ComplaintsService.updateComplaintStatus(complaintsDto)));
    		 dto.setStatus(true);
    	 }catch(Exception e){
    		 dto.setStatus(false);
    		 log.error("some Unknown exception occurs.");
    		 dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
    	 }
    	 return dto;
    }
    	
    @RequestMapping(value = "/complaint/validateAndRegisterNewComplaint", method = RequestMethod.POST)
    public @ResponseBody ResponseDto validateAndRegisterNewComplaint(@RequestBody ComplaintsDtlsDto dto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
        try{
        	log.info("Method :: validateAndRegisterNewComplaint");
        	reponseDto.setResponse(this.ComplaintsService.validateAndRegisterNewComplaint(dto, null));
        	reponseDto.setStatus(true);
        }catch(ValidationException vex){
        	log.error(ExceptionUtils.getFullStackTrace(vex));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(vex.getExceptionMessage());
        }
        catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        return reponseDto;
    }
    
    @RequestMapping(value = "/register/registerMemeberDeviceByMblNo", method = RequestMethod.POST)
    public @ResponseBody ResponseDto registerMemeberDeviceByMblNo(@RequestBody MemberDtlsDto memberDtlsDto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
    	ObjectMapper mapper = new ObjectMapper();
        try{
        	log.info("Method :: registerMemeberDeviceByMblNo");
        	RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(1);
        	UserMetaInfo metaInfo = new UserMetaInfo();
        	metaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
        	metaInfo.setUserName(userRoles.getRlmsUserMaster().getFirstName());
        	metaInfo.setUserRole(userRoles);
        	MemberDtlsDto dto = this.customerService.registerMemeberDeviceByMblNo(memberDtlsDto, metaInfo);
        	reponseDto.setResponse(mapper.writeValueAsString(dto));
         	reponseDto.setStatus(true);
         }catch(ValidationException vex){
        	log.error(ExceptionUtils.getFullStackTrace(vex));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(vex.getExceptionMessage());
        }
        catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        return reponseDto;
    }
    
    @RequestMapping(value = "/register/registerTechnicianDeviceByMblNo", method = RequestMethod.POST)
    public @ResponseBody ResponseDto registerTechnicianDeviceByMblNo(@RequestBody UserDtlsDto userDtlsDto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
    	ObjectMapper mapper = new ObjectMapper();
        try{
        	log.info("Method :: registerTechnicianDeviceByMblNo");
        	RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(1);
        	UserMetaInfo metaInfo = new UserMetaInfo();
        	metaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
        	metaInfo.setUserName(userRoles.getRlmsUserMaster().getFirstName());
        	metaInfo.setUserRole(userRoles);
        	reponseDto.setResponse(mapper.writeValueAsString(this.userService.registerTechnicianDeviceByMblNo(userDtlsDto, metaInfo)));
        	reponseDto.setStatus(true);
        	
        }catch(ValidationException vex){
        	log.error(ExceptionUtils.getFullStackTrace(vex));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(vex.getExceptionMessage());
        }
        catch(Exception e){
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        return reponseDto;
    }
    @RequestMapping(value = "/register/registerTechnicianDeviceByUserNamePassword", method = RequestMethod.POST)
    public @ResponseBody ResponseDto registerTechnicianDeviceByUserNamePassword(@RequestBody UserDtlsDto userDtlsDto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
    	ObjectMapper mapper = new ObjectMapper();
       try{
        	log.info("Method :: registerTechnicianDeviceByUserNamePassword");
        	log.debug("registerlat"+userDtlsDto.getLatitude());
        	log.debug("registerlong"+userDtlsDto.getLongitude());
        	log.debug("registeradd"+userDtlsDto.getAddress());
        	log.debug("registerAppId"+userDtlsDto.getAppRegId());
        	log.debug("registerUserAppId"+userDtlsDto.getUserRoleId());

        	RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(1);
        	UserMetaInfo metaInfo = new UserMetaInfo();
        	metaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
        	metaInfo.setUserName(userRoles.getRlmsUserMaster().getFirstName());
        	metaInfo.setUserRole(userRoles);
        	UserDtlsDto dtlsDto=this.userService.getTechnicianRoleObjByUserNameAndPassword(userDtlsDto, metaInfo);
        	if(dtlsDto.getMsg()==null) {
        	reponseDto.setResponse(mapper.writeValueAsString(dtlsDto));
        	reponseDto.setStatus(true);
        	}
        	else {
        		reponseDto.setStatus(false);
            	reponseDto.setResponse(dtlsDto.getMsg());
        	}
        }catch(ValidationException vex){
      	log.error(ExceptionUtils.getFullStackTrace(vex));
       	reponseDto.setStatus(false);
      	reponseDto.setResponse(vex.getExceptionMessage());
        }
        catch(Exception e){
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return reponseDto;
    }
    
    @RequestMapping(value = "/lift/getAllLiftsForMember", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getAllLiftsForMember(@RequestBody MemberDtlsDto memberDtlsDto){
    	List<LiftDtlsDto> listOfLiftdtls = null;
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto reponseDto = new ResponseDto();
         try{
         	log.info("Method :: getAllLiftsForMember");
         	 listOfLiftdtls =  this.customerService.getAllLiftsForMember(memberDtlsDto.getMemberId());
         	reponseDto.setResponse(mapper.writeValueAsString(listOfLiftdtls));
         	reponseDto.setStatus(true);
         }
         catch(Exception e){
         	log.error(ExceptionUtils.getFullStackTrace(e));
         	reponseDto.setStatus(false);
         	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
         	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
         }
         return reponseDto;
    }
    @RequestMapping(value = "/complaints/getAllComplaintsByMember", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getAllComplaintsByMember(@RequestBody MemberDtlsDto memberDtlsDto){
    	List<ComplaintsDto> listOfAllComplaints = null;
    	ResponseDto reponseDto = new ResponseDto();
    	ObjectMapper mapper = new ObjectMapper();
        try{
        	log.info("Method :: getAllComplaintsByMembers");
        	listOfAllComplaints =  this.ComplaintsService.getAllComplaintsByMember(memberDtlsDto.getMemberId(),RLMSCallType.USER_RAIGED_CALL_THROUGH_APP.getId());
        	reponseDto.setResponse(mapper.writeValueAsString(listOfAllComplaints));
        	reponseDto.setStatus(true);
        }
        catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        return reponseDto;
    }
    
    @RequestMapping(value = "/lift/uploadPhoto", method = RequestMethod.POST)
    public @ResponseBody ResponseDto uploadPhoto(@RequestBody LiftDtlsDto dto){
    	ResponseDto reponseDto = new ResponseDto();
        try{
        	log.info("Method :: uploadPhoto");
        	reponseDto.setStatus(true);
        	reponseDto.setResponse(this.liftService.uploadPhoto(dto));        	
       
        }catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        return reponseDto;
    }
    
    @RequestMapping(value = "/complaint/validateAndSaveSiteVisitDtls", method = RequestMethod.POST)
    public @ResponseBody ResponseDto validateAndSaveSiteVisitDtls(@RequestBody SiteVisitDtlsDto dto){
    	ResponseDto reponseDto = new ResponseDto();
        try{
        	log.info("Method ::" + dto.getFromDateDtr() + " " + dto.getToDateStr());
        	if(null != dto.getFromDateDtr() && null != dto.getToDateStr()){
        	dto.setFromDate(DateUtils.convertStringToDateWithTime(dto.getFromDateDtr()));
        	dto.setToDate(DateUtils.convertStringToDateWithTime(dto.getToDateStr()));
        	}
        	reponseDto.setResponse(this.ComplaintsService.validateAndSaveSiteVisitDtls(dto));        	
        	reponseDto.setStatus(true);
        }catch(ValidationException e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(e.getExceptionMessage());
        
        }catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        return reponseDto;
    }
    @RequestMapping(value = "/complaint/getAllVisitsForComplaint", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getAllVisitsForComplaint(@RequestBody SiteVisitDtlsDto siteVisitDtlsDto){
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto reponseDto = new ResponseDto();
    	try{
    	 List<SiteVisitDtlsDto> listOfVisists = this.ComplaintsService.getAllVisitsForComplaint(siteVisitDtlsDto);
    	 reponseDto.setStatus(true);
    	 reponseDto.setResponse(mapper.writeValueAsString(listOfVisists));
    	}catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
    	return reponseDto;
    }
    
    @RequestMapping(value = "/complaint/getTechnicianDtls", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getTechnicianDtls(@RequestBody ComplaintsDtlsDto complaintsDtlsDto){
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto reponseDto = new ResponseDto();
    	try{
    	 UserDtlsDto uesrDtlsDto = this.ComplaintsService.getTechnicianDtls(complaintsDtlsDto.getComplaintId());
    	 reponseDto.setStatus(true);
    	 reponseDto.setResponse(mapper.writeValueAsString(uesrDtlsDto));
    	}catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
    	return reponseDto;
    }
    
    @RequestMapping(value = "/lift/updateLiftDetails", method = RequestMethod.POST)
    public @ResponseBody ResponseDto validateAndUpdateLiftDetails(@RequestBody LiftDtlsDto dto) throws RunTimeException, ValidationException {
	 ResponseDto reponseDto = new ResponseDto();
        try{
        	reponseDto.setStatus(true);
        	reponseDto.setResponse(this.liftService.updateLiftDetails(dto, null));
        }
        catch(Exception e){
        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        return reponseDto;
  }
    
    @RequestMapping(value = "/deleteComplaint", method = RequestMethod.POST)
    public @ResponseBody ResponseDto deleteComplaint(@RequestBody ComplaintsDto complaintsDto) {
    
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto dto = new ResponseDto();
    	 try {
    		 dto.setResponse(mapper.writeValueAsString(this.ComplaintsService.deleteComplaint(complaintsDto)));
    		 dto.setStatus(true);
    	 }catch(Exception e){
    		 dto.setStatus(false);
    		 log.error("some Unknown exception occurs.");
    		 dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
    	 }
    	 return dto;
    }
    @RequestMapping(value = "/lift/getLiftParameters", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getLiftParameters(@RequestBody LiftDtlsDto dto) throws RunTimeException, ValidationException {
		List<LiftDtlsDto> listOfLiftdtls = null;
		ObjectMapper mapper = new ObjectMapper();
		ResponseDto reponseDto = new ResponseDto();
		try {
			log.info("Method :: getAllLiftsForMember");
			listOfLiftdtls = this.customerService.getAllLiftParameters(dto.getLiftCustomerMapId());
			reponseDto.setResponse(mapper.writeValueAsString(listOfLiftdtls));
			reponseDto.setStatus(true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			reponseDto.setStatus(false);
			reponseDto
					.setResponse(PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}
		return reponseDto;
  }
    @RequestMapping(value = "/technician/updateTechnicianLocation", method = RequestMethod.POST)
    public @ResponseBody ResponseDto updateTechnicianLocation(@RequestBody UserDtlsDto userDtlsDto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
        try{
        	log.info("Method :: updateTechnicianLocation");
        	log.debug("techlocation");
        	log.debug("techlat"+userDtlsDto.getLatitude());
        	log.debug("techlong"+userDtlsDto.getLongitude());
        	RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(1);
        	UserMetaInfo metaInfo = new UserMetaInfo();
        	metaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
        	metaInfo.setUserName(userRoles.getRlmsUserMaster().getFirstName());
        	metaInfo.setUserRole(userRoles);
        	reponseDto.setResponse(this.userService.updateTechnicianLocation(userDtlsDto, metaInfo));
        	reponseDto.setStatus(true);
        }catch(Exception e){
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return reponseDto;
    }
  
    @RequestMapping(value = "/addEvents ", method = RequestMethod.GET)
    public int addEvents(@RequestParam("from") String msgFrom,@RequestParam("message") String msg   ) {
	 
    	return  rlmsLiftEventService.addEvent(msgFrom,msg);     
    }
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public @ResponseBody ResponseDto logout(@RequestBody UserDtlsDto userDtlsDto) {
	   	
    	return  userService.logout(userDtlsDto);
    }
   
    @RequestMapping(value="/getCustomerListForTechnician", method = RequestMethod.POST)
    public ResponseDto  getCustomerListForTechnician(@RequestBody UserDtlsDto dtlsDto){
    	
    	return   liftService.getLiftDetailsList(dtlsDto);
    }
    @RequestMapping(value = "/lift/getApplicableLifts", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getAllLiftsForTechnician(@RequestBody UserDtlsDto dto){
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto reponseDto = new ResponseDto();
    	try{
    	 List<LiftDtlsDto> listOfApplicableLift = this.liftService.getAllLiftsForTechnician(dto.getUserRoleId());
    	 reponseDto.setStatus(true);
    	 reponseDto.setResponse(mapper.writeValueAsString(listOfApplicableLift));
    	}catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
    	return reponseDto;
    }
    @RequestMapping(value = "/forgotPassword"	, method = RequestMethod.POST)
    public @ResponseBody ResponseDto forgotPassword(@RequestBody UserDtlsDto dto){
    	ResponseDto reponseDto = new ResponseDto();
    	try{
    	  reponseDto.setStatus(true);
    	  reponseDto.setResponse(userService.forgotPassword(dto));
    	}catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
    	return reponseDto;
    }
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public @ResponseBody ResponseDto resetPassword(@RequestBody UserDtlsDto dto){
    	ResponseDto responseDto = new ResponseDto();
    	try{
        	responseDto=userService.changePassword(dto);
    	}catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	responseDto.setStatus(false);
        	responseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
    	return responseDto;
    }

    @RequestMapping(value = "/downloadUserManual", method = RequestMethod.POST)
	public String downloadUserManual(@RequestBody LiftDtlsDto dtlsDto,HttpServletResponse response) throws FileNotFoundException, SQLException, IOException {
    	byte[] userManual = null;
    	RlmsLiftCustomerMap liftCustomerMap = liftDao.getLiftCustomerMapById(dtlsDto.getLiftCustomerMapId());
    	if(liftCustomerMap!=null) {
    		RlmsLiftManualMapDtls liftManualMapDtls =  liftManualService.getLiftManualMapDtls(liftCustomerMap.getLiftCustomerMapId());
    		if(liftManualMapDtls!=null) {
    			 userManual = liftManualMapDtls.getCompanyManual().getUserManual();
    			}
    	    }
           response.setContentType("application/pdf");
           String filename = liftCustomerMap.getLiftMaster().getLiftNumber()+"_"+"usermanual"+"."+"pdf";
           response.setHeader("Content-disposition", "attachment; filename="+ filename);
           OutputStream os=null;
          try {
        	   os = response.getOutputStream();
        	   os.write(userManual);
          } finally {
        	  os.close();
          }
    	/*response.setContentType("application/pdf");
         String filename = liftCustomerMap.getLiftMaster().getLiftNumber()+"_"+"usermanual"+"."+"pdf";
         response.setHeader("Content-disposition", "attachment; filename="+ filename);
    	 File f = new File("C:\\Users\\USER\\Desktop\\response.pdf");
        try {
             OutputStream os = response.getOutputStream();
             InputStream   inputStream = new BufferedInputStream(new FileInputStream(f));
			 FileCopyUtils.copy(userManual,response.getOutputStream());
             os.close();
             inputStream.close();
        } catch (IOException e) { 
            e.printStackTrace();
        }*/
          return "";
    }
        @RequestMapping(value = "/downloadSafetyGuide", method = RequestMethod.POST)
      	public String downloadSafetyGuide(@RequestBody LiftDtlsDto dtlsDto,HttpServletResponse response) throws FileNotFoundException, SQLException, IOException {
          	byte[] safetyGuide = null;
          	RlmsLiftCustomerMap liftCustomerMap = liftDao.getLiftCustomerMapById(dtlsDto.getLiftCustomerMapId());
          	if(liftCustomerMap!=null) {
          		RlmsLiftManualMapDtls liftManualMapDtls =  liftManualService.getLiftManualMapDtls(liftCustomerMap.getLiftCustomerMapId());
          		if(liftManualMapDtls!=null) {
          			safetyGuide = liftManualMapDtls.getCompanyManual().getSafetyGuide();
          			response.setContentType("application/pdf");
          			String filename = liftCustomerMap.getLiftMaster().getLiftNumber()+"_"+"safetyguide"+"."+"pdf";
          			response.setHeader("Content-disposition", "attachment; filename="+ filename);
          			OutputStream os=null;
          			try {
          				os = response.getOutputStream();
          				os.write(safetyGuide);
          			} finally {
          				os.close();
                    }
          		}
          	}
                return "";
      	}
   	 
 	@RequestMapping(value = "/uploadLiftImg", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
 	public @ResponseBody ResponseDto releaseUploadtest( HttpServletRequest request,HttpServletResponse response,@RequestParam("file") MultipartFile uploadFile) {
 	   return liftService.uploadLiftImg(uploadFile,request,this.getMetaInfo());
      }
  }
 