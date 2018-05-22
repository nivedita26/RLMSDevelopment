package com.rlms.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.jdbc.Messages;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RLMSMessages;
//import com.rlms.constants.RLMSMessages;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
//import com.rlms.constants.XMPPServerDetails;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.SiteVisitDtlsDto;
import com.rlms.contract.SiteVisitReportDto;
import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserDtlsDto;
//import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserMetaInfo;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.controller.RestControllerController;
import com.rlms.dao.ComplaintsDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsSiteVisitDtls;
import com.rlms.model.RlmsUserApplicationMapDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;
//import com.telesist.xmpp.AndroidNotificationService;
//import com.telesist.xmpp.AndroidNotificationServiceImpl;
import com.telesist.xmpp.util.Util;

@Service("ComplaintsService")
public class ComplaintsServiceImpl implements ComplaintsService{

	@Autowired
	private ComplaintsDao complaintsDao;
	
	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private MessagingService messagingService;
	
	private static final Logger log = Logger.getLogger(ComplaintsServiceImpl.class);
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList){
		
		List<RlmsComplaintTechMapDtls> listOFAssignedComplaints =  this.complaintsDao.getAllComplaintsAssigned(userRoleId, statusList);
		List<ComplaintsDto> listOFComplaints = new ArrayList<ComplaintsDto>();
		for (RlmsComplaintTechMapDtls complaintTechMapDtls : listOFAssignedComplaints) {
			ComplaintsDto dto = new ComplaintsDto();
			dto.setActualServiceEndDate(complaintTechMapDtls.getComplaintMaster().getActualServiceEndDate());
			dto.setComplaintNumber(complaintTechMapDtls.getComplaintMaster().getComplaintNumber());
			dto.setCustomerName(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setLatitude(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLatitude());
			dto.setLongitude(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLongitude());
			dto.setLiftNumber(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLiftNumber());
			dto.setRegistrationDate(complaintTechMapDtls.getComplaintMaster().getRegistrationDate());
			dto.setRemark(complaintTechMapDtls.getComplaintMaster().getRemark());
			dto.setServiceStartDate(complaintTechMapDtls.getComplaintMaster().getServiceStartDate());
			dto.setComplaintId(complaintTechMapDtls.getComplaintMaster().getComplaintId());
			dto.setComplaintTechMapId(complaintTechMapDtls.getComplaintTechMapId());
			if(Status.PENDING.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.PENDING.getStatusMsg());
			}else if(Status.ASSIGNED.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.ASSIGNED.getStatusMsg());
			}else if(Status.INPROGESS.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.INPROGESS.getStatusMsg());
			}else if(Status.RESOLVED.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.RESOLVED.getStatusMsg());
			}
			listOFComplaints.add(dto);
		}
		return listOFComplaints;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndRegisterNewComplaint(ComplaintsDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException{
		String result = null;
		if(this.validateComplaintDetails(dto)){
			RlmsComplaintMaster complaintMaster = this.counstructComplaintMaster(dto, metaInfo);			
			if(complaintMaster!=null) {
			Integer complaintId = this.complaintsDao.saveComplaintM(complaintMaster);
			complaintMaster.setComplaintNumber(complaintId.toString());
			this.complaintsDao.mergeComplaintM(complaintMaster);
			result = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_REG_SUCCESSFUL.getMessage());
			this.sendNotificationsAboutComplaintRegistration(complaintMaster);
			}
		}
	  return result;
	}
	private boolean validateComplaintDetails(ComplaintsDtlsDto dto) throws ValidationException{
		boolean isValidaDetails = true;
		if(null == dto.getComplaintsRemark()){
			isValidaDetails = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_REMARK_BLANK.getMessage()));
		}
		if(null == dto.getComplaintsTitle()){
			isValidaDetails = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_TITLE_BLANK.getMessage()));
		}
		
		if(null == dto.getLiftCustomerMapId()){
			isValidaDetails = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.LIFT_CUSTOMER_BLANK.getMessage()));
		}
		
		return isValidaDetails;
	}
	private RlmsComplaintMaster counstructComplaintMaster(ComplaintsDtlsDto dto, UserMetaInfo metaInfo){
		RlmsComplaintMaster complaintMaster = new RlmsComplaintMaster();
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapById(dto.getLiftCustomerMapId());
		
		complaintMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		complaintMaster.setComplaintNumber(String.valueOf(Math.random()));
		complaintMaster.setLiftCustomerMap(liftCustomerMap);
		complaintMaster.setRegistrationDate(new Date());
		complaintMaster.setRegistrationType(dto.getRegistrationType());
		complaintMaster.setRemark(dto.getComplaintsRemark());
		complaintMaster.setStatus(Status.PENDING.getStatusId());
		complaintMaster.setTitle(dto.getComplaintsTitle());
		complaintMaster.setCallType(0);
		if(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId() == dto.getRegistrationType()){
				complaintMaster.setCreatedBy(metaInfo.getUserId());				
				complaintMaster.setUpdatedBy(metaInfo.getUserId());				
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId() == dto.getRegistrationType()){
			complaintMaster.setCreatedBy(dto.getMemberId());				
			complaintMaster.setUpdatedBy(dto.getMemberId());		
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getId() == dto.getRegistrationType()){
			complaintMaster.setCreatedBy(RLMSConstants.MINUS_ONE.getId());				
			complaintMaster.setUpdatedBy(RLMSConstants.MINUS_ONE.getId());
		}
		complaintMaster.setUpdatedDate(new Date());
		complaintMaster.setCreatedDate(new Date());
		return complaintMaster;
	}
	
	
	private ComplaintsDto constructComplaintDto(RlmsComplaintMaster complaintMaster){
		ComplaintsDto dto = new ComplaintsDto();
		if(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId().equals(complaintMaster.getRegistrationType())){
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getName());
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId().equals(complaintMaster.getRegistrationType())){
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getName());
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getId().equals(complaintMaster.getRegistrationType())){
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getName());
		}
		dto.setComplaintId(complaintMaster.getComplaintId());
		dto.setComplaintNumber(complaintMaster.getComplaintNumber());
		dto.setCustomerName(complaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
		dto.setLiftAddress(complaintMaster.getLiftCustomerMap().getLiftMaster().getAddress());
		dto.setRegistrationDate(complaintMaster.getRegistrationDate());
		dto.setRemark(complaintMaster.getRemark());
		dto.setCity(complaintMaster.getLiftCustomerMap().getLiftMaster().getCity());
		if(null != complaintMaster.getRegistrationDate()){
			dto.setRegistrationDateStr(DateUtils.convertDateToStringWithoutTime(complaintMaster.getRegistrationDate()));
		}
		dto.setActualServiceEndDate(complaintMaster.getActualServiceEndDate());
		if(null != complaintMaster.getActualServiceEndDate()){
			dto.setActualServiceEndDateStr(DateUtils.convertDateToStringWithoutTime(complaintMaster.getActualServiceEndDate()));
		}
		dto.setRemark(complaintMaster.getRemark());
		dto.setTitle(complaintMaster.getTitle());
		dto.setServiceStartDate(complaintMaster.getServiceStartDate());
		if(null != complaintMaster.getServiceStartDate()){
			dto.setServiceStartDateStr(DateUtils.convertDateToStringWithoutTime(complaintMaster.getServiceStartDate()));
		}
		if(!Status.PENDING.getStatusId().equals(complaintMaster.getStatus())){
			RlmsComplaintTechMapDtls complaintTechMapDtls = this.complaintsDao.getComplTechMapObjByComplaintId(complaintMaster.getComplaintId());
			if(null != complaintTechMapDtls){
				String techDtls = complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName() + " (" + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber() + ")";			
				dto.setTechnicianDtls(techDtls);
			}
		}else{
			dto.setTechnicianDtls("-");
		}
		if(Status.PENDING.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.PENDING.getStatusMsg());
		}else if(Status.ASSIGNED.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.ASSIGNED.getStatusMsg());
		}else if(Status.INPROGESS.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.INPROGESS.getStatusMsg());
		}else if(Status.COMPLETED.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.COMPLETED.getStatusMsg());
		}else if(Status.RESOLVED.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.RESOLVED.getStatusMsg());
		}
		String complaintent = null;
		if(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId() == complaintMaster.getRegistrationType()){
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getName());
			RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(complaintMaster.getCreatedBy());
			if(userRoles!=null){
				complaintent = userRoles.getRlmsUserMaster().getFirstName() + " " + userRoles.getRlmsUserMaster().getLastName() + " (" + userRoles.getRlmsUserMaster().getContactNumber() + ")";
			}
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId() == complaintMaster.getRegistrationType()){
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getName());
			RlmsMemberMaster memberMaster = this.customerService.getMemberById(complaintMaster.getCreatedBy());
			complaintent = memberMaster.getFirstName() + " " + memberMaster.getLastName() + " (" + memberMaster.getContactNumber() + ")";
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getId() == complaintMaster.getRegistrationType()){
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getName());
			complaintent = RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getName();
		}
		dto.setComplaintent(complaintent);
		
		
		return dto;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto){
		List<ComplaintsDto> listOfAllComplaints = new ArrayList<ComplaintsDto>();
		List<RlmsComplaintMaster> listOfComplaints = this.complaintsDao.getAllComplaintsForGivenCriteria(dto.getBranchCompanyMapId(), dto.getBranchCustomerMapId(), dto.getListOfLiftCustoMapId(), dto.getStatusList(),dto.getFromDate(), dto.getToDate(),dto.getServiceCallType());
		for (RlmsComplaintMaster rlmsComplaintMaster : listOfComplaints) {
			ComplaintsDto complaintsDto = this.constructComplaintDto(rlmsComplaintMaster);
			listOfAllComplaints.add(complaintsDto);
		}
		
		return listOfAllComplaints;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(ComplaintsDtlsDto dto){
		return this.complaintsDao.getAllComplaintsForGivenCriteria(dto.getBranchCompanyMapId(), dto.getBranchCustomerMapId(), dto.getListOfLiftCustoMapId(), dto.getStatusList(),dto.getFromDate(), dto.getToDate(),0);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String assignComplaint(ComplaintsDto complaintsDto, UserMetaInfo metaInfo) throws ValidationException{
		
		RlmsComplaintTechMapDtls alreadyAssignedComplaint = this.complaintsDao.getComplTechMapByComplaintId(complaintsDto.getComplaintId());
		if(null != alreadyAssignedComplaint){
		  throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_ASSIGNED_ALREADY.getMessage()));	
		}
		RlmsComplaintTechMapDtls complaintTechMapDtls = this.constructComplaintTechMapDtlsDto(complaintsDto, metaInfo);
		this.complaintsDao.saveComplaintTechMapDtls(complaintTechMapDtls);
		RlmsComplaintMaster complaintMaster = complaintTechMapDtls.getComplaintMaster();
		complaintMaster.setStatus(Status.ASSIGNED.getStatusId());
		complaintMaster.setUpdatedBy(metaInfo.getUserId());
		complaintMaster.setUpdatedDate(new Date());
		
		this.complaintsDao.mergeComplaintM(complaintMaster);
		String techName = complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() +  " " +complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName() + " (" + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber() + ")";
		String statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_ASSIGNED_SUUCESSFULLY.getMessage()) + " " + techName;
		this.sendNotificationsAboutComplaintAssign(complaintTechMapDtls);
		return statusMessage;
		
	}
	
	private RlmsComplaintTechMapDtls constructComplaintTechMapDtlsDto(ComplaintsDto complaintsDto, UserMetaInfo metaInfo){
		RlmsComplaintTechMapDtls complaintTechMapDtls = new RlmsComplaintTechMapDtls();
		RlmsComplaintMaster complaintMaster = this.complaintsDao.getComplaintMasterObj(complaintsDto.getComplaintId(),complaintsDto.getServiceCallType());
		RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(complaintsDto.getUserRoleId());
		complaintTechMapDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		complaintTechMapDtls.setAssignedDate(new Date());
		complaintTechMapDtls.setComplaintMaster(complaintMaster);
		complaintTechMapDtls.setCreatedBy(metaInfo.getUserId());
		complaintTechMapDtls.setCreatedDate(new Date());
		complaintTechMapDtls.setStatus(Status.ASSIGNED.getStatusId());
		complaintTechMapDtls.setUpdatedBy(metaInfo.getUserId());
		complaintTechMapDtls.setUpdatedDate(new Date());
		complaintTechMapDtls.setUserRoles(userRoles);
		return complaintTechMapDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getAllLiftsForBranchsOrCustomer(LiftDtlsDto dto){
		List<LiftDtlsDto> listOfLiftDtls = new ArrayList<LiftDtlsDto>();
		List<RlmsLiftCustomerMap> listOfLifts = this.liftDao.getAllLiftsForBranchsOrCustomer(dto);
		for (RlmsLiftCustomerMap rlmsLiftCustomerMap : listOfLifts) {
			LiftDtlsDto lift = new LiftDtlsDto();
			lift.setLiftId(rlmsLiftCustomerMap.getLiftCustomerMapId());
			lift.setLiftNumber(rlmsLiftCustomerMap.getLiftMaster().getLiftNumber());
			listOfLiftDtls.add(lift);
		}
		return listOfLiftDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserAppDtls> getRegIdsOfAllApplicableUsers(Integer liftCustomerMapId){
		List<UserAppDtls> listOfAllApplicableDtls = new ArrayList<UserAppDtls>();
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapById(liftCustomerMapId);
		List<RlmsCustomerMemberMap> listOfAllMembers = this.customerService.getAllMembersForCustomer(liftCustomerMap.getBranchCustomerMap().getBranchCustoMapId());
		for (RlmsCustomerMemberMap rlmsCustomerMemberMap : listOfAllMembers) {
			listOfAllApplicableDtls.add(this.customerService.getUserAppDtls(rlmsCustomerMemberMap.getRlmsMemberMaster().getMemberId(), RLMSConstants.MEMBER_TYPE.getId()));
		}
		return listOfAllApplicableDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNotificationsAboutComplaintRegistration(RlmsComplaintMaster complaintMaster){
	//	Map<String, String> dataPayload = new HashMap<String, String>();
		 JSONObject  dataPayload = new JSONObject();
		try {
			//dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TITLE, PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintMaster.getTitle());
			//dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, complaintMaster.getRemark());
			dataPayload.put("title", PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintMaster.getTitle());
			dataPayload.put("body", complaintMaster.getRemark());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		List<UserAppDtls> listOfUsers = this.getRegIdsOfAllApplicableUsers(complaintMaster.getLiftCustomerMap().getLiftCustomerMapId());
		if(listOfUsers !=null && !listOfUsers.isEmpty()) {
			for (UserAppDtls userAppDtls : listOfUsers) {
				try{
						log.debug("sendNotificationsAboutComplaintRegistration :: Sending notification");
						if(userAppDtls.getAppRegId()!=null) {
							this.messagingService.sendUserNotification(userAppDtls.getAppRegId(), dataPayload);
						log.debug("sendNotificationsAboutComplaintRegistration :: Notification sent to Id :" + userAppDtls.getAppRegId());
				    }				
				}catch(Exception e){
					log.error(ExceptionUtils.getFullStackTrace(e));
				}
			}
		}
		else {
			log.error("user app registration id not found");
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNotificationsAboutComplaintAssign(RlmsComplaintTechMapDtls complaintTechMapDtls){
		this.notifyTechnician(complaintTechMapDtls);
		this.sendNotificationsToMembers(complaintTechMapDtls);
	}
	
	private void notifyTechnician(RlmsComplaintTechMapDtls complaintTechMapDtls){
		UserAppDtls userAppDtls = this.customerService.getUserAppDtls(complaintTechMapDtls.getUserRoles().getUserRoleId(), RLMSConstants.USER_ROLE_TYPE.getId());
		//Map<String, String> dataPayload = new HashMap<String, String>();
		JSONObject dataPayload = new JSONObject();
		try {
			/*dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TITLE, PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintTechMapDtls.getComplaintMaster().getTitle());
			dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, complaintTechMapDtls.getComplaintMaster().getRemark());*/
			dataPayload.put("title", PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintTechMapDtls.getComplaintMaster().getTitle());
			dataPayload.put("body", complaintTechMapDtls.getComplaintMaster().getRemark());
		
		} catch (JSONException e1) {
			e1.printStackTrace();
		}	
		
		try{
			log.debug("notifyTechnician :: Sending notification");
			this.messagingService.sendTechnicianNotification(userAppDtls.getAppRegId(), dataPayload);
			log.debug("notifyTechnician :: Notification sent to Id :" + userAppDtls.getAppRegId());
		}catch(Exception e){
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		
	}
	private void sendNotificationsToMembers(RlmsComplaintTechMapDtls complaintTechMapDtls){
	//	Map<String, String> dataPayload = new HashMap<String, String>();
		JSONObject dataPayload = new JSONObject();
		String techDtls = complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName() + " (" + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber() + ")";

		try {
			dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TITLE, PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintTechMapDtls.getComplaintMaster().getTitle());
			dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, complaintTechMapDtls.getComplaintMaster().getRemark());
			dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TECHNICIAN, techDtls);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		List<UserAppDtls> listOfUsers = this.getRegIdsOfAllApplicableUsers(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftCustomerMapId());
		
		for (UserAppDtls userAppDtls : listOfUsers) {
			try{
				log.debug("sendNotificationsAboutComplaintRegistration :: Sending notification");
				this.messagingService.sendUserNotification(userAppDtls.getAppRegId(), dataPayload);
				log.debug("sendNotificationsAboutComplaintRegistration :: Notification sent to Id :" + userAppDtls.getAppRegId());
			}catch(Exception e){
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getAllComplaintsByMember(Integer memberId,Integer serviceCall){
		List<ComplaintsDto> listOfComplDtls = new ArrayList<ComplaintsDto>();
		List<RlmsComplaintMaster> listOfAllComplByMember = this.complaintsDao.getAllComplaintsByMemberId(memberId,serviceCall);
		
		for (RlmsComplaintMaster rlmsComplaintMaster : listOfAllComplByMember) {
			ComplaintsDto dto = new ComplaintsDto();
			dto.setActualServiceEndDate(rlmsComplaintMaster.getActualServiceEndDate());
			dto.setComplaintNumber(rlmsComplaintMaster.getComplaintNumber());
			dto.setCustomerName(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setLatitude(rlmsComplaintMaster.getLiftCustomerMap().getLiftMaster().getLatitude());
			dto.setLongitude(rlmsComplaintMaster.getLiftCustomerMap().getLiftMaster().getLongitude());
			dto.setLiftNumber(rlmsComplaintMaster.getLiftCustomerMap().getLiftMaster().getLiftNumber());
			dto.setRegistrationDate(rlmsComplaintMaster.getRegistrationDate());
			dto.setRemark(rlmsComplaintMaster.getRemark());
			dto.setServiceStartDate(rlmsComplaintMaster.getServiceStartDate());
			dto.setLiftCustoMapId(rlmsComplaintMaster.getLiftCustomerMap().getLiftCustomerMapId());
			if(Status.PENDING.getStatusId().equals(rlmsComplaintMaster.getStatus())){
				dto.setStatus(Status.PENDING.getStatusMsg());
			}else if(Status.ASSIGNED.getStatusId().equals(rlmsComplaintMaster.getStatus())){
				dto.setStatus(Status.ASSIGNED.getStatusMsg());
			}else if(Status.INPROGESS.getStatusId().equals(rlmsComplaintMaster.getStatus())){
				dto.setStatus(Status.INPROGESS.getStatusMsg());
			}else if(Status.RESOLVED.getStatusId().equals(rlmsComplaintMaster.getStatus())){
				dto.setStatus(Status.RESOLVED.getStatusMsg());
			}
			dto.setComplaintTechMapId(rlmsComplaintMaster.getComplaintId());
			listOfComplDtls.add(dto);
		}
		return listOfComplDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserRoleDtlsDTO> getAllTechniciansToAssignComplaint(ComplaintsDtlsDto complaintsDtlsDto){
   	 
		RlmsComplaintMaster complaintMaster = this.complaintsDao.getComplaintMasterObj(complaintsDtlsDto.getComplaintId(),complaintsDtlsDto.getServiceCallType());
		List<UserRoleDtlsDTO> listOFUserAdtls = new ArrayList<UserRoleDtlsDTO>();
		List<RlmsUserRoles> listOfAllTechnicians = this.userService.getListOfTechniciansForBranch(complaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getCompanyBranchMapId());
		for (RlmsUserRoles rlmsUserRoles : listOfAllTechnicians) {
			UserRoleDtlsDTO dto = new UserRoleDtlsDTO();
			dto.setUserId(rlmsUserRoles.getRlmsUserMaster().getUserId());
			
			
			dto.setCompanyBranchMapId(rlmsUserRoles.getRlmsCompanyBranchMapDtls().getCompanyBranchMapId());
			dto.setName(rlmsUserRoles.getRlmsUserMaster().getFirstName() + " " + rlmsUserRoles.getRlmsUserMaster().getLastName());
			dto.setContactNumber(rlmsUserRoles.getRlmsUserMaster().getContactNumber());
			dto.setUserRoleId(rlmsUserRoles.getUserRoleId());
			
			
			
			dto.setLongitude(rlmsUserRoles.getRlmsUserApplicationMapDetails().getLongitude());
			dto.setLatitude(rlmsUserRoles.getRlmsUserApplicationMapDetails().getLatitude());
			
			
			 List<Integer> statusList = new ArrayList<Integer>();
		   	 statusList.add(Status.ASSIGNED.getStatusId());
		   	 statusList.add(Status.INPROGESS.getStatusId());
		   	 statusList.add(Status.RESOLVED.getStatusId());
			 List<RlmsComplaintTechMapDtls> listOfAssignedComplaints = this.complaintsDao.getAllComplaintsAssigned(rlmsUserRoles.getUserRoleId(), statusList);
			 if(null != listOfAssignedComplaints && !listOfAssignedComplaints.isEmpty()){
				 dto.setCountOfComplaintsAssigned(listOfAssignedComplaints.size());
			 }
			 
			 UserAppDtls userAppDtls = this.customerService.getUserAppDtls(rlmsUserRoles.getRlmsUserMaster().getUserId(), RLMSConstants.MEMBER_TYPE.getId());
			 if(null != userAppDtls){
				 dto.setCurrentAddress(userAppDtls.getAddress());
			 }
			 listOFUserAdtls.add(dto);
		}
		return listOFUserAdtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String updateComplaintStatus(ComplaintsDto dto){
		RlmsComplaintMaster complaintMaster = this.complaintsDao.getComplaintMasterObj(dto.getComplaintId(),0);
		Integer statusId = 0;
		if(Status.PENDING.getStatusMsg().equalsIgnoreCase(dto.getStatus())){
			statusId = Status.PENDING.getStatusId();
		}else if(Status.ASSIGNED.getStatusMsg().equalsIgnoreCase(dto.getStatus())){
			statusId = Status.ASSIGNED.getStatusId();
		}else if(Status.COMPLETED.getStatusMsg().equalsIgnoreCase(dto.getStatus())){
			statusId = Status.COMPLETED.getStatusId();
			complaintMaster.setActualServiceEndDate(new Date());
		}else if(Status.RESOLVED.getStatusMsg().equalsIgnoreCase(dto.getStatus())){
			statusId = Status.RESOLVED.getStatusId();
			complaintMaster.setActualServiceEndDate(new Date());
		}
		complaintMaster.setStatus(statusId);
		this.complaintsDao.mergeComplaintM(complaintMaster);
		String resultMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.STATUS_UPDATED.getMessage()) + " " + dto.getStatus() + " " + PropertyUtils.getPrpertyFromContext(RlmsErrorType.SUCCESSFULLY.getMessage());
		return resultMessage;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComplaintSiteVisitDtls(RlmsSiteVisitDtls siteVisitDtls){
		this.complaintsDao.saveComplaintSiteVisitDtls(siteVisitDtls);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndSaveSiteVisitDtls(SiteVisitDtlsDto dto) throws ValidationException, ParseException{
		//this.validateVisitDtls(dto);
		RlmsSiteVisitDtls visitDtls = this.constructVisitDtls(dto);
		this.saveComplaintSiteVisitDtls(visitDtls);
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.VISIT_UPDATED_SUCCESS.getMessage());
	}
	
	private RlmsSiteVisitDtls constructVisitDtls(SiteVisitDtlsDto dto) throws ParseException{
		RlmsComplaintTechMapDtls complaintTechMapDtls = this.complaintsDao.getComplTechMapByComplaintTechMapId(dto.getComplaintTechMapId());
		RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(dto.getUserRoleId());
		RlmsSiteVisitDtls visitDtls = new RlmsSiteVisitDtls();
		visitDtls.setComplaintTechMapDtls(complaintTechMapDtls);
		visitDtls.setFromDate(DateUtils.convertStringToDateWithTime(dto.getFromDateDtr()));
		visitDtls.setToDate(DateUtils.convertStringToDateWithTime(dto.getToDateStr()));
		
		visitDtls.setTotalTime(DateUtils.getDateDiff(dto.getFromDate(), dto.getToDate(), TimeUnit.SECONDS));
		visitDtls.setUserRoles(userRoles);
		visitDtls.setRemark(dto.getRemark());
		visitDtls.setCreatedDate(new Date());
		visitDtls.setCreatedBy(dto.getUserRoleId());
		visitDtls.setUpdatedDate(new Date());
		visitDtls.setUpdatedBy(dto.getUserRoleId());
		return visitDtls;
	}
	private String validateVisitDtls(SiteVisitDtlsDto dto) throws ValidationException{
		String errorMesage = null;
		RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(dto.getUserRoleId());
		if(null == userRoles){
			throw new ValidationException(RlmsErrorType.INVALID_USER_ROLE_ID.getCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.INVALID_USER_ROLE_ID.getMessage()));
		}
		
		if(null == dto.getFromDate() || null == dto.getToDate() || null == dto.getComplaintTechMapId()){
			throw new ValidationException(RlmsErrorType.INVALID_USER_ROLE_ID.getCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.INCOMPLETE_DATA.getMessage()));
		}
		
		return errorMesage;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<SiteVisitDtlsDto> getAllVisitsForComplaint(SiteVisitDtlsDto siteVisitDtlsDto){
		List<SiteVisitDtlsDto> listOfVisits = new ArrayList<SiteVisitDtlsDto>();
		List<RlmsSiteVisitDtls> listOfAllVisits = this.complaintsDao.getAllVisitsForComnplaints(siteVisitDtlsDto.getComplaintTechMapId());
		for (RlmsSiteVisitDtls rlmsSiteVisitDtls : listOfAllVisits) {
			SiteVisitDtlsDto dto = new SiteVisitDtlsDto();
			dto.setComplaintTechMapId(rlmsSiteVisitDtls.getComplaintTechMapDtls().getComplaintTechMapId());
			dto.setFromDateDtr(DateUtils.convertDateToStringWithTime(rlmsSiteVisitDtls.getFromDate()));
			dto.setToDateStr(DateUtils.convertDateToStringWithTime(rlmsSiteVisitDtls.getToDate()));
			String totalTime = DateUtils.convertTimeIntoDaysHrMin(DateUtils.getDateDiff(rlmsSiteVisitDtls.getFromDate(), rlmsSiteVisitDtls.getToDate(), TimeUnit.SECONDS), TimeUnit.SECONDS);
			if(null != totalTime){
				dto.setTotalTime(totalTime);
			}
			dto.setRemark(rlmsSiteVisitDtls.getRemark());
			listOfVisits.add(dto);
		}
		return listOfVisits;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDtlsDto getTechnicianDtls(Integer complaintId){
		UserDtlsDto dto = new UserDtlsDto();
		RlmsComplaintTechMapDtls complaintTechMapDtls = this.complaintsDao.getComplTechMapByComplaintId(complaintId);
		if(null != complaintTechMapDtls){
			dto.setFirstName(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName());
			dto.setLastName(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName());
			dto.setUserId(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getUserId());
			dto.setContactNumber(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber());
			dto.setEmailId(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getEmailId());
		}
		return dto;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndUpdateComplaint(ComplaintsDto complaintsDto,
			UserMetaInfo metaInfo) throws ValidationException, ParseException {
		RlmsComplaintTechMapDtls complaintTechMapDtls = this.complaintsDao.getComplTechMapByComplaintId(complaintsDto.getComplaintId());
		if(null==complaintTechMapDtls && "Assigned".equalsIgnoreCase(complaintsDto.getStatus()) && null!=complaintsDto.getUserRoleId()){
			this.assignComplaint(complaintsDto, metaInfo);
		}else{			
			RlmsComplaintMaster complaintMaster = this.complaintsDao.getComplaintMasterObj(complaintsDto.getComplaintId(),complaintsDto.getServiceCallType());			
			if(complaintsDto.getRegistrationDateStr()!=null && !(" - ".equals(complaintsDto.getRegistrationDateStr()))){
				complaintMaster.setRegistrationDate(DateUtils.convertStringToDateWithoutTime(complaintsDto.getRegistrationDateStr()));
			}if(complaintsDto.getServiceStartDateStr()!=null && !(" - ".equals(complaintsDto.getServiceStartDateStr()))){
				complaintMaster.setServiceStartDate(DateUtils.convertStringToDateWithoutTime(complaintsDto.getServiceStartDateStr()));
			}if(complaintsDto.getActualServiceEndDateStr()!=null && !(" - ".equals(complaintsDto.getActualServiceEndDateStr()))){
				complaintMaster.setActualServiceEndDate(DateUtils.convertStringToDateWithoutTime(complaintsDto.getActualServiceEndDateStr()));
			}
			complaintMaster.setTitle(complaintsDto.getTitle());
			complaintMaster.setRemark(complaintsDto.getRemark());
			if("Assigned".equalsIgnoreCase(complaintsDto.getStatus()) && null!=complaintsDto.getUserRoleId()){
				complaintMaster.setStatus(Status.ASSIGNED.getStatusId());		
			}else if("Pending".equalsIgnoreCase(complaintsDto.getStatus())){
				complaintMaster.setStatus(Status.PENDING.getStatusId());
				if(null!=complaintTechMapDtls){
					deleteComplaintsTechMapDetails(complaintTechMapDtls.getComplaintTechMapId());
				}
			}else if("Completed".equalsIgnoreCase(complaintsDto.getStatus()) && null!=complaintsDto.getUserRoleId()){
				complaintMaster.setStatus(Status.COMPLETED.getStatusId());
			}else if("In Progress".equalsIgnoreCase(complaintsDto.getStatus()) && null!=complaintsDto.getUserRoleId()){
				complaintMaster.setStatus(Status.INPROGESS.getStatusId());
			}
			complaintMaster.setUpdatedBy(metaInfo.getUserId());
			complaintMaster.setUpdatedDate(new Date());
			if(null!=complaintTechMapDtls && !"Pending".equalsIgnoreCase(complaintsDto.getStatus())){
				complaintTechMapDtls.setUpdatedBy(metaInfo.getUserId());
				complaintTechMapDtls.setUpdatedDate(new Date());
				RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(complaintsDto.getUserRoleId());
				complaintTechMapDtls.setUserRoles(userRoles);
				complaintTechMapDtls.setComplaintMaster(complaintMaster);
				this.complaintsDao.updateComplaints(complaintTechMapDtls);
				
			}else{
				this.complaintsDao.updateComplaintsMatser(complaintMaster);
			}
		}
		
		String statusMessage = PropertyUtils.getPrpertyFromContext("Complaint Updated Successfully");
		return statusMessage;
		
	}
	@Transactional(propagation = Propagation.REQUIRED)
	private void deleteComplaintsTechMapDetails(Integer complaintTechMapId) {
		this.complaintsDao.deleteComplaintsTechMap(complaintTechMapId);
	}	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String deleteComplaint(ComplaintsDto dto){
		RlmsComplaintMaster complaintMaster = this.complaintsDao.getComplaintMasterObj(dto.getComplaintId(),0);
		complaintMaster.setActiveFlag(RLMSConstants.INACTIVE.getId());
		this.complaintsDao.mergeComplaintM(complaintMaster);
		String resultMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_DELETE_SUCCESFUL.getMessage());
		return resultMessage;
	}
}
