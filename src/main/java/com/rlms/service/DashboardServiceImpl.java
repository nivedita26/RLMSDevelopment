package com.rlms.service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.AMCType;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.SpocRoleConstants;
import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.BranchDtlsDto;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.EventCountDtls;
import com.rlms.contract.EventDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.TechnicianCount;
import com.rlms.contract.UserMetaInfo;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.dao.BranchDao;
import com.rlms.dao.ComplaintsDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.DashboardDao;
import com.rlms.dao.LiftDao;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsSiteVisitDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.predicates.LiftPredicate;
import com.rlms.propertyconfiguration.ParameterIndexPropertyConfig;
import com.rlms.propertyconfiguration.PropertyConfiguration;
import com.rlms.utils.DateUtils;

@Service
public class DashboardServiceImpl implements DashboardService {
	static ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PropertyConfiguration.class);
	static   ParameterIndexPropertyConfig paramIndex= applicationContext.getBean(ParameterIndexPropertyConfig.class);
	
	private static final Logger logger = Logger.getLogger(DashboardServiceImpl.class);

	
	@Autowired
	private DashboardDao dashboardDao;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LiftDao liftDao;
	@Autowired
	private BranchDao branchDao;
	@Autowired
	CustomerDao customerDao;
	@Autowired
	ComplaintsDao complaintsDao;
   
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDtlsDto> getAllCustomersForBranch(
			List<Integer> listOfApplicableBranchIds) {
		List<CustomerDtlsDto> listOFDtos = new ArrayList<CustomerDtlsDto>();
		List<RlmsBranchCustomerMap> listOfAllCustomers = this.customerDao
				.getAllCustomersForBranches(listOfApplicableBranchIds);
		return this.constructListOfCustomerDtlsDto(listOfAllCustomers);
	}
	
@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsLiftCustomerMap> getAllLiftsForBranchsOrCustomer(
			LiftDtlsDto dto) {
		List<RlmsLiftCustomerMap> list = this.liftDao
				.getAllLiftsForBranchsOrCustomer(dto);
		return list;
	}

	private List<CustomerDtlsDto> constructListOfCustomerDtlsDto(
			List<RlmsBranchCustomerMap> listOfCustomers) {
		List<CustomerDtlsDto> listOFDtos = new ArrayList<CustomerDtlsDto>();
		for (RlmsBranchCustomerMap branchCustomerMap : listOfCustomers) {
			List<Integer> listOfCustomer = new ArrayList<Integer>();
			listOfCustomer.add(branchCustomerMap.getCustomerMaster()
					.getCustomerId());
			CustomerDtlsDto dto = new CustomerDtlsDto();
			List<RlmsLiftCustomerMap> listOfLifts = this.liftDao
					.getAllLiftsForCustomers(listOfCustomer);
			dto.setAddress(branchCustomerMap.getCustomerMaster().getAddress());
			dto.setArea(branchCustomerMap.getCustomerMaster().getArea());
			dto.setCity(branchCustomerMap.getCustomerMaster().getCity());
			dto.setPinCode(branchCustomerMap.getCustomerMaster().getPincode());
			dto.setCntNumber(branchCustomerMap.getCustomerMaster()
					.getCntNumber());
			dto.setFirstName(branchCustomerMap.getCustomerMaster()
					.getCustomerName());
			dto.setEmailID(branchCustomerMap.getCustomerMaster().getEmailID());
			dto.setPanNumber(branchCustomerMap.getCustomerMaster()
					.getPanNumber());
			dto.setChairmanName(branchCustomerMap.getCustomerMaster()
					.getChairmanName());
			dto.setChairmanNumber(branchCustomerMap.getCustomerMaster()
					.getChairmanNumber());
			dto.setChairmanEmail(branchCustomerMap.getCustomerMaster()
					.getChairmanEmail());
			dto.setSecretaryName(branchCustomerMap.getCustomerMaster()
					.getSecretaryName());
			dto.setSecretaryNumber(branchCustomerMap.getCustomerMaster()
					.getSecretaryNumber());
			dto.setSecretaryEmail(branchCustomerMap.getCustomerMaster()
					.getSecretaryEmail());
			dto.setTreasurerName(branchCustomerMap.getCustomerMaster()
					.getTreasurerName());
			dto.setTreasurerNumber(branchCustomerMap.getCustomerMaster()
					.getTreasurerNumber());
			dto.setTreasurerEmail(branchCustomerMap.getCustomerMaster()
					.getTreasurerEmail());
			dto.setWatchmenName(branchCustomerMap.getCustomerMaster()
					.getWatchmenName());
			dto.setWatchmenNumber(branchCustomerMap.getCustomerMaster()
					.getWatchmenNumber());
			dto.setWatchmenEmail(branchCustomerMap.getCustomerMaster()
					.getWatchmenEmail());
			if (null != listOfLifts && !listOfLifts.isEmpty()) {
				dto.setTotalNumberOfLifts(listOfLifts.size());
			}
			dto.setBranchName(branchCustomerMap.getCompanyBranchMapDtls()
					.getRlmsBranchMaster().getBranchName());
			dto.setCompanyName(branchCustomerMap.getCompanyBranchMapDtls()
					.getRlmsCompanyMaster().getCompanyName());
			dto.setBranchCustomerMapId(branchCustomerMap.getBranchCustoMapId());
			listOFDtos.add(dto);
		}
		return listOFDtos;
	}


	@Transactional(propagation = Propagation.REQUIRED)
	public List<AMCDetailsDto> getAMCDetailsForDashboard(List<Integer> liftCustomerMapId,AMCDetailsDto amcDetailsDto) {
		List<AMCDetailsDto> listOFAMCDetails = new ArrayList<AMCDetailsDto>();
		List<Integer> listOfLiftsForAMCDtls = new ArrayList<Integer>();
		List<RlmsLiftCustomerMap> listOFApplicableLifts = new ArrayList<RlmsLiftCustomerMap>();

		listOFApplicableLifts = this.liftDao.getAllLiftsByIds(liftCustomerMapId);
		for (RlmsLiftCustomerMap rlmsLiftCustomerMap : listOFApplicableLifts) {
			listOfLiftsForAMCDtls.add(rlmsLiftCustomerMap
					.getLiftCustomerMapId());
		}

		List<RlmsLiftAmcDtls> listOfAMCDtls = this.liftDao
				.getAMCDetilsForLifts(listOfLiftsForAMCDtls, amcDetailsDto);
		Set<Integer> liftIds = new HashSet<Integer>();
		for (RlmsLiftAmcDtls liftAmcDtls : listOfAMCDtls) {
			liftIds.add(liftAmcDtls.getLiftCustomerMap().getLiftMaster()
					.getLiftId());
		}

		for (Integer liftId : liftIds) {
			List<RlmsLiftAmcDtls> listForLift = new ArrayList<RlmsLiftAmcDtls>(
					listOfAMCDtls);
			CollectionUtils.filter(listForLift, new LiftPredicate(liftId));
			listOFAMCDetails.addAll(this.constructListOFAMcDtos(listForLift));
		}

		return listOFAMCDetails;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto) {
		List<ComplaintsDto> listOfAllComplaints = new ArrayList<ComplaintsDto>();
		List<RlmsComplaintMaster> listOfComplaints = this.dashboardDao
				.getAllComplaintsForGivenCriteria(dto.getBranchCompanyMapId(),
						dto.getBranchCustomerMapId(),
						dto.getListOfLiftCustoMapId(), dto.getStatusList(),
						dto.getFromDate(), dto.getToDate(),0);
		for (RlmsComplaintMaster rlmsComplaintMaster : listOfComplaints) {
			ComplaintsDto complaintsDto = this
					.constructComplaintDto(rlmsComplaintMaster);
			listOfAllComplaints.add(complaintsDto);
		}

		return listOfAllComplaints;
	}

	private ComplaintsDto constructComplaintDto(
			RlmsComplaintMaster complaintMaster) {
		ComplaintsDto dto = new ComplaintsDto();
		if (RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId().equals(
				complaintMaster.getRegistrationType())) {
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN
					.getName());
		} else if (RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId().equals(
				complaintMaster.getRegistrationType())) {
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_END_USER
					.getName());
		} else if (RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getId().equals(
				complaintMaster.getRegistrationType())) {
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT
					.getName());
		}
		dto.setComplaintId(complaintMaster.getComplaintId());
		dto.setComplaintNumber(complaintMaster.getComplaintNumber());
		dto.setCustomerName(complaintMaster.getLiftCustomerMap()
				.getBranchCustomerMap().getCustomerMaster().getCustomerName());
		dto.setLiftAddress(complaintMaster.getLiftCustomerMap().getLiftMaster()
				.getAddress());
		dto.setRegistrationDate(complaintMaster.getRegistrationDate());
		if (null != complaintMaster.getRegistrationDate()) {
			dto.setRegistrationDateStr(DateUtils
					.convertDateToStringWithoutTime(complaintMaster
							.getRegistrationDate()));
		}
		dto.setActualServiceEndDate(complaintMaster.getActualServiceEndDate());
		if (null != complaintMaster.getActualServiceEndDate()) {
			dto.setActualServiceEndDateStr(DateUtils
					.convertDateToStringWithoutTime(complaintMaster
							.getActualServiceEndDate()));
		}
		dto.setRemark(complaintMaster.getRemark());
		dto.setTitle(complaintMaster.getTitle());
		dto.setServiceStartDate(complaintMaster.getServiceStartDate());
		if (null != complaintMaster.getServiceStartDate()) {
			dto.setServiceStartDateStr(DateUtils
					.convertDateToStringWithoutTime(complaintMaster
							.getServiceStartDate()));
		}
		if (!Status.PENDING.getStatusId().equals(complaintMaster.getStatus())) {
			RlmsComplaintTechMapDtls complaintTechMapDtls = this.dashboardDao
					.getComplTechMapObjByComplaintId(complaintMaster
							.getComplaintId());
			if (null != complaintTechMapDtls) {
				String techDtls = complaintTechMapDtls.getUserRoles()
						.getRlmsUserMaster().getFirstName()
						+ " "
						+ complaintTechMapDtls.getUserRoles()
								.getRlmsUserMaster().getLastName()
						+ " ("
						+ complaintTechMapDtls.getUserRoles()
								.getRlmsUserMaster().getContactNumber() + ")";
				dto.setTechnicianDtls(techDtls);
			}
		} else {
			dto.setTechnicianDtls("-");
		}
		if (Status.PENDING.getStatusId().equals(complaintMaster.getStatus())) {
			dto.setStatus(Status.PENDING.getStatusMsg());
		} else if (Status.ASSIGNED.getStatusId().equals(
				complaintMaster.getStatus())) {
			dto.setStatus(Status.ASSIGNED.getStatusMsg());
		} else if (Status.INPROGESS.getStatusId().equals(
				complaintMaster.getStatus())) {
			dto.setStatus(Status.INPROGESS.getStatusMsg());
		} else if (Status.COMPLETED.getStatusId().equals(
				complaintMaster.getStatus())) {
			dto.setStatus(Status.COMPLETED.getStatusMsg());
		}
		String complaintent = null;
		if (RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId() == complaintMaster
				.getRegistrationType()) {
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getName());
			RlmsUserRoles userRoles = this.userService
					.getUserRoleObjhById(complaintMaster.getCreatedBy());
			if(userRoles!=null){
				complaintent = userRoles.getRlmsUserMaster().getFirstName() + " "
						+ userRoles.getRlmsUserMaster().getLastName() + " ("
						+ userRoles.getRlmsUserMaster().getContactNumber() + ")";
			}
		} else if (RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId() == complaintMaster
				.getRegistrationType()) {
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getName());
			RlmsMemberMaster memberMaster = this.customerService
					.getMemberById(complaintMaster.getCreatedBy());
			complaintent = memberMaster.getFirstName() + " "
					+ memberMaster.getLastName() + " ("
					+ memberMaster.getContactNumber() + ")";
		} else if (RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getId() == complaintMaster
				.getRegistrationType()) {
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT
					.getName());
			complaintent = RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT
					.getName();
		}
		dto.setComplaintent(complaintent);
		dto.setUpdatedDate(complaintMaster.getUpdatedDate());
		dto.setCompanyName(complaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
		return dto;
	}

	private List<AMCDetailsDto> constructListOFAMcDtos(
			List<RlmsLiftAmcDtls> listOFAMCs) {
		List<AMCDetailsDto> listOFDtos = new ArrayList<AMCDetailsDto>();
		int i = 0;
		for (RlmsLiftAmcDtls liftAmcDtls : listOFAMCs) {
			AMCDetailsDto dto = new AMCDetailsDto();
			if (null != liftAmcDtls.getAmcEndDate()) {
				dto.setAmcEdDate(liftAmcDtls.getAmcEndDate());
				dto.setAmcEndDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcEndDate()));
			}
			if (null != liftAmcDtls.getAmcStartDate()) {
				dto.setAmcStDate(liftAmcDtls.getAmcStartDate());
				dto.setAmcStartDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcStartDate()));
			}

			dto.setCustomerName(liftAmcDtls.getLiftCustomerMap()
					.getBranchCustomerMap().getCustomerMaster()
					.getCustomerName());
			if (null != liftAmcDtls.getAmcDueDate()) {
				dto.setAmcDueDate(liftAmcDtls.getAmcDueDate());
				dto.setDueDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcDueDate()));
			}

			if (null != liftAmcDtls.getAmcSlackStartDate()) {
				dto.setLackEndDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcSlackStartDate()));
			}

			if (null != liftAmcDtls.getAmcSlackEndDate()) {
				dto.setLackEndDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcSlackEndDate()));
			}

			dto.setLiftNumber(liftAmcDtls.getLiftCustomerMap().getLiftMaster()
					.getLiftNumber());
			dto.setCity(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap()
					.getCustomerMaster().getCity());
			dto.setArea(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap()
					.getCustomerMaster().getArea());
			Date tempStartDate = listOFAMCs.get(listOFAMCs.size() - 1)
					.getLiftCustomerMap().getLiftMaster().getAmcStartDate();
			Date tempEndDate = listOFAMCs.get(listOFAMCs.size() - 1)
					.getLiftCustomerMap().getLiftMaster().getAmcEndDate();
			Date tempDateOfInstallation = listOFAMCs.get(listOFAMCs.size() - 1)
					.getLiftCustomerMap().getLiftMaster()
					.getDateOfInstallation();
			dto.setStatus(this.calculateAMCStatus(tempStartDate, tempEndDate,
					tempDateOfInstallation).getStatusMsg());
			dto.setAmcAmount(liftAmcDtls.getLiftCustomerMap().getLiftMaster()
					.getAmcAmount());

			if (i > 0) {

				Integer diffInDays = DateUtils.daysBetween(listOFAMCs.get(i)
						.getAmcStartDate(), listOFAMCs.get(i - 1)
						.getAmcEndDate());
				if (diffInDays > 0) {
					Date slackStartDate = DateUtils.addDaysToDate(listOFAMCs
							.get(i - 1).getAmcEndDate(), 1);
					Date slackEndDate = DateUtils.addDaysToDate(
							listOFAMCs.get(i).getAmcStartDate(), -1);
					if (null != slackStartDate && null != slackEndDate) {
						{
							dto.setSlackStartDate(DateUtils
									.convertDateToStringWithoutTime(slackStartDate));
							dto.setSlackEndDate(DateUtils
									.convertDateToStringWithoutTime(slackEndDate));
							dto.setSlackperiod(diffInDays);
						}
					}
				}
			}
			if(liftAmcDtls.getAmcType()!=null){
				if (AMCType.COMPREHENSIVE.getId() == liftAmcDtls.getAmcType()) {
					dto.setAmcTypeStr(AMCType.COMPREHENSIVE.getType());
				} else if (AMCType.NON_COMPREHENSIVE.getId() == liftAmcDtls
						.getAmcType()) {
					dto.setAmcTypeStr(AMCType.NON_COMPREHENSIVE.getType());
				} else if (AMCType.ON_DEMAND.getId() == liftAmcDtls.getAmcType()) {
					dto.setAmcTypeStr(AMCType.ON_DEMAND.getType());
				} else if (AMCType.OTHER.getId() == liftAmcDtls.getAmcType()) {
					dto.setAmcTypeStr(AMCType.OTHER.getType());
				}
			}
			dto.setCompanyName(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			dto.setActiveFlag(liftAmcDtls.getActiveFlag());
			listOFDtos.add(dto);
			i++;
		}
		return listOFDtos;
	}

	private Status calculateAMCStatus(Date amcStartDate, Date amcEndDate,
			Date dateOfInstallation) {
		Status amcStatus = null;
		Date today = new Date();
		Date warrantyexpiryDate = DateUtils.addDaysToDate(dateOfInstallation,
				365);
		Date renewalDate = DateUtils.addDaysToDate(amcEndDate, -30);
		if (DateUtils.isBeforeOrEqualToDate(amcEndDate, warrantyexpiryDate)) {
			amcStatus = Status.UNDER_WARRANTY;
		} else if (DateUtils.isAfterOrEqualTo(renewalDate, today)
				&& DateUtils.isBeforeOrEqualToDate(today, amcEndDate)) {
			amcStatus = Status.RENEWAL_DUE;
		} else if (DateUtils.isAfterToDate(amcEndDate, today)) {
			amcStatus = Status.AMC_PENDING;
		} else if (DateUtils.isBeforeOrEqualToDate(amcStartDate, today)
				&& DateUtils.isAfterOrEqualTo(today, amcEndDate)) {
			amcStatus = Status.UNDER_AMC;
		}
		return amcStatus;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(
			Integer complaintId) {
		return complaintsDao.getComplTechMapObjByComplaintId(complaintId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsSiteVisitDtls> getAllVisitsForComnplaints(
			Integer complaintTechMapId) {
		return complaintsDao.getAllVisitsForComnplaints(complaintTechMapId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserRoleDtlsDTO> getListOfTechnicians(
			List<Integer> companyBranchMapIds) {
		List<UserRoleDtlsDTO> listOFUserAdtls = new ArrayList<UserRoleDtlsDTO>();
		List<RlmsUserRoles> listOfAllTechnicians = this.getListOfTechniciansBy(companyBranchMapIds);
		for (RlmsUserRoles rlmsUserRoles : listOfAllTechnicians) {
			UserRoleDtlsDTO dto = new UserRoleDtlsDTO();
			dto.setUserId(rlmsUserRoles.getRlmsUserMaster().getUserId());
			dto.setCompanyBranchMapId(rlmsUserRoles.getRlmsCompanyBranchMapDtls().getCompanyBranchMapId());
			dto.setCompanyName(rlmsUserRoles.getRlmsCompanyMaster().getCompanyName());
			dto.setCity(rlmsUserRoles.getRlmsUserMaster().getCity());
			dto.setName(rlmsUserRoles.getRlmsUserMaster().getFirstName() + " " + rlmsUserRoles.getRlmsUserMaster().getLastName());
			dto.setContactNumber(rlmsUserRoles.getRlmsUserMaster().getContactNumber());
			dto.setUserRoleId(rlmsUserRoles.getUserRoleId());
			dto.setActiveFlag(rlmsUserRoles.getActiveFlag());
	        listOFUserAdtls.add(dto);
		}
		return listOFUserAdtls;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TechnicianCount> getListOfTechniciansForBranch(
			List<Integer> companyBranchMapIds) {
		List<UserRoleDtlsDTO> listOFUserAdtls = new ArrayList<UserRoleDtlsDTO>();
		List<TechnicianCount> listOfTechniciansCount = this.getListOfTechniciansForBranchByCompanybranchMappingId(companyBranchMapIds);
	/*	for (RlmsUserRoles rlmsUserRoles : listOfAllTechnicians) {
			UserRoleDtlsDTO dto = new UserRoleDtlsDTO();
			dto.setUserId(rlmsUserRoles.getRlmsUserMaster().getUserId());
			dto.setCompanyBranchMapId(rlmsUserRoles.getRlmsCompanyBranchMapDtls().getCompanyBranchMapId());
			dto.setCompanyName(rlmsUserRoles.getRlmsCompanyMaster().getCompanyName());
			dto.setCity(rlmsUserRoles.getRlmsUserMaster().getCity());
			dto.setName(rlmsUserRoles.getRlmsUserMaster().getFirstName() + " " + rlmsUserRoles.getRlmsUserMaster().getLastName());
			dto.setContactNumber(rlmsUserRoles.getRlmsUserMaster().getContactNumber());
			dto.setUserRoleId(rlmsUserRoles.getUserRoleId());
			dto.setActiveFlag(rlmsUserRoles.getActiveFlag());
	        listOFUserAdtls.add(dto);
		}*/
		return listOfTechniciansCount;
	}
	public List<RlmsUserRoles> getListOfTechniciansBy(
			List<Integer> compBranchMapId) {
		List<RlmsUserRoles> listOfUserRoles = new ArrayList<RlmsUserRoles>();

		listOfUserRoles = this.dashboardDao.getAllUserWithRoleFor(compBranchMapId,
				SpocRoleConstants.TECHNICIAN.getSpocRoleId());
		List<RlmsUserRoles> listOfActiveUserRoles = new ArrayList<RlmsUserRoles>();
		
		return listOfUserRoles;
	}
	public List<TechnicianCount> getListOfTechniciansForBranchByCompanybranchMappingId(
		List<Integer> compBranchMapId) {
		List<Object[]>listOfObject = new ArrayList<Object[]>();
       List<TechnicianCount> technicianCountsList =new ArrayList<TechnicianCount>();
		listOfObject = this.dashboardDao.getTechnicianCountByCompanyBranchMap(compBranchMapId,
				SpocRoleConstants.TECHNICIAN.getSpocRoleId());
		if(listOfObject!=null && !listOfObject.isEmpty()) {
				for (Object[] objects : listOfObject) {
					TechnicianCount technicianCounts = new TechnicianCount();
					technicianCounts.setCount((BigInteger)objects[1]);
					List<Object[]> objectList = dashboardDao.getTechnicianActiveStatusCountByCompanyBranchMap((Integer)objects[0],SpocRoleConstants.TECHNICIAN.getSpocRoleId());
					for (Object[] obj : objectList) {
						if(objectList.size()<2) {
							if((Integer)obj[0]==1) {
							technicianCounts.setTotolActiveTechnician((BigInteger)obj[1]);
							}
							else {
								technicianCounts.setTotalInactiveTechnician((BigInteger)obj[1]);
							}
						}
						else {
							if((Integer)obj[0]==0) {
								technicianCounts.setTotalInactiveTechnician((BigInteger)obj[1]);
							}
							else {
								technicianCounts.setTotolActiveTechnician((BigInteger)obj[1]);
							}
						}
					}
					RlmsCompanyBranchMapDtls companyBranchMapDtls= dashboardDao.getCompanyBranchMapDtlsForDashboard((Integer)objects[0]);
					if(companyBranchMapDtls!=null) {
						technicianCounts.setBranchName(companyBranchMapDtls.getRlmsBranchMaster().getBranchName());
						technicianCounts.setCity(companyBranchMapDtls.getRlmsBranchMaster().getCity());
						technicianCountsList.add(technicianCounts);		
				  }
				}
		}
		return technicianCountsList;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AMCDetailsDto> getAllAMCDetails(List<Integer> liftCustomerMapId,AMCDetailsDto amcDetailsDto) {
		List<AMCDetailsDto> listOFAMCDetails = new ArrayList<AMCDetailsDto>();
		List<Integer> listOfLiftsForAMCDtls = new ArrayList<Integer>();
		List<RlmsLiftCustomerMap> listOFApplicableLifts = new ArrayList<RlmsLiftCustomerMap>();

		listOFApplicableLifts = this.liftDao.getAllLiftsByIds(liftCustomerMapId);
		for (RlmsLiftCustomerMap rlmsLiftCustomerMap : listOFApplicableLifts) {
			listOfLiftsForAMCDtls.add(rlmsLiftCustomerMap
					.getLiftCustomerMapId());
		}

		List<RlmsLiftAmcDtls> listOfAMCDtls = this.liftDao
				.getAllAMCDetils(listOfLiftsForAMCDtls, amcDetailsDto);
		Set<Integer> liftIds = new HashSet<Integer>();
		for (RlmsLiftAmcDtls liftAmcDtls : listOfAMCDtls) {
			liftIds.add(liftAmcDtls.getLiftCustomerMap().getLiftMaster()
					.getLiftId());
		}

		for (Integer liftId : liftIds) {
			List<RlmsLiftAmcDtls> listForLift = new ArrayList<RlmsLiftAmcDtls>(
					listOfAMCDtls);
			CollectionUtils.filter(listForLift, new LiftPredicate(liftId));
			listOFAMCDetails.addAll(this.constructListOFAMcDtos(listForLift));
		}

		return listOFAMCDetails;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsCompanyBranchMapDtls> getAllBranchesForDashBoard(Integer companyId){
		return this.dashboardDao.getAllBranchesForDashboard(companyId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<BranchDtlsDto> getListOfBranchDtlsForDashboard(Integer companyId, UserMetaInfo metaInfo){
		List<Integer> listOfAllApplicableCompanies = new ArrayList<Integer>();
		List<Integer> listOFApplicableBranches = new ArrayList<Integer>();
		listOfAllApplicableCompanies.add(companyId);
		 List<RlmsCompanyBranchMapDtls> listOfAllBranches = this.dashboardDao.getAllBranchDtlsForDashboard(listOfAllApplicableCompanies);
		  for (RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls : listOfAllBranches) {
			  listOFApplicableBranches.add(rlmsCompanyBranchMapDtls.getCompanyBranchMapId());
		  }
		  
		
		List<BranchDtlsDto> listOFBranchDtls = new ArrayList<BranchDtlsDto>();
		for (Integer companyBranchMapId : listOFApplicableBranches) {
			BranchDtlsDto branchDtlsDto = new BranchDtlsDto();
			RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls = this.dashboardDao.getCompanyBranchMapDtlsForDashboard(companyBranchMapId);
			branchDtlsDto.setId(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchId());
			branchDtlsDto.setBranchName(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchName());
			branchDtlsDto.setBranchAddress(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getBranchAddress());
			branchDtlsDto.setArea(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getArea());
			branchDtlsDto.setCity(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getCity());
			branchDtlsDto.setPinCode(rlmsCompanyBranchMapDtls.getRlmsBranchMaster().getPincode());
			branchDtlsDto.setCompanyName(rlmsCompanyBranchMapDtls.getRlmsCompanyMaster().getCompanyName());
			branchDtlsDto.setActiveFlag(rlmsCompanyBranchMapDtls.getActiveFlag());
		
			
			/*List<UserDtlsDto> listOfAllTech = this.getListOFAllTEchnicians(companyBranchMapId);
			branchDtlsDto.setListOfAllTechnicians(listOfAllTech);
			if(null != listOfAllTech && !listOfAllTech.isEmpty()){
				branchDtlsDto.setNumberOfTechnicians(listOfAllTech.size());
			}*/
			/*List<LiftDtlsDto> listOfAllLifts = this.getListOfAllLifts(companyBranchMapId);
			if(null != listOfAllLifts){
				branchDtlsDto.setListOfAllLifts(listOfAllLifts);
			}
			if(null != listOfAllLifts){
				branchDtlsDto.setNumberOfLifts(listOfAllLifts.size());
			}*/
			listOFBranchDtls.add(branchDtlsDto);			
		}
		return listOFBranchDtls;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<EventDtlsDto> getListOfEvetnDetails(List<Integer> listCustMap,
			UserMetaInfo metaInfo) {
		
		List<RlmsEventDtls> allEvent = this.dashboardDao.getAllEventDtlsForDashboard(listCustMap);
		               
		return 	 this.constructEventDetailsList(allEvent);	

	}
	
	
	 
/*	private RlmsEventDtls constructVisitDtls(RlmsEventDtls dto) throws ParseException{
		RlmsEventDtls eventDtls = new RlmsEventDtls();
		eventDtls.setEventType(dto.getEventType());
		eventDtls.setEventDescription(dto.getEventDescription());
	//	eventDtls.setLiftCustomerMapId(dto.getLiftCustomerMapId());
		eventDtls.setGeneratedDate(DateUtils.convertStringToDateWithTime(dto.getGeneratedDateStr()));
		eventDtls.setGeneratedBy(dto.getGeneratedBy());
		eventDtls.setUpdatedDate(DateUtils.convertStringToDateWithTime(dto.getUpdatedDateStr()));
		eventDtls.setUpdatedBy(dto.getUpdatedBy());
		eventDtls.setActiveFlag(dto.getActiveFlag());
		return eventDtls;
	}
	*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveEventDtls(RlmsEventDtls eventDtls){
		this.dashboardDao.saveEventDtls(eventDtls);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getListOfAmcCallsBy(ComplaintsDtlsDto dto) {
		List<ComplaintsDto> listOfAllComplaints = new ArrayList<ComplaintsDto>();
		List<RlmsComplaintMaster> listOfComplaints = this.dashboardDao
				.getAllComplaintsForGivenCriteria(dto.getBranchCompanyMapId(),
						dto.getBranchCustomerMapId(),
						dto.getListOfLiftCustoMapId(), dto.getStatusList(),
						dto.getFromDate(), dto.getToDate(),1);
		for (RlmsComplaintMaster rlmsComplaintMaster : listOfComplaints) {
			ComplaintsDto complaintsDto = this
					.constructComplaintDto(rlmsComplaintMaster);
			listOfAllComplaints.add(complaintsDto);
		}

		return listOfAllComplaints;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<EventDtlsDto> getListOfEventsByType(RlmsEventDtls rlmsEventDtls) {
		List<EventDtlsDto>eventDetailsList = new ArrayList<EventDtlsDto>();
		if(rlmsEventDtls!=null ) {
			if(rlmsEventDtls.getEventType()!=null) {
				List<RlmsEventDtls> rlmsEventDtlsList = dashboardDao.getListOfEventsByType(rlmsEventDtls);
					if(rlmsEventDtlsList!=null && !rlmsEventDtlsList.isEmpty()) {
						eventDetailsList = this.constructEventDetailsList(rlmsEventDtlsList);	
					}
					else {
						logger.debug("event data not found");
					}
			}
			else {
				logger.debug("invalid event type");
			}
		}
      else{
    		   logger.debug("invalid getListOfEventsByType request");
      }
		return eventDetailsList;
	}
	public List<EventDtlsDto> constructEventDetailsList(List<RlmsEventDtls> rlmsEventDtlList){
		 List<EventDtlsDto> eventDtlsDtoList = new ArrayList<>();
		for (RlmsEventDtls rlmsEventDtls : rlmsEventDtlList) {
			      EventDtlsDto eventDtlsDto = new EventDtlsDto();
			      eventDtlsDto.setEventId(rlmsEventDtls.getEventId());
			      eventDtlsDto.setEventService(rlmsEventDtls.getEventService());
			      eventDtlsDto.setImei(rlmsEventDtls.getImeiId());
			      eventDtlsDto.setEventType(rlmsEventDtls.getEventType());
			      eventDtlsDto.setEventCode(rlmsEventDtls.getEventCode());
			      eventDtlsDto.setEventDescription(rlmsEventDtls.getEventDescription());
			      eventDtlsDto.setFloorNo(rlmsEventDtls.getFloorNo());
			      
			   //  convert epoch into date format
			        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			        format.setTimeZone(TimeZone.getTimeZone("IST"));
			        String eventDate = format.format(rlmsEventDtls.getEventDate());
			      		      
			      eventDtlsDto.setDate(eventDate); 
			      eventDtlsDto.setLiftCustomerMap(rlmsEventDtls.getRlmsLiftCustomerMap());
			      eventDtlsDto.setActiveFlag(rlmsEventDtls.getActiveFlag());
			      eventDtlsDto.setEventFromContactNo(rlmsEventDtls.getFromContact());
			      eventDtlsDtoList.add(eventDtlsDto);
		}
		return eventDtlsDtoList;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<EventCountDtls> getEventCountDetails(List<Integer> listCustMap,
			UserMetaInfo metaInfo) {
		Set<Integer> uniqueIdsForLiftCustMapId = new  HashSet<Integer>();
		List<EventCountDtls> eventDtlsDtosList = new ArrayList<EventCountDtls>();
		RlmsLiftCustomerMap liftCustomerMap = new RlmsLiftCustomerMap();
		List<Object[]> EventCount = this.dashboardDao.getEventCountDtlsForDashboard(listCustMap);
		               for (Object[] obj : EventCount) {
		            	   EventCountDtls eventCountDtls = new EventCountDtls();
		            	   if(!uniqueIdsForLiftCustMapId.contains(obj[0])) {
		            	   	   liftCustomerMap = liftDao.getLiftCustomerMapById((Integer)obj[0]);
		            	   }
		            	   eventCountDtls.setBranchName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
		            	   eventCountDtls.setCity(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getCity());
		            	   eventCountDtls.setCustomerName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
		            	   eventCountDtls.setLiftNumber(liftCustomerMap.getLiftMaster().getLiftNumber());
		            	if(obj[1].equals("LMS EVENT")) {
		            		eventCountDtls.setTotolEventCount((BigInteger)obj[2]);
		            		}
		            	else if(obj[1].equals("LMS ERROR")) {
		            		eventCountDtls.setTotalErrorCount((BigInteger)obj[2]);
		            	}
		            	else if(obj[1].equals("LMS RES")) {
		            	   eventCountDtls.setTotalResCount((BigInteger)obj[2]);
		            	}
		            	 uniqueIdsForLiftCustMapId.add((Integer)obj[0]);
			             eventDtlsDtosList.add(eventCountDtls);

					}
		return 	 eventDtlsDtosList;

	}
	
}
