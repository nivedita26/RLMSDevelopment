package com.rlms.service;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.AMCType;
import com.rlms.constants.RLMSCallType;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.SpocRoleConstants;
import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.CallSpecificReportDto;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.EventDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.SiteVisitDtlsDto;
import com.rlms.contract.SiteVisitReportDto;
import com.rlms.contract.TechnicianWiseReportDTO;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.contract.UserRolePredicate;
import com.rlms.dao.BranchDao;
import com.rlms.dao.ComplaintsDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.DashboardDao;
import com.rlms.dao.LiftDao;
import com.rlms.dao.UserMasterDao;
import com.rlms.dao.UserRoleDao;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsSiteVisitDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.model.ServiceCall;
import com.rlms.predicates.LiftPredicate;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;

@Service("ReportService")
public class ReportServiceImpl implements ReportService {

	@Autowired
	private BranchDao branchDao;
	
	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private ComplaintsDao complaintsDao;
	
	@Autowired
	private ComplaintsService complaintService;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	private DashboardDao dashBoardDao;
	
	@Autowired
	private  UserMasterDao userMasterDao;
	
	@Autowired
	private  CustomerDao customerDao;
	
	@Autowired
	private UserService userService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AMCDetailsDto> getAMCDetailsForLifts(AMCDetailsDto dto){
		List<AMCDetailsDto> listOFAMCDetails = new ArrayList<AMCDetailsDto>();
		List<Integer> listOFAllCustomersForBranch = new ArrayList<Integer>();
		List<Integer> listOfLiftsForAMCDtls = new ArrayList<Integer>();
		List<RlmsLiftCustomerMap> listOFApplicableLifts = new ArrayList<RlmsLiftCustomerMap>();
		if(null == dto.getListOfBranchCustomerMapId()){
			List<RlmsBranchCustomerMap> listOFAllCustoOfBranch = this.branchDao.getAllCustomersOfBranch(dto.getBranchCompanyMapId());
			for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOFAllCustoOfBranch) {
				listOFAllCustomersForBranch.add(rlmsBranchCustomerMap.getBranchCustoMapId());
			}			
		}
		
		if(null != dto.getLiftCustomerMapId() && !dto.getLiftCustomerMapId().isEmpty()){
			listOFApplicableLifts = this.liftDao.getAllLiftsByIds(dto.getLiftCustomerMapId());
		}else{
			if(null != dto.getListOfBranchCustomerMapId() && !dto.getListOfBranchCustomerMapId().isEmpty()){
				listOFApplicableLifts = this.liftDao.getAllLiftsForCustomres(dto.getListOfBranchCustomerMapId());
			}else{
				 listOFApplicableLifts = this.liftDao.getAllLiftsForCustomres(listOFAllCustomersForBranch);
			}
		}
		if(listOFApplicableLifts!=null) {
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : listOFApplicableLifts) {
				listOfLiftsForAMCDtls.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
			}
		}
		List<RlmsLiftAmcDtls> listOfAMCDtls = this.liftDao.getAMCDetilsForLifts(listOfLiftsForAMCDtls, dto);
		Set<Integer> liftIds = new HashSet<Integer>();
		for (RlmsLiftAmcDtls liftAmcDtls : listOfAMCDtls) {
			liftIds.add(liftAmcDtls.getLiftCustomerMap().getLiftMaster().getLiftId());
		}
		
		for (Integer liftId : liftIds) {
			List<RlmsLiftAmcDtls> listForLift = new ArrayList<RlmsLiftAmcDtls>(listOfAMCDtls);
			CollectionUtils.filter(listForLift, new LiftPredicate(liftId));
			listOFAMCDetails.addAll(this.constructListOFAMcDtos(listForLift));
		}
		return listOFAMCDetails;
	}
	
	private List<AMCDetailsDto> constructListOFAMcDtos(List<RlmsLiftAmcDtls> listOFAMCs){
		List<AMCDetailsDto> listOFDtos = new ArrayList<AMCDetailsDto>();
		int i = 0;
		for (RlmsLiftAmcDtls liftAmcDtls : listOFAMCs) {
			AMCDetailsDto dto  = new AMCDetailsDto();
			if(null != liftAmcDtls.getAmcEndDate()){
				dto.setAmcEndDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcEndDate()));
			}
			if(null != liftAmcDtls.getAmcStartDate()){
				dto.setAmcStartDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcStartDate()));
			}
			
			dto.setCustomerName(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			if(null != liftAmcDtls.getAmcDueDate()){
				dto.setDueDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcDueDate()));
			}
			
			if(null != liftAmcDtls.getAmcSlackStartDate()){
				dto.setLackEndDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcSlackStartDate()));
			}
			
			if(null != liftAmcDtls.getAmcSlackEndDate()){
				dto.setLackEndDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcSlackEndDate()));
			}
			dto.setBranchName(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			dto.setLiftNumber(liftAmcDtls.getLiftCustomerMap().getLiftMaster().getLiftNumber());
			dto.setCity(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCity());
			dto.setArea(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getArea());
			Date tempStartDate = listOFAMCs.get(listOFAMCs.size() - 1).getAmcStartDate();
			/*Date tempEndDate = listOFAMCs.get(listOFAMCs.size() - 1).getLiftCustomerMap().getLiftMaster().getAmcEndDate();
			Date tempDateOfInstallation = listOFAMCs.get(listOFAMCs.size() - 1).getLiftCustomerMap().getLiftMaster().getDateOfInstallation();
			Date tempWarrantyStartDate = listOFAMCs.get(listOFAMCs.size() - 1).getLiftCustomerMap().getLiftMaster().getServiceStartDate();
			Date tempWarrantyEndDate = listOFAMCs.get(listOFAMCs.size() - 1).getLiftCustomerMap().getLiftMaster().getServiceEndDate();
		*/
		//	Date tempEndDate = listOFAMCs.get(listOFAMCs.size() - 1).getAmcEndDate();
		//	Date tempDateOfInstallation = listOFAMCs.get(listOFAMCs.size() - 1).getLiftCustomerMap().getLiftMaster().getDateOfInstallation();
		//	Date tempWarrantyStartDate = listOFAMCs.get(listOFAMCs.size() - 1).getLiftCustomerMap().getLiftMaster().getServiceStartDate();
		//	Date tempWarrantyEndDate = listOFAMCs.get(listOFAMCs.size() - 1).getLiftCustomerMap().getLiftMaster().getServiceEndDate();

		//	dto.setStatus(this.calculateAMCStatus(tempStartDate, tempEndDate, tempDateOfInstallation,tempWarrantyStartDate,tempWarrantyEndDate).getStatusMsg());
			dto.setStatus(Status.getStringFromID(liftAmcDtls.getStatus()));

			dto.setAmcAmount(liftAmcDtls.getAmcAmount());
			
			if(i > 0 ){
				Integer diffInDays = DateUtils.daysBetween(listOFAMCs.get(i).getAmcStartDate(), listOFAMCs.get(i - 1).getAmcEndDate());
				if(diffInDays > 0){
					Date slackStartDate = DateUtils.addDaysToDate(listOFAMCs.get(i - 1).getAmcEndDate(), 1);
					Date slackEndDate = DateUtils.addDaysToDate(listOFAMCs.get(i).getAmcStartDate(), -1);
					if(null != slackStartDate &&  null != slackEndDate){
						{
							dto.setSlackStartDate(DateUtils.convertDateToStringWithoutTime(slackStartDate));
							dto.setSlackEndDate(DateUtils.convertDateToStringWithoutTime(slackEndDate));
							dto.setSlackperiod(diffInDays);
						}
					}
				}
			}
			if(liftAmcDtls.getAmcType()!=null){
				if(AMCType.COMPREHENSIVE.getId() == liftAmcDtls.getAmcType()){
					dto.setAmcTypeStr(AMCType.COMPREHENSIVE.getType());
				}else if(AMCType.NON_COMPREHENSIVE.getId() == liftAmcDtls.getAmcType()){
					dto.setAmcTypeStr(AMCType.NON_COMPREHENSIVE.getType());
				}else if(AMCType.ON_DEMAND.getId() == liftAmcDtls.getAmcType()){
					dto.setAmcTypeStr(AMCType.ON_DEMAND.getType());
				}else if(AMCType.OTHER.getId() == liftAmcDtls.getAmcType()){
					dto.setAmcTypeStr(AMCType.OTHER.getType());
				}
			}
			listOFDtos.add(dto);
			i++;
		}
		return listOFDtos;
	}
	private Status calculateAMCStatus(Date amcStartDate, Date amcEndDate, Date dateOfInstallation,Date warrantyStartDate,Date warrantyEndDate) {
		Status amcStatus = null;
		Date today = new Date();
		int renewalDays = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
  			today = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {

			e.printStackTrace();
		}
		if(DateUtils.isBeforeOrEqualToDate(today,warrantyEndDate)){
			amcStatus = Status.UNDER_WARRANTY;
		}
	   if(amcStartDate==null && amcEndDate==null) {
		   if(DateUtils.isBeforeToDate(warrantyEndDate,today)){
				amcStatus = Status.WARRANTY_EXPIRED;
		   }
	   }
		if(amcStartDate!=null &&amcEndDate!=null ) {
		Date renewalDate = DateUtils.addDaysToDate(amcEndDate, -30);
		if(DateUtils.isBeforeToDate(warrantyEndDate,today)&&(DateUtils.isAfterToDate(today,amcStartDate))){
			amcStatus = Status.WARRANTY_EXPIRED;
		}
		/*else if(DateUtils.isAfterOrEqualTo(renewalDate,today) && DateUtils.isBeforeOrEqualToDate(today, amcEndDate)){
			amcStatus = Status.RENEWAL_DUE;
		 }*/
		 else if(DateUtils.isBeforeToDate(amcEndDate, today)){
				amcStatus = Status.AMC_PENDING;
			 }
		 else if((DateUtils.isBeforeOrEqualToDate(amcStartDate,today))&&(DateUtils.isAfterOrEqualTo(today,amcEndDate))){
				amcStatus = Status.UNDER_AMC;
			}
		
		else if((DateUtils.isBeforeToDate(renewalDate, today))){
			renewalDays = DateUtils.daysBetween(renewalDate,today);
			if(renewalDays<=30 &&renewalDays>=0) {
				//	if((DateUtils.isAfterOrEqualTo(renewalDate,today)) && (DateUtils.isBeforeOrEqualToDate(today, amcEndDate))){
				amcStatus = Status.RENEWAL_DUE;
				}
		}
		else if ((DateUtils.isAfterToDate(today, renewalDate))){
				renewalDays = DateUtils.daysBetween(today,renewalDate);
				if(renewalDays<=30 &&renewalDays>=0) {
					//	if((DateUtils.isAfterOrEqualTo(renewalDate,today)) && (DateUtils.isBeforeOrEqualToDate(today, amcEndDate))){
					amcStatus = Status.RENEWAL_DUE;
				}
		 }
	
		}
		return amcStatus;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public String addAMCDetailsForLift(AMCDetailsDto dto, UserMetaInfo metaInfo) throws ParseException{
		RlmsLiftAmcDtls liftAmcDtls = this.constructLiftAMCDtls(dto, metaInfo);
		this.liftDao.saveLiftAMCDtls(liftAmcDtls);
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.ADDED_AMC_DTLS_SUCCESSFULLY.getMessage());
	}
	
	public RlmsLiftAmcDtls constructLiftAMCDtls(AMCDetailsDto dto, UserMetaInfo metaInfo) throws ParseException{
	    RlmsLiftAmcDtls liftAMCDtls = new RlmsLiftAmcDtls();
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapById(dto.getLiftCustoMapId());
		liftAMCDtls = 	liftDao.getRlmsLiftAmcDtlsByLiftCustomerMapId(liftCustomerMap.getLiftCustomerMapId());	
		if(liftAMCDtls==null) {
		   liftAMCDtls = new RlmsLiftAmcDtls();
	   }
		List<ServiceCall> amacServiceCalls=dto.getAmcServiceCalls();
		if(amacServiceCalls !=null && !amacServiceCalls.isEmpty()) {
			for (ServiceCall serviceCall : amacServiceCalls) {
				createServiceCalls(serviceCall,metaInfo, liftCustomerMap);
			}
		}
		if(!StringUtils.isEmpty(dto.getLiftServiceStartDate())){
			dto.setServiceStDate(DateUtils.convertStringToDateWithoutTime(dto.getLiftServiceStartDate()));
		}
		if(!StringUtils.isEmpty(dto.getLiftServiceEndDate())){
			dto.setServiceEdDate(DateUtils.convertStringToDateWithoutTime(dto.getLiftServiceEndDate()));
		}
		if(!StringUtils.isEmpty(dto.getAmcEndDate())){
			dto.setAmcEdDate(DateUtils.convertStringToDateWithoutTime(dto.getAmcEndDate()));
		}
		if(!StringUtils.isEmpty(dto.getAmcStartDate())){
			dto.setAmcStDate(DateUtils.convertStringToDateWithoutTime(dto.getAmcStartDate()));
		}
		//System.out.println("***********"+RLMSConstants.ACTIVE.getId());
		liftAMCDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		if(null != dto.getAmcEdDate()){
			liftAMCDtls.setAmcDueDate(DateUtils.addDaysToDate(dto.getAmcEdDate(), -30));
		}
		if(null != dto.getAmcEdDate()){
			liftAMCDtls.setAmcEndDate(dto.getAmcEdDate());
		}
		
		if(null !=dto.getAmcStDate()){
			liftAMCDtls.setAmcStartDate(dto.getAmcStDate());
		}
		
		if(null != liftCustomerMap){
			liftAMCDtls.setLiftCustomerMap(liftCustomerMap);
		}
		if(!StringUtils.isEmpty(dto.getLiftServiceStartDate()) && !StringUtils.isEmpty(dto.getLiftServiceEndDate())){
			Status amcStatus = this.calculateAMCStatus(dto.getAmcStDate(), dto.getAmcEdDate(), liftCustomerMap.getLiftMaster().getDateOfInstallation(), liftCustomerMap.getLiftMaster().getServiceStartDate(), liftCustomerMap.getLiftMaster().getServiceEndDate());
			liftAMCDtls.setStatus(amcStatus.getStatusId());
		}
		if(!StringUtils.isEmpty(dto.getAmcStartDate()) && !StringUtils.isEmpty(dto.getAmcEndDate())){
			Status amcStatus = this.calculateAMCStatus(dto.getAmcStDate(), dto.getAmcEdDate(), liftCustomerMap.getLiftMaster().getDateOfInstallation(), liftCustomerMap.getLiftMaster().getServiceStartDate(), liftCustomerMap.getLiftMaster().getServiceEndDate());
			liftAMCDtls.setStatus(amcStatus.getStatusId());
		}
		
		/*else {
			liftAMCDtls.setStatus( Status.NOT_UNDER_AMC.getStatusId());
			
		}*/
		if(null !=dto.getAmcStDate() && null !=dto.getAmcEdDate()){
			Status amcStatus = this.calculateAMCStatus(dto.getAmcStDate(), dto.getAmcEdDate(), liftCustomerMap.getLiftMaster().getDateOfInstallation(),liftCustomerMap.getLiftMaster().getServiceStartDate(),liftCustomerMap.getLiftMaster().getServiceEndDate());
			liftAMCDtls.setStatus(amcStatus.getStatusId());
		}
		liftAMCDtls.setAmcAmount(dto.getAmcAmount());
		liftAMCDtls.setAmcType(dto.getAmcType());
		liftAMCDtls.setUpdatedBy(metaInfo.getUserId());
		liftAMCDtls.setUpdatedDate(new Date());
		liftAMCDtls.setCreatedBy(metaInfo.getUserId());
		liftAMCDtls.setCraetedDate(new Date());
//	}
		return liftAMCDtls;	
	}
	private void createServiceCalls(ServiceCall serviceCall, UserMetaInfo metaInfo,
		RlmsLiftCustomerMap liftCustomerMap) throws ParseException {
		RlmsComplaintMaster complaintMaster = new RlmsComplaintMaster();
		complaintMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		complaintMaster.setComplaintNumber(String.valueOf(Math.random()));
		complaintMaster.setLiftCustomerMap(liftCustomerMap);
		complaintMaster.setRegistrationDate(serviceCall.getServiceDate());
		//complaintMaster.setRegistrationType(dto.getRegistrationType());
		//complaintMaster.setRemark(dto.getComplaintsRemark());
		complaintMaster.setStatus(Status.PENDING.getStatusId());
		complaintMaster.setTitle(serviceCall.getTitle());
		complaintMaster.setCreatedDate(new Date());
    	complaintMaster.setCreatedBy(metaInfo.getUserId());				
		complaintMaster.setUpdatedBy(metaInfo.getUserId());				
		complaintMaster.setUpdatedDate(new Date());
		//complaintMaster.setServiceStartDate(serviceCall.getServiceDate());
		complaintMaster.setCallType(RLMSCallType.AMC_CALL.getId());		
		Integer complaintId = this.complaintsDao.saveComplaintM(complaintMaster);
		complaintMaster.setComplaintNumber(complaintId.toString());
		this.complaintsDao.mergeComplaintM(complaintMaster);
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public List<SiteVisitReportDto> getSiteVisitReport(SiteVisitReportDto dto){
		
		//List<RlmsLiftCustomerMap> liftCustomerMapsList = liftDao.getLiftCustomerMapDtlsListByBranchCustomerMapId(dto.getListOfBranchCustoMapIds());
		
		//List<RlmsComplaintTechMapDtls> listOfComplaints = this.complaintsDao.getListOfComplaintDtlsForTechies(dto);
		
		List<RlmsComplaintTechMapDtls> listOfComplaints = this.complaintsDao.getListOfComplaintDtlsForTechies(dto);
		List<SiteVisitReportDto> listOfAllComplaints = new ArrayList<SiteVisitReportDto>();
		for (RlmsComplaintTechMapDtls rlmsComplaintTechMapDtls : listOfComplaints) {
			SiteVisitReportDto complaintwiseSiteVisitReport = new SiteVisitReportDto();
			List<SiteVisitDtlsDto> listOfAllVisists = new ArrayList<SiteVisitDtlsDto>();
			List<RlmsSiteVisitDtls> listOfAllVisits = this.complaintsDao.getAllVisitsForComnplaints(rlmsComplaintTechMapDtls.getComplaintTechMapId());
			if(null != rlmsComplaintTechMapDtls.getComplaintMaster().getRegistrationDate()){
				complaintwiseSiteVisitReport.setComplaintRegDate(DateUtils.convertDateToStringWithoutTime(rlmsComplaintTechMapDtls.getComplaintMaster().getRegistrationDate()));
			}
			
			if(null != rlmsComplaintTechMapDtls.getPlannedEndDate()){
				complaintwiseSiteVisitReport.setComplaintResolveDate(DateUtils.convertDateToStringWithoutTime(rlmsComplaintTechMapDtls.getPlannedEndDate()));
			}
			complaintwiseSiteVisitReport.setMessage(rlmsComplaintTechMapDtls.getComplaintMaster().getTitle());
			complaintwiseSiteVisitReport.setAddress(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getAddress());
			complaintwiseSiteVisitReport.setCity(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getCity());
			complaintwiseSiteVisitReport.setComplaintStatus(Status.getStringFromID(rlmsComplaintTechMapDtls.getStatus()));
			complaintwiseSiteVisitReport.setComplNumber(rlmsComplaintTechMapDtls.getComplaintMaster().getComplaintNumber());
			complaintwiseSiteVisitReport.setCustomerName(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			complaintwiseSiteVisitReport.setLiftNumber(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLiftNumber());
			complaintwiseSiteVisitReport.setTechName(rlmsComplaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + rlmsComplaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName());
			complaintwiseSiteVisitReport.setTechNumber(rlmsComplaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber());
			if(rlmsComplaintTechMapDtls.getComplaintMaster().getServiceStartDate()!=null){
				complaintwiseSiteVisitReport.setSericeDate(DateUtils.convertDateToStringWithoutTime(rlmsComplaintTechMapDtls.getComplaintMaster().getServiceStartDate()));
			}
			if(null != listOfAllVisits &&  !listOfAllVisits.isEmpty()){
				complaintwiseSiteVisitReport.setTotalNoOfVisits(listOfAllVisits.size());
			}
			Long totalTimeForComplaint = 0L;
		//	Long totalVisitTimeForComplaint = 0L;
			for (RlmsSiteVisitDtls rlmsSiteVisitDtls : listOfAllVisits) {
				SiteVisitDtlsDto siteVisitDto = new SiteVisitDtlsDto();
				siteVisitDto.setFromDateDtr(DateUtils.convertDateToStringWithoutTime(rlmsSiteVisitDtls.getFromDate()));
				siteVisitDto.setToDateStr(DateUtils.convertDateToStringWithoutTime(rlmsSiteVisitDtls.getToDate()));
				String totalTime = DateUtils.convertTimeIntoDaysHrMin(DateUtils.getDateDiff(rlmsSiteVisitDtls.getFromDate(), rlmsSiteVisitDtls.getToDate(), TimeUnit.SECONDS), TimeUnit.SECONDS);
				if(null != totalTime){
					siteVisitDto.setTotalTime(totalTime);
				}
				totalTimeForComplaint = totalTimeForComplaint + DateUtils.getDateDiff(rlmsSiteVisitDtls.getFromDate(), rlmsSiteVisitDtls.getToDate(), TimeUnit.SECONDS);
				siteVisitDto.setRemark(rlmsSiteVisitDtls.getRemark());
				listOfAllVisists.add(siteVisitDto);
			}
			if(null != totalTimeForComplaint){
			//	totalVisitTimeForComplaint = totalVisitTimeForComplaint+totalTimeForComplaint;
				complaintwiseSiteVisitReport.setTotalTimeTaken(DateUtils.convertTimeIntoDaysHrMin(totalTimeForComplaint, TimeUnit.SECONDS));
			}
			complaintwiseSiteVisitReport.setListOFAllVisits(listOfAllVisists);
		//	complaintwiseSiteVisitReport.setTotalVisitTimeTaken(totalVisitTimeForComplaint);
			complaintwiseSiteVisitReport.setUserRating(rlmsComplaintTechMapDtls.getUserRating());
			listOfAllComplaints.add(complaintwiseSiteVisitReport);
		}
		return listOfAllComplaints;
	}
	
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TechnicianWiseReportDTO> getTechnicianWiseReport(TechnicianWiseReportDTO dto){
		List<Integer> listOfUserRoleIds = new ArrayList<Integer>();
		List<TechnicianWiseReportDTO> technicianList = new ArrayList<TechnicianWiseReportDTO>();
		List<RlmsUserRoles> listOfAllTechnicians = this.userRoleDao.getAllUserWithRoleForBranch(dto.getBranchCompanyMapId(), null, SpocRoleConstants.TECHNICIAN.getSpocRoleId());
		for (RlmsUserRoles userRoles : listOfAllTechnicians) {
			listOfUserRoleIds.add(userRoles.getUserRoleId());
		}
		if(!listOfUserRoleIds.isEmpty()){
			if(null == dto.getListOfUserRoleIds() || dto.getListOfUserRoleIds().isEmpty()){
				dto.setListOfUserRoleIds(listOfUserRoleIds);
			}
		}
		List<RlmsComplaintTechMapDtls> listOfComplaints = this.complaintsDao.getListOfComplaintDtlsForTechies(dto);
		List<SiteVisitReportDto> listOfAllComplaints = new ArrayList<SiteVisitReportDto>();
		for (Integer userRoleId : listOfUserRoleIds) {
			List<RlmsComplaintTechMapDtls> tempListOfComplaints = new ArrayList<RlmsComplaintTechMapDtls>(listOfComplaints);
			RlmsUserRoles userRoles = this.userRoleDao.getUserRoleToRegister(userRoleId);
			CollectionUtils.filter(tempListOfComplaints, new UserRolePredicate(userRoleId));
			TechnicianWiseReportDTO technician = new TechnicianWiseReportDTO();
			technician.setTechnicianName(userRoles.getRlmsUserMaster().getFirstName() + " " + userRoles.getRlmsUserMaster().getLastName());
			technician.setContactNumber(userRoles.getRlmsUserMaster().getContactNumber());
			technician.setBranchname(userRoles.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			technician.setCompanyName(userRoles.getRlmsCompanyMaster().getCompanyName());
			Long totalTimeForAllComplaints = 0L;
			Integer totalResolvedComplaints = 0;
			Integer avgRating = 0;
			Integer ratCount = 0 ;
			for (RlmsComplaintTechMapDtls rlmsComplaintTechMapDtls : tempListOfComplaints) {
				if(Status.RESOLVED.getStatusId().equals(rlmsComplaintTechMapDtls.getStatus())){
					totalResolvedComplaints++;
					List<RlmsSiteVisitDtls> listOfAllVisits = this.complaintsDao.getAllVisitsForComnplaints(rlmsComplaintTechMapDtls.getComplaintTechMapId());
					for (RlmsSiteVisitDtls rlmsSiteVisitDtls : listOfAllVisits) {
						totalTimeForAllComplaints = totalTimeForAllComplaints + rlmsSiteVisitDtls.getTotalTime();
					}
				}
				if(null != rlmsComplaintTechMapDtls.getUserRating()){
					avgRating = avgRating + rlmsComplaintTechMapDtls.getUserRating();
					ratCount++;
				}
			}
			if(ratCount > 0 && avgRating > 0){
				avgRating = avgRating / ratCount;
			}
			technician.setUserRating(avgRating);
			if(null != tempListOfComplaints){
				technician.setTotalComplaintsAssigned(tempListOfComplaints.size());
			}else{
				technician.setTotalComplaintsAssigned(RLMSConstants.ZERO.getId());
			}
			technician.setTotalComplaintsResolved(totalResolvedComplaints);
			if(totalTimeForAllComplaints > 0L && totalResolvedComplaints > 0){
				Long avgTimeTaken = totalTimeForAllComplaints/totalResolvedComplaints;
				technician.setAvgTimeTaken(DateUtils.convertTimeIntoDaysHrMin(avgTimeTaken, TimeUnit.SECONDS));
			}else{
				technician.setAvgTimeTaken(RLMSConstants.NA.getName());
			}
			technicianList.add(technician);
		}
		return technicianList;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void changeStatusToAMCRenewalAndNotifyUser() throws UnsupportedEncodingException{
		List<RlmsLiftAmcDtls> listOfAllLifts = this.liftDao.getAllLiftsWithTodaysDueDate();
		for (RlmsLiftAmcDtls rlmsLiftAmcDtls : listOfAllLifts) {
			rlmsLiftAmcDtls.setStatus(Status.RENEWAL_DUE.getStatusId());
			this.liftDao.mergeLiftAMCDtls(rlmsLiftAmcDtls);
			
			List<String> listOfDynamicValues = new ArrayList<String>();
			listOfDynamicValues.add(rlmsLiftAmcDtls.getLiftCustomerMap().getLiftMaster().getLiftNumber());
			listOfDynamicValues.add(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getAddress()+ ", " + rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getArea() + ", " + rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCity());
			listOfDynamicValues.add(DateUtils.convertDateToStringWithoutTime(rlmsLiftAmcDtls.getAmcDueDate()));
			listOfDynamicValues.add(DateUtils.convertDateToStringWithoutTime(rlmsLiftAmcDtls.getAmcEndDate()));
			
			List<RlmsUserRoles> listOfAdmin = this.userRoleDao.getAllUserWithRoleForBranch(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getBranchCustoMapId(), SpocRoleConstants.BRANCH_ADMIN.getSpocRoleId());
			if(null != listOfAdmin && !listOfAdmin.isEmpty()){
				listOfDynamicValues.add(listOfAdmin.get(0).getRlmsUserMaster().getFirstName() + " " + listOfAdmin.get(0).getRlmsUserMaster().getLastName() + " (" + listOfAdmin.get(0).getRlmsUserMaster().getContactNumber() + ")");
			}else{
				RlmsUserRoles companyAdmin = this.userRoleDao.getUserWithRoleForCompany(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyId(), SpocRoleConstants.COMPANY_ADMIN.getSpocRoleId());
				if(null != companyAdmin){
					listOfDynamicValues.add(companyAdmin.getRlmsUserMaster().getFirstName() + " " + companyAdmin.getRlmsUserMaster().getLastName() + " (" + companyAdmin.getRlmsUserMaster().getContactNumber() + ")");
				}
			}
			
			List<String> toList = new ArrayList<String>();
			toList.add(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getEmailID());
			this.messagingService.sendAMCMail(listOfDynamicValues, toList, com.rlms.constants.EmailTemplateEnum.AMC_RENEWAL.getTemplateId());
		}
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void changeStatusToAMCExpiryAndNotifyUser() throws UnsupportedEncodingException{
		List<RlmsLiftAmcDtls> listOfAllLifts = this.liftDao.getAllLiftsWithTodaysExpiryDate();
		for (RlmsLiftAmcDtls rlmsLiftAmcDtls : listOfAllLifts) {
			rlmsLiftAmcDtls.setStatus(Status.AMC_PENDING.getStatusId());
			this.liftDao.mergeLiftAMCDtls(rlmsLiftAmcDtls);

			List<String> listOfDynamicValues = new ArrayList<String>();
			listOfDynamicValues.add(rlmsLiftAmcDtls.getLiftCustomerMap().getLiftMaster().getLiftNumber());
			listOfDynamicValues.add(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getAddress()+ ", " + rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getArea() + ", " + rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCity());
			listOfDynamicValues.add(DateUtils.convertDateToStringWithoutTime(rlmsLiftAmcDtls.getAmcEndDate()));
			
			List<RlmsUserRoles> listOfAdmin = this.userRoleDao.getAllUserWithRoleForBranch(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getBranchCustoMapId(), SpocRoleConstants.BRANCH_ADMIN.getSpocRoleId());
			if(null != listOfAdmin && !listOfAdmin.isEmpty()){
				listOfDynamicValues.add(listOfAdmin.get(0).getRlmsUserMaster().getFirstName() + " " + listOfAdmin.get(0).getRlmsUserMaster().getLastName() + " (" + listOfAdmin.get(0).getRlmsUserMaster().getContactNumber() + ")");
			}else{
				RlmsUserRoles companyAdmin = this.userRoleDao.getUserWithRoleForCompany(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyId(), SpocRoleConstants.COMPANY_ADMIN.getSpocRoleId());
				if(null != companyAdmin){
					listOfDynamicValues.add(companyAdmin.getRlmsUserMaster().getFirstName() + " " + companyAdmin.getRlmsUserMaster().getLastName() + " (" + companyAdmin.getRlmsUserMaster().getContactNumber() + ")");
				}
			}
			List<String> toList = new ArrayList<String>();
			toList.add(rlmsLiftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getEmailID());
			this.messagingService.sendAMCMail(listOfDynamicValues, toList, com.rlms.constants.EmailTemplateEnum.AMC_EXPIRED.getTemplateId());
		}
	}
	@Override
	public List<EventDtlsDto> getAllInOutEventsData(EventDtlsDto dto) {
		List<RlmsEventDtls> listOfEvents = new ArrayList<RlmsEventDtls>();
		List<EventDtlsDto>dtlsDtoList = new ArrayList<EventDtlsDto>();
		try {
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			if(dto.getBranchCustomerMapId()!=null) {
			List<Integer>branchCustMapIds=dto.getBranchCustomerMapId();
			for (Integer integer : branchCustMapIds) {
				
				LiftDtlsDto dtoTemp = new LiftDtlsDto();
			    dtoTemp.setBranchCustomerMapId(integer);
				List<RlmsLiftCustomerMap> list = dashboardService
						.getAllLiftsForBranchsOrCustomer(dtoTemp);
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap
							.getLiftCustomerMapId());
					}		
				}
			}
			else {
				liftCustomerMapIds=dto.getLiftCustomerMapId();
			}
		//	logger.info("Method :: getAllBranchesForCompany");
			listOfEvents = dashBoardDao.getAllEventDtlsForDashboard(liftCustomerMapIds,dto.getEventType());
	        for (RlmsEventDtls  rlmsEventDtls : listOfEvents) {
				EventDtlsDto dtlsDto =new EventDtlsDto();
				dtlsDto.setBranchName(rlmsEventDtls.getRlmsLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
				dtlsDto.setCustomerName(rlmsEventDtls.getRlmsLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
				dtlsDto.setImei(rlmsEventDtls.getImeiId());
				dtlsDto.setEventDescription(rlmsEventDtls.getEventDescription());
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		        format.setTimeZone(TimeZone.getTimeZone("IST"));
		        String eventDate = format.format(rlmsEventDtls.getEventDate());
				dtlsDto.setDate(eventDate);
				dtlsDto.setEventType(rlmsEventDtls.getEventType());
				dtlsDto.setLiftNumber(rlmsEventDtls.getRlmsLiftCustomerMap().getLiftMaster().getLiftNumber());
				dtlsDto.setLiftAddress(rlmsEventDtls.getRlmsLiftCustomerMap().getLiftMaster().getAddress());
				dtlsDto.setCity(rlmsEventDtls.getRlmsLiftCustomerMap().getLiftMaster().getCity());
				dtlsDto.setEventFromContactNo(rlmsEventDtls.getFromContact());
				dtlsDto.setLmsResponseContactNo(rlmsEventDtls.getLmsResponseUserContactNo());
				dtlsDtoList.add(dtlsDto);
			}
		} catch (Exception e) {
		}
		return dtlsDtoList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getCallDetailedReport(ComplaintsDtlsDto dto) {
		List<ComplaintsDto> complaintsDtoList = new ArrayList<>();
		List<RlmsComplaintMaster> complaintList = new ArrayList<>();
		
		List<RlmsLiftCustomerMap> liftCustomerMapList = liftDao.getliftCustomerMapDtlsByBranchCutomerId(dto);
		List<Integer> listCustMapIds = new ArrayList<>();
		for (RlmsLiftCustomerMap  liftCustomerMap : liftCustomerMapList) {
			listCustMapIds.add(liftCustomerMap.getLiftCustomerMapId());
		}
		complaintList = complaintsDao.complaintMastersList(listCustMapIds,dto);
		for (RlmsComplaintMaster rlmsComplaintMaster : complaintList) {
			ComplaintsDto complaintsDto = new ComplaintsDto();
			complaintsDto.setServiceCallTypeStr(RLMSCallType.getStringFromID(rlmsComplaintMaster.getCallType()));
			complaintsDto.setTitle(rlmsComplaintMaster.getTitle());
			complaintsDto.setComplaintNumber(rlmsComplaintMaster.getComplaintNumber());
			complaintsDto.setLiftNumber(rlmsComplaintMaster.getLiftCustomerMap().getLiftMaster().getLiftNumber());
			complaintsDto.setCustomerName(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			complaintsDto.setCustomerCity(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCity());
			complaintsDto.setCustomerAddress(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getAddress());
			complaintsDto.setRegistrationDateStr(DateUtils.convertDateToStringWithoutTime(rlmsComplaintMaster.getRegistrationDate()));
            complaintsDto.setStatus(Status.getStringFromID(rlmsComplaintMaster.getStatus()));
            complaintsDto.setServiceCallType(rlmsComplaintMaster.getCallType());
        //	RlmsUserRoles  rlmsUserRoles = userRoleDao.getUserRoleByUserId(rlmsComplaintMaster.getCreatedBy());
            RlmsComplaintTechMapDtls  complaintTechMapDtls = complaintsDao.getComplTechMapObjByComplaintId(rlmsComplaintMaster.getComplaintId());		
            if(complaintTechMapDtls!=null) {
			complaintsDto.setCallAssignedDateStr(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getAssignedDate()));
			// complaintsDto.setLastVisitedDateStr(DateUtils.convertDateTimestampToStringWithTime(complaintTechMapDtls.getUpdatedDate()));
                if(complaintTechMapDtls.getStatus()==Status.RESOLVED.getStatusId()) {
                 	complaintsDto.setResolvedDateStr(DateUtils.convertDateToStringWithoutTime(rlmsComplaintMaster.getUpdatedDate()));
                }
                if(complaintTechMapDtls.getStatus()==Status.RESOLVED.getStatusId()||complaintTechMapDtls.getStatus()==Status.INPROGESS.getStatusId()) {
                Date updateDateDiff = 	rlmsComplaintMaster.getUpdatedDate();
                updateDateDiff = DateUtils.addDaysToDate(updateDateDiff, 1);
                 String totalDaysForComplaint = DateUtils.convertTimeIntoDaysHrMin(DateUtils.getDateDiff(rlmsComplaintMaster.getRegistrationDate(), updateDateDiff,TimeUnit.SECONDS),TimeUnit.SECONDS);
                 //int totalDays =  DateUtils.daysBetween(complaintTechMapDtls.getAssignedDate(),complaintTechMapDtls.getUpdatedDate());
                	complaintsDto.setTotalDaysForComplaint(totalDaysForComplaint);
                	  List<RlmsSiteVisitDtls> listOfAllVisits = this.complaintsDao.getAllVisitsForComnplaints(complaintTechMapDtls.getComplaintTechMapId());
                  	  if(listOfAllVisits!=null && !listOfAllVisits.isEmpty()) {
                  		complaintsDto.setTotalAttempts(listOfAllVisits.size());
                  	  }
                  	  complaintsDto.setLastVisitedDateStr(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getUpdatedDate()));
                }
                	complaintsDto.setToDateStr(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getUpdatedDate()));
    	            complaintsDto.setFromDateStr(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getAssignedDate()));
    	           	complaintsDto.setTechnicianDtls(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName()+" "+complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName());
    	           	complaintsDto.setRemark(rlmsComplaintMaster.getRemark());
            }
        	if(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId().equals(rlmsComplaintMaster.getRegistrationType())){
    		   		RlmsUserRoles  rlmsUserRoles  = userRoleDao.getUserRoleByUserId(rlmsComplaintMaster.getCreatedBy());
    			  complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName() +" "+rlmsUserRoles.getRlmsUserMaster().getLastName()+"("+rlmsUserRoles.getRole()+")");
    		}else if(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId().equals(rlmsComplaintMaster.getRegistrationType())){
    			RlmsMemberMaster memberMaster = userService.getMemberById(rlmsComplaintMaster.getCreatedBy());
    			if(memberMaster!=null) {
    				 complaintsDto.setRegisteredBy(memberMaster.getFirstName() +" "+memberMaster.getLastName()+"("+memberMaster.getContactNumber()+")");
    			}
    		}
        	 if(RLMSCallType.AMC_CALL.getId() == rlmsComplaintMaster.getCallType()){
				   complaintsDto.setRegisteredBy("Autogenerated AMC Call");
				}
        /*    if(rlmsUserRoles.getRlmsSpocRoleMaster().getSpocRoleId()==SpocRoleConstants.END_USER.getSpocRoleId()){
                complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName()+""+rlmsUserRoles.getRlmsUserMaster().getLastName()+" "+"("+"USER"+")");
            }
            else if(rlmsUserRoles.getRlmsSpocRoleMaster().getSpocRoleId()==SpocRoleConstants.COMPANY_OPERATOR.getSpocRoleId()){

                complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName()+" "+rlmsUserRoles.getRlmsUserMaster().getLastName()+" "+"("+"OPERATOR"+")");
            }else if(rlmsUserRoles.getRlmsSpocRoleMaster().getSpocRoleId()==SpocRoleConstants.COMPANY_ADMIN.getSpocRoleId()){

                complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName()+" "+rlmsUserRoles.getRlmsUserMaster().getLastName()+" "+"("+"ADMIN"+")");
            }*/
            complaintsDto.setBranchName(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
            complaintsDtoList.add(complaintsDto);
		}
		return complaintsDtoList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
    public List<CallSpecificReportDto> getCallSpecificReport(ComplaintsDtlsDto dto) {
		
		List<CallSpecificReportDto> complaintsDtoList = new ArrayList<>();
		List<Integer> listCustMapIds = new ArrayList<>();
		
		if(dto.getListOfLiftCustoMapId()!=null) {
			listCustMapIds= dto.getListOfLiftCustoMapId();
		}
		else {
		List<RlmsLiftCustomerMap> liftCustomerMapList = liftDao.getliftCustomerMapDtlsByBranchCutomerId(dto);
		for (RlmsLiftCustomerMap  liftCustomerMap : liftCustomerMapList) {
			listCustMapIds.add(liftCustomerMap.getLiftCustomerMapId());
		}
	}
		List<RlmsComplaintMaster> complaintList = complaintsDao.complaintMastersList(listCustMapIds,dto);
		
		for (RlmsComplaintMaster rlmsComplaintMaster : complaintList) {
			CallSpecificReportDto complaintsDto = new CallSpecificReportDto();
			complaintsDto.setServiceCallTypeStr(RLMSCallType.getStringFromID(rlmsComplaintMaster.getCallType()));
			complaintsDto.setTitle(rlmsComplaintMaster.getTitle());
			complaintsDto.setComplaintNumber(rlmsComplaintMaster.getComplaintNumber());
			complaintsDto.setLiftNumber(rlmsComplaintMaster.getLiftCustomerMap().getLiftMaster().getLiftNumber());
			complaintsDto.setCustomerName(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			complaintsDto.setCustomerCity(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCity());
			complaintsDto.setCustomerAddress(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getAddress());
			complaintsDto.setRegistrationDateStr(DateUtils.convertDateToStringWithoutTime(rlmsComplaintMaster.getRegistrationDate()));
            complaintsDto.setStatus(Status.getStringFromID(rlmsComplaintMaster.getStatus()));
            //complaintsDto.setServiceCallType(rlmsComplaintMaster.getCallType());
            complaintsDto.setComplaintNumber(rlmsComplaintMaster.getComplaintNumber());
            
        	if(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId().equals(rlmsComplaintMaster.getRegistrationType())){
    		   		RlmsUserRoles  rlmsUserRoles  = userRoleDao.getUserRoleByUserId(rlmsComplaintMaster.getCreatedBy());
    			  complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName() +" "+rlmsUserRoles.getRlmsUserMaster().getLastName()+"("+rlmsUserRoles.getRole()+")");
    		}else if(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId().equals(rlmsComplaintMaster.getRegistrationType())){
    			RlmsMemberMaster memberMaster = userService.getMemberById(rlmsComplaintMaster.getCreatedBy());
    			if(memberMaster!=null) {
    				 complaintsDto.setRegisteredBy(memberMaster.getFirstName() +" "+memberMaster.getLastName()+"("+memberMaster.getContactNumber()+")");
    			}
    		}
            
        /*	RlmsUserRoles  rlmsUserRoles = userRoleDao.getUserRoleByUserId(rlmsComplaintMaster.getCreatedBy());
        	  if(rlmsUserRoles.getRlmsSpocRoleMaster().getSpocRoleId()==SpocRoleConstants.END_USER.getSpocRoleId()){
                  complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName()+""+rlmsUserRoles.getRlmsUserMaster().getLastName()+" "+"("+"USER"+")");
              }
              else if(rlmsUserRoles.getRlmsSpocRoleMaster().getSpocRoleId()==SpocRoleConstants.COMPANY_OPERATOR.getSpocRoleId()){

                  complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName()+" "+rlmsUserRoles.getRlmsUserMaster().getLastName()+" "+"("+"COMPANY OPERATOR"+")");
              }else if(rlmsUserRoles.getRlmsSpocRoleMaster().getSpocRoleId()==SpocRoleConstants.COMPANY_ADMIN.getSpocRoleId()){

                  complaintsDto.setRegisteredBy(rlmsUserRoles.getRlmsUserMaster().getFirstName()+" "+rlmsUserRoles.getRlmsUserMaster().getLastName()+" "+"("+"COMPANY ADMIN"+")");
              }*/
              complaintsDto.setBranchName(rlmsComplaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
        	
              RlmsComplaintTechMapDtls  complaintTechMapDtls = complaintsDao.getComplTechMapObjByComplaintId(rlmsComplaintMaster.getComplaintId());		
              if(complaintTechMapDtls!=null) {
             //	 complaintsDto.setCallAssignedDateStr(DateUtils.convertDateToStringWithTime(complaintTechMapDtls.getAssignedDate()));

             	 complaintsDto.setCallAssignedDateStr(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getAssignedDate()));

            	  
            	 /* complaintsDto.setCallAssignedDateStr(DateUtils.convertDateToStringWithTime(complaintTechMapDtls.getAssignedDate()));
            	  if(complaintTechMapDtls.getStatus()==Status.RESOLVED.getStatusId()) {
            		  complaintsDto.setResolvedDateStr(DateUtils.convertDateToStringWithTime(rlmsComplaintMaster.getUpdatedDate()));
            		  int totalDays =  DateUtils.daysBetween(complaintTechMapDtls.getAssignedDate(),complaintTechMapDtls.getUpdatedDate());
            		  complaintsDto.setTotalDaysRequiredToResolveComplaint(totalDays);
            	  }
            	  complaintsDto.setToDateStr(DateUtils.convertDateToStringWithTime(complaintTechMapDtls.getUpdatedDate()));
            	  complaintsDto.setFromDateStr(DateUtils.convertDateToStringWithTime(complaintTechMapDtls.getAssignedDate()));
    	            
            	  complaintsDto.setTechnicianDtls(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName()+" "+complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName());
            	  complaintsDto.setRemark(rlmsComplaintMaster.getRemark());*/
    	           	//
            	  SiteVisitReportDto complaintwiseSiteVisitReport = new SiteVisitReportDto();
            	  List<SiteVisitDtlsDto> listOfAllVisists = new ArrayList<SiteVisitDtlsDto>();
            	  List<RlmsSiteVisitDtls> listOfAllVisits = this.complaintsDao.getAllVisitsForComnplaints(complaintTechMapDtls.getComplaintTechMapId());
            	  if(null != complaintTechMapDtls.getComplaintMaster().getRegistrationDate()){
            		  complaintwiseSiteVisitReport.setComplaintRegDate(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getComplaintMaster().getRegistrationDate()));
            	  }
            	  if(complaintTechMapDtls.getStatus()==Status.RESOLVED.getStatusId()) {
                  	 complaintwiseSiteVisitReport.setComplaintResolveDate(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getUpdatedDate()));
            	  }
            	//  if(null != complaintTechMapDtls.getPlannedEndDate()){
            	//	  complaintwiseSiteVisitReport.setComplaintResolveDate(DateUtils.convertDateTimestampToStringWithTime(complaintTechMapDtls.getPlannedEndDate()));
            	 // }
            	  complaintwiseSiteVisitReport.setMessage(complaintTechMapDtls.getComplaintMaster().getTitle());
            	  complaintwiseSiteVisitReport.setAddress(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getAddress());
            	  complaintwiseSiteVisitReport.setCity(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getCity());
            	  complaintwiseSiteVisitReport.setComplaintStatus(Status.getStringFromID(complaintTechMapDtls.getStatus()));
            	  complaintwiseSiteVisitReport.setComplNumber(complaintTechMapDtls.getComplaintMaster().getComplaintNumber());
            	  complaintwiseSiteVisitReport.setCustomerName(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
            	  complaintwiseSiteVisitReport.setLiftNumber(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLiftNumber());
            	  complaintwiseSiteVisitReport.setTechName(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName());
            	  complaintwiseSiteVisitReport.setTechNumber(complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber());
            	  if(complaintTechMapDtls.getComplaintMaster().getServiceStartDate()!=null){
            		  complaintwiseSiteVisitReport.setSericeDate(DateUtils.convertDateToStringWithoutTime(complaintTechMapDtls.getComplaintMaster().getServiceStartDate()));
    				}
            	  if(null != listOfAllVisits &&  !listOfAllVisits.isEmpty()){
            		  complaintwiseSiteVisitReport.setTotalNoOfVisits(listOfAllVisits.size());
            	  
            	  Long totalTimeForComplaint = 0L;
    			 for (RlmsSiteVisitDtls rlmsSiteVisitDtls : listOfAllVisits) {
    					SiteVisitDtlsDto siteVisitDto = new SiteVisitDtlsDto();
    					siteVisitDto.setFromDateDtr(DateUtils.convertDateToStringWithoutTime(rlmsSiteVisitDtls.getFromDate()));
    					siteVisitDto.setToDateStr(DateUtils.convertDateToStringWithoutTime(rlmsSiteVisitDtls.getToDate()));
    					String totalTime = DateUtils.convertTimeIntoDaysHrMin(DateUtils.getDateDiff(rlmsSiteVisitDtls.getFromDate(), rlmsSiteVisitDtls.getToDate(), TimeUnit.SECONDS), TimeUnit.SECONDS);
    					if(null != totalTime){
    						siteVisitDto.setTotalTime(totalTime);
    					}
    					totalTimeForComplaint = totalTimeForComplaint + DateUtils.getDateDiff(rlmsSiteVisitDtls.getFromDate(), rlmsSiteVisitDtls.getToDate(), TimeUnit.SECONDS);
    					siteVisitDto.setRemark(rlmsSiteVisitDtls.getRemark());
    					listOfAllVisists.add(siteVisitDto);
    				}
    				if(null != totalTimeForComplaint){
    					complaintwiseSiteVisitReport.setTotalTimeTaken(DateUtils.convertTimeIntoDaysHrMin(totalTimeForComplaint, TimeUnit.SECONDS));
    				}
    				complaintwiseSiteVisitReport.setListOFAllVisits(listOfAllVisists);
    			
    				complaintsDto.setSiteVisitDetailsList(complaintwiseSiteVisitReport);
              }
    				complaintwiseSiteVisitReport.setUserRating(complaintTechMapDtls.getUserRating());

              }
          //  }
            //
            complaintsDtoList.add(complaintsDto);
		}
		return complaintsDtoList;
		
		/*List<RlmsComplaintTechMapDtls> listOfComplaints = this.complaintsDao.getListOfComplaintDtlsForTechies(dto);
		List<SiteVisitReportDto> listOfAllComplaints = new ArrayList<SiteVisitReportDto>();
		for (RlmsComplaintTechMapDtls rlmsComplaintTechMapDtls : listOfComplaints) {
			SiteVisitReportDto complaintwiseSiteVisitReport = new SiteVisitReportDto();
			List<SiteVisitDtlsDto> listOfAllVisists = new ArrayList<SiteVisitDtlsDto>();
			List<RlmsSiteVisitDtls> listOfAllVisits = this.complaintsDao.getAllVisitsForComnplaints(rlmsComplaintTechMapDtls.getComplaintTechMapId());
			if(null != rlmsComplaintTechMapDtls.getComplaintMaster().getRegistrationDate()){
				complaintwiseSiteVisitReport.setComplaintRegDate(DateUtils.convertDateToStringWithTime(rlmsComplaintTechMapDtls.getComplaintMaster().getRegistrationDate()));
			}
			
			if(null != rlmsComplaintTechMapDtls.getPlannedEndDate()){
				complaintwiseSiteVisitReport.setComplaintResolveDate(DateUtils.convertDateToStringWithTime(rlmsComplaintTechMapDtls.getPlannedEndDate()));
			}
			complaintwiseSiteVisitReport.setMessage(rlmsComplaintTechMapDtls.getComplaintMaster().getTitle());
			complaintwiseSiteVisitReport.setAddress(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getAddress());
			complaintwiseSiteVisitReport.setCity(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getCity());
			complaintwiseSiteVisitReport.setComplaintStatus(Status.getStringFromID(rlmsComplaintTechMapDtls.getStatus()));
			complaintwiseSiteVisitReport.setComplNumber(rlmsComplaintTechMapDtls.getComplaintMaster().getComplaintNumber());
			complaintwiseSiteVisitReport.setCustomerName(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			complaintwiseSiteVisitReport.setLiftNumber(rlmsComplaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLiftNumber());
			complaintwiseSiteVisitReport.setTechName(rlmsComplaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + rlmsComplaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName());
			complaintwiseSiteVisitReport.setTechNumber(rlmsComplaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber());
			if(rlmsComplaintTechMapDtls.getComplaintMaster().getServiceStartDate()!=null){
				complaintwiseSiteVisitReport.setSericeDate(DateUtils.convertDateToStringWithTime(rlmsComplaintTechMapDtls.getComplaintMaster().getServiceStartDate()));
			}
			if(null != listOfAllVisits &&  !listOfAllVisits.isEmpty()){
				complaintwiseSiteVisitReport.setTotalNoOfVisits(listOfAllVisits.size());
			}
			Long totalTimeForComplaint = 0L;
			for (RlmsSiteVisitDtls rlmsSiteVisitDtls : listOfAllVisits) {
				SiteVisitDtlsDto siteVisitDto = new SiteVisitDtlsDto();
				siteVisitDto.setFromDateDtr(DateUtils.convertDateTimestampToStringWithTime(rlmsSiteVisitDtls.getFromDate()));
				siteVisitDto.setToDateStr(DateUtils.convertDateTimestampToStringWithTime(rlmsSiteVisitDtls.getToDate()));
				String totalTime = DateUtils.convertTimeIntoDaysHrMin(DateUtils.getDateDiff(rlmsSiteVisitDtls.getFromDate(), rlmsSiteVisitDtls.getToDate(), TimeUnit.SECONDS), TimeUnit.SECONDS);
				if(null != totalTime){
					siteVisitDto.setTotalTime(totalTime);
				}
				totalTimeForComplaint = totalTimeForComplaint + DateUtils.getDateDiff(rlmsSiteVisitDtls.getFromDate(), rlmsSiteVisitDtls.getToDate(), TimeUnit.SECONDS);
				siteVisitDto.setRemark(rlmsSiteVisitDtls.getRemark());
				listOfAllVisists.add(siteVisitDto);
			}
			if(null != totalTimeForComplaint){
				complaintwiseSiteVisitReport.setTotalTimeTaken(DateUtils.convertTimeIntoDaysHrMin(totalTimeForComplaint, TimeUnit.SECONDS));
			}
			complaintwiseSiteVisitReport.setListOFAllVisits(listOfAllVisists);
			complaintwiseSiteVisitReport.setUserRating(rlmsComplaintTechMapDtls.getUserRating());
			listOfAllComplaints.add(complaintwiseSiteVisitReport);
		}
		return listOfAllComplaints;
	*/
	}
}
