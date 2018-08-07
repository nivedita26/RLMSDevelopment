package com.rlms.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.rlms.constants.RLMSCallType;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.LoginDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.SiteVisitDtlsDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.ComplaintsService;
import com.rlms.service.CustomerService;
import com.rlms.service.DashboardService;
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
    public @ResponseBody ResponseDto getAllComplaintsAssigned(@RequestBody LoginDtlsDto loginDtlsDto) {
     	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto dto = new ResponseDto();
     	List<ComplaintsDto> listOfAllAssignedComplaints = null;
    	 List<Integer> statusList = new ArrayList<Integer>();
    	 statusList.add(Status.ASSIGNED.getStatusId());
    	 statusList.add(Status.INPROGESS.getStatusId());
    	 statusList.add(Status.COMPLETED.getStatusId());
    	 try {
    		 listOfAllAssignedComplaints =  this.ComplaintsService.getAllComplaintsAssigned(Integer.valueOf(loginDtlsDto.getUserRoleId()), statusList);
    		 if(null != listOfAllAssignedComplaints && !listOfAllAssignedComplaints.isEmpty()){
	    		 dto.setStatus(true);    
	    		 dto.setResponse(mapper.writeValueAsString(listOfAllAssignedComplaints));
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
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
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
    
    
    
    /*
    @RequestMapping(value = "/downloadUserGuide", method = RequestMethod.POST)
	public String downloadUserGuide(int liftCustomerMapId,HttpServletResponse response) throws FileNotFoundException, SQLException, IOException {
	int directoryfileCount;
	final String OUTPUT_ZIP_FILE;
	final String INTERNAL_FILE_PATH;
	InputStream inputStream=null ;
	int filestatus = restapidao.getfileStatus(deviceid);
	if (filestatus >0){
		File zipfilename=new File(deviceid + ".zip");
		if(zipfilename.exists()){
			zipfilename.delete();
			logger.debug("previously present zip file deleted");
		}
		response.setContentType("application/zip");
		OUTPUT_ZIP_FILE = Constant.zipfilepath + deviceid+ ".zip";
		logger.debug("OUTPUT_ZIP_FILE "+OUTPUT_ZIP_FILE);
		
		directoryfileCount = getFileCountInDirectory(Constant.filepath + deviceid);//to avoid empty zip
		logger.debug("no of files to zip in productId"+deviceid+"filecount:"+directoryfileCount);
		
		if (directoryfileCount>0){
			Set<String> fileList = new HashSet<String>();
				*//**
				 * Generate List of files
				 *//*
			Set<String> zipfileList = generateFileList(new File(Constant.filepath + deviceid ),fileList);
				*//**
				 * Zip all files
				 *//*
			zipIt(OUTPUT_ZIP_FILE,deviceid,zipfileList);
			INTERNAL_FILE_PATH=Constant.zipfilepath + deviceid + ".zip";
					
			logger.debug("Internal zip file path"+ INTERNAL_FILE_PATH);
			File file = new File(INTERNAL_FILE_PATH);
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName()+"\"" ));
			logger.debug("set response header");
			response.setContentLength((int) file.length());
			logger.debug("response length"+file.length());
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
				logger.debug("inputstream"+file);
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.debug("Response to App : Zip file for " + deviceid  + " sent successfully.");
			File zipfile = new File(INTERNAL_FILE_PATH);
			if(zipfile.exists()){
				zipfile.delete();   
				logger.debug("zip file deleted"+zipfile);
				File productDirectoryPath = new File(Constant.filepath + deviceid);
				if(productDirectoryPath.exists()){
					FileUtils.cleanDirectory(productDirectoryPath);
					FileUtils.deleteDirectory(productDirectoryPath);
				}
			}
			return "";
		} 
		else {
			logger.debug(" " +  deviceid  + " data  not present in directory.");
			return "";
		} 
	}
	logger.debug("Response Sent : " + deviceid + " is not present in Database.");
	return "";
	}*/
    
}