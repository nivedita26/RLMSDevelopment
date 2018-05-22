package com.rlms.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.AMCType;
import com.rlms.constants.PhotoType;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.SpocRoleConstants;
import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.BranchDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.DashboardDao;
import com.rlms.dao.FyaDao;
import com.rlms.dao.LiftDao;
import com.rlms.dao.UserRoleDao;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsFyaTranDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;
import com.rlms.model.RlmsUserRoles;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;

@Service("LiftService")
public class LiftServiceImpl implements LiftService{

	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private BranchDao branchDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private FyaDao fyaDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ReportService reportService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsLiftCustomerMap> getAllLiftsForBranch(Integer companyBranchMapId){
		List<RlmsLiftCustomerMap> liftsForBranch = new ArrayList<RlmsLiftCustomerMap>();
		List<Integer> listOfAllCustmers = new ArrayList<Integer>();
		List<RlmsBranchCustomerMap> listOfCustomersOfBranch = this.companyService.getAllCustomersOfBranch(companyBranchMapId);
		for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOfCustomersOfBranch) {
			listOfAllCustmers.add(rlmsBranchCustomerMap.getCustomerMaster().getCustomerId());
		}
		if(null != listOfAllCustmers && !listOfAllCustmers.isEmpty()){
			liftsForBranch =  this.liftDao.getAllLiftsForCustomers(listOfAllCustmers);
		}
		return liftsForBranch;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndAddNewLiftDtls(LiftDtlsDto dto, UserMetaInfo metaInfo) throws ParseException{
		RlmsLiftMaster liftM = this.constructLiftMaster(dto, metaInfo);
		Integer liftId = this.liftDao.saveLiftM(liftM);
		liftM.setLiftId(liftId);
		
		
		RlmsLiftCustomerMap liftCustomerMap = this.constructLiftCustomerMap(liftM, dto, metaInfo);
		Integer liftCustomerMapID = this.liftDao.saveLiftCustomerMap(liftCustomerMap);
		liftCustomerMap.setLiftCustomerMapId(liftCustomerMapID);
		
		AMCDetailsDto amcDetailsDto = this.constructAMCDtlsDto(liftCustomerMap);
		this.reportService.addAMCDetailsForLift(amcDetailsDto, metaInfo);
		
		RlmsFyaTranDtls fyaTranDtls = this.constructFyaTranDtls(liftCustomerMap, metaInfo);
		this.fyaDao.saveFyaTranDtls(fyaTranDtls);
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.LIFT_ADDED_SUCCESSFULLY.getMessage());
		
	}
	
	private AMCDetailsDto constructAMCDtlsDto(RlmsLiftCustomerMap liftCustomerMap){
		AMCDetailsDto dto = new AMCDetailsDto();
		dto.setAmcAmount(liftCustomerMap.getLiftMaster().getAmcAmount());
		dto.setAmcStartDate(DateUtils.convertDateToStringWithoutTime(liftCustomerMap.getLiftMaster().getAmcStartDate()));
		dto.setAmcEndDate(DateUtils.convertDateToStringWithoutTime(liftCustomerMap.getLiftMaster().getAmcEndDate()));
		Date amcDueDate = DateUtils.addDaysToDate(liftCustomerMap.getLiftMaster().getAmcEndDate(), -30);
		dto.setAmcDueDate(amcDueDate);
		dto.setLiftCustoMapId(liftCustomerMap.getLiftCustomerMapId());
		dto.setLiftNumber(liftCustomerMap.getLiftMaster().getLiftNumber());
		dto.setAmcType(liftCustomerMap.getLiftMaster().getAmcType());
		return dto;
		
	}
	private RlmsFyaTranDtls constructFyaTranDtls(RlmsLiftCustomerMap liftCustomerMap, UserMetaInfo metaInfo){
		RlmsFyaTranDtls fyaTranDtls = new RlmsFyaTranDtls();
		RlmsUserRoles userRole = this.userRoleDao.getUserRoleForCompany(RLMSConstants.INDITECH.getId(), SpocRoleConstants.INDITECH_ADMIN.getSpocRoleId());
		
		fyaTranDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		fyaTranDtls.setFyaType(RLMSConstants.LIFT_TYPE.getId());
		fyaTranDtls.setInitiatedBy(metaInfo.getUserId());
		fyaTranDtls.setLiftCustomerMap(liftCustomerMap);
		fyaTranDtls.setPendingWith(userRole.getUserRoleId());
		fyaTranDtls.setStatus(Status.PENDING.getStatusId());
		fyaTranDtls.setCreatedBy(metaInfo.getUserId());
		fyaTranDtls.setCreatedDate(new Date());
		fyaTranDtls.setUpdatedDate(new Date());
		fyaTranDtls.setUdpatedBy(metaInfo.getUserId());
		return fyaTranDtls;
	}
	private RlmsLiftCustomerMap constructLiftCustomerMap(RlmsLiftMaster liftM, LiftDtlsDto dto, UserMetaInfo metaInfo){
		RlmsLiftCustomerMap liftCustomerMap =  new RlmsLiftCustomerMap();
		RlmsBranchCustomerMap branchCustomerMap = this.branchDao.getBranchCustomerMapDtls(dto.getBranchCustomerMapId());
		
		liftCustomerMap.setActiveFlag(RLMSConstants.ACTIVE.getId());
		liftCustomerMap.setBranchCustomerMap(branchCustomerMap);
		liftCustomerMap.setLiftMaster(liftM);
		liftCustomerMap.setUpdatedBy(metaInfo.getUserId());
		liftCustomerMap.setUpdatedDate(new Date());
		liftCustomerMap.setCreatedBy(metaInfo.getUserId());
		liftCustomerMap.setCreatedDate(new Date());
		return liftCustomerMap;	

	}
	
	private RlmsLiftMaster constructLiftMaster(LiftDtlsDto dto, UserMetaInfo metaInfo){
		RlmsLiftMaster liftM = new RlmsLiftMaster();
		liftM.setAccessControl(dto.getAccessControl());
		liftM.setActiveFlag(RLMSConstants.ACTIVE.getId());
		liftM.setAddress(dto.getAddress());
		liftM.setCity(dto.getCity());
		liftM.setArea(dto.getArea());
		liftM.setPincode(dto.getPinCode());
		liftM.setAlarm(dto.getAlarm());
		liftM.setAlarmBattery(dto.getAlarmBattery());
		liftM.setAmcAmount(dto.getAmcAmount());
		liftM.setAmcStartDate(dto.getAmcStartDate());
		liftM.setAmcType(dto.getAmcType());
		liftM.setARD(dto.getArd());
		liftM.setARDPhoto(dto.getArdPhoto());
		liftM.setAutoDoorHeaderPhoto(dto.getAutoDoorHeaderPhoto());
		liftM.setAutoDoorMake(dto.getAutoDoorMake());
		liftM.setBatteryCapacity(dto.getBatteryCapacity());
		liftM.setBatteryMake(dto.getBatteryMake());
		liftM.setBreakVoltage(dto.getBreakVoltage());
		liftM.setCartopPhoto(dto.getCartopPhoto());
		liftM.setCollectiveType(dto.getCollectiveType());
		liftM.setCOPMake(dto.getCopMake());
		liftM.setCOPPhoto(dto.getCopPhoto());		
		liftM.setDateOfInstallation(dto.getDateOfInstallation());
		liftM.setDoorType(dto.getDoorType());
		liftM.setEngineType(dto.getEngineType());
		liftM.setFireMode(dto.getFireMode());
		liftM.setIntercomm(dto.getIntercomm());
		liftM.setLatitude(dto.getLatitude());
		liftM.setLiftNumber(dto.getLiftNumber());
		liftM.setLobbyPhoto(dto.getLobbyPhoto());
		liftM.setLongitude(dto.getLongitude());
		liftM.setLOPMake(dto.getLopMake());
		liftM.setLOPPhoto(dto.getLopPhoto());
		liftM.setMachineCapacity(dto.getMachineCapacity());
		liftM.setMachineCurrent(dto.getMachineCurrent());
		liftM.setMachineMake(dto.getMachineMake());
		liftM.setMachinePhoto(dto.getMachinePhoto());
		liftM.setNoOfBatteries(dto.getNoOfBatteries());
		liftM.setNoOfStops(dto.getNoOfStops());
		liftM.setPanelMake(dto.getPanelMake());
		liftM.setPanelPhoto(dto.getPanelPhoto());
		liftM.setServiceEndDate(dto.getServiceEndDate());
		liftM.setServiceStartDate(dto.getServiceStartDate());
		liftM.setSimplexDuplex(dto.getSimplexDuplex());
		liftM.setUpdatedBy(metaInfo.getUserId());		
		liftM.setWiringShceme(dto.getWiringShceme());
		liftM.setStatus(Status.PENDING_FOR_APPROVAL.getStatusId());
		liftM.setLiftType(dto.getLiftType());
		liftM.setUpdatedDate(new Date());
		liftM.setWiringPhoto(dto.getWiringPhoto());
		liftM.setCreatedBy(metaInfo.getUserId());
		liftM.setCreatedDate(new Date());
		liftM.setAmcEndDate(dto.getAmcEndDate());
		liftM.setAmcType(dto.getAmcType());
		liftM.setImei(dto.getImei());
		liftM.setLmsEventContactNumber(dto.getLmsEventFromContactNo());
		
		return liftM;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getLiftsToBeApproved(){
		List<RlmsLiftCustomerMap> listOfAllPendingLifts = this.liftDao.getAllLiftsToBeApproved();
		List<LiftDtlsDto> listOfDtos = new ArrayList<LiftDtlsDto>();
		for (RlmsLiftCustomerMap liftCustomerMap : listOfAllPendingLifts) {
			LiftDtlsDto dto = new LiftDtlsDto();
			RlmsFyaTranDtls fyaTranDtls = this.fyaDao.getFyaForLift(liftCustomerMap.getLiftCustomerMapId(), Status.PENDING.getStatusId());
			dto.setLiftNumber(liftCustomerMap.getLiftMaster().getLiftNumber());
			dto.setAddress(liftCustomerMap.getLiftMaster().getAddress());
			dto.setCustomerName(liftCustomerMap.getBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setBranchName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			dto.setLiftId(liftCustomerMap.getLiftMaster().getLiftId());
			dto.setLiftCustomerMapId(liftCustomerMap.getLiftCustomerMapId());
			dto.setFyaTranId(fyaTranDtls.getFyaTranId());
			dto.setCompanyName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			listOfDtos.add(dto);
		}
		
		return listOfDtos;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String approveLift(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo){
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapByLiftId(liftDtlsDto.getLiftId());
		
		liftCustomerMap.getLiftMaster().setActiveFlag(RLMSConstants.ACTIVE.getId());
		liftCustomerMap.getLiftMaster().setServiceStartDate(new Date());
		liftCustomerMap.setActiveFlag(RLMSConstants.ACTIVE.getId());
		this.liftDao.updateLiftCustomerMap(liftCustomerMap);
		
		RlmsFyaTranDtls fyaTranDtls = this.fyaDao.getFyaByFyaTranIDt(liftDtlsDto.getFyaTranId());
		fyaTranDtls.setStatus(Status.COMPLETED.getStatusId());
		this.fyaDao.updateFyaTranDtls(fyaTranDtls);
		
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.LIFT_APPROVED.getMessage());
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getLiftDetailsForBranch(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo){
		List<RlmsLiftCustomerMap> listOFAllLifts = this.liftDao.getAllLiftsForBranchs(liftDtlsDto.getBranchCompanyMapId());
		List<LiftDtlsDto> listOfAllDtos = new ArrayList<LiftDtlsDto>();
		for (RlmsLiftCustomerMap liftCustomerMap : listOFAllLifts) {
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
			listOfAllDtos.add(dto);
		}
		
		return listOfAllDtos;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String uploadPhoto(LiftDtlsDto dto){
		
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapById(dto.getLiftCustomerMapId());
		RlmsLiftMaster liftMaster = liftCustomerMap.getLiftMaster();
		if(PhotoType.MACHINE_PHOTO.getId() == dto.getPhotoType()){
			liftMaster.setMachinePhoto(dto.getMachinePhoto());
		}else if(PhotoType.ARD_PHOTO.getId() == dto.getPhotoType()){
			liftMaster.setARDPhoto(dto.getArdPhoto());
		}else if(PhotoType.AUTO_DOOR_HEADER_PHOTO.getId() == dto.getPhotoType()){
			liftMaster.setAutoDoorHeaderPhoto(dto.getAutoDoorHeaderPhoto());
		}else if(PhotoType.CARTOP_PHOTO.getId() == dto.getPhotoType()){
			
			liftMaster.setCartopPhoto(dto.getCartopPhoto());
		}else if(PhotoType.COP_PHOTO.getId() == dto.getPhotoType()){		
			
			liftMaster.setCOPPhoto(dto.getCopPhoto());
		}else if(PhotoType.LOBBY_PHOTO.getId() == dto.getPhotoType()){
			
			liftMaster.setLobbyPhoto(dto.getLobbyPhoto());
		}else if(PhotoType.LOP_PHOTO.getId() == dto.getPhotoType()){
			
			liftMaster.setLOPPhoto(dto.getLopPhoto());
		}else if(PhotoType.PANEL_PHOTO.getId() == dto.getPhotoType()){
			
			liftMaster.setPanelPhoto(dto.getPanelPhoto());
		}else if(PhotoType.WIRING_PHOTO.getId() == dto.getPhotoType()){
			
			liftMaster.setWiringPhoto(dto.getWiringPhoto());
			
		}
		
		this.liftDao.mergeLiftM(liftMaster);
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.PHOTO_UPDATED.getMessage());	
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public LiftDtlsDto getLiftMasterForType(LiftDtlsDto loftDtlsDto){
		LiftDtlsDto dto = new LiftDtlsDto();
		RlmsBranchCustomerMap branchCustomerMap = this.branchDao.getBranchCustomerMapDtls(loftDtlsDto.getBranchCustomerMapId());
		if(null != branchCustomerMap){
			dto.setAddress(branchCustomerMap.getCustomerMaster().getAddress());
			dto.setArea(branchCustomerMap.getCustomerMaster().getArea());
			dto.setPinCode(branchCustomerMap.getCustomerMaster().getPincode());
			dto.setCity(branchCustomerMap.getCustomerMaster().getCity());
		}
		RlmsLiftCustomerMap luftCustomerMap =  this.liftDao.getLiftMasterForType(loftDtlsDto.getBranchCustomerMapId(), loftDtlsDto.getLiftType());
		if(null != luftCustomerMap){
			dto.setBlank(false);
			dto.setAccessControl(luftCustomerMap.getLiftMaster().getAccessControl());
			
			dto.setAlarm(luftCustomerMap.getLiftMaster().getAlarm());
			dto.setAlarmBattery(luftCustomerMap.getLiftMaster().getAlarmBattery());
			dto.setAmcAmount(luftCustomerMap.getLiftMaster().getAmcAmount());
			dto.setAmcStartDate(luftCustomerMap.getLiftMaster().getAmcStartDate());
			dto.setDoorType(luftCustomerMap.getLiftMaster().getDoorType());
			dto.setNoOfStops(luftCustomerMap.getLiftMaster().getNoOfStops());
			dto.setEngineType(luftCustomerMap.getLiftMaster().getEngineType());
			dto.setMachineMake(luftCustomerMap.getLiftMaster().getMachineMake());
			dto.setMachineCapacity(luftCustomerMap.getLiftMaster().getMachineCapacity());
			dto.setMachineCurrent(luftCustomerMap.getLiftMaster().getMachineCurrent());
			dto.setMachinePhoto(luftCustomerMap.getLiftMaster().getMachinePhoto());
			dto.setBreakVoltage(luftCustomerMap.getLiftMaster().getBreakVoltage());
			dto.setPanelMake(luftCustomerMap.getLiftMaster().getPanelMake());
			dto.setPanelPhoto(luftCustomerMap.getLiftMaster().getPanelPhoto());
			dto.setNoOfBatteries(luftCustomerMap.getLiftMaster().getNoOfBatteries());
			dto.setCopMake(luftCustomerMap.getLiftMaster().getCOPMake());
			dto.setCopPhoto(luftCustomerMap.getLiftMaster().getCOPPhoto());
			dto.setLopMake(luftCustomerMap.getLiftMaster().getLOPMake());
			dto.setLopPhoto(luftCustomerMap.getLiftMaster().getLOPPhoto());
			dto.setCollectiveType(luftCustomerMap.getLiftMaster().getCollectiveType());
			dto.setSimplexDuplex(luftCustomerMap.getLiftMaster().getSimplexDuplex());
			dto.setCartopPhoto(luftCustomerMap.getLiftMaster().getCartopPhoto());
			dto.setAutoDoorMake(luftCustomerMap.getLiftMaster().getAutoDoorMake());
			dto.setWiringPhoto(luftCustomerMap.getLiftMaster().getWiringPhoto());
			dto.setWiringShceme(luftCustomerMap.getLiftMaster().getWiringShceme());
			dto.setFireMode(luftCustomerMap.getLiftMaster().getFireMode());
			dto.setIntercomm(luftCustomerMap.getLiftMaster().getIntercomm());
			dto.setAlarm(luftCustomerMap.getLiftMaster().getAlarm());
			dto.setAlarmBattery(luftCustomerMap.getLiftMaster().getAlarmBattery());
			dto.setAccessControl(luftCustomerMap.getLiftMaster().getAccessControl());
			dto.setLobbyPhoto(luftCustomerMap.getLiftMaster().getLobbyPhoto());
			if(null != luftCustomerMap.getLiftMaster().getAmcStartDate()){
				dto.setAmcStartDateStr(DateUtils.convertDateToStringWithoutTime(luftCustomerMap.getLiftMaster().getAmcStartDate()));
			}
			dto.setAmcType(luftCustomerMap.getLiftMaster().getAmcType());
			dto.setArd(luftCustomerMap.getLiftMaster().getARD());
			dto.setArdPhoto(luftCustomerMap.getLiftMaster().getARDPhoto());
			dto.setAutoDoorHeaderPhoto(luftCustomerMap.getLiftMaster().getAutoDoorHeaderPhoto());
			dto.setBatteryCapacity(luftCustomerMap.getLiftMaster().getBatteryCapacity());
			dto.setBatteryMake(luftCustomerMap.getLiftMaster().getBatteryMake());
			dto.setDateOfInstallation(luftCustomerMap.getLiftMaster().getDateOfInstallation());
			if(null != luftCustomerMap.getLiftMaster().getDateOfInstallation()){
				dto.setDateOfInstallationStr(DateUtils.convertDateToStringWithoutTime(luftCustomerMap.getLiftMaster().getDateOfInstallation()));
			}
			dto.setLiftNumber(luftCustomerMap.getLiftMaster().getLiftNumber());
			dto.setServiceStartDate(luftCustomerMap.getLiftMaster().getServiceStartDate());
			if(null != luftCustomerMap.getLiftMaster().getServiceStartDate()){
				dto.setServiceStartDateStr(DateUtils.convertDateToStringWithoutTime(luftCustomerMap.getLiftMaster().getServiceStartDate()));
			}
			dto.setServiceEndDate(luftCustomerMap.getLiftMaster().getServiceEndDate());
			if(null != luftCustomerMap.getLiftMaster().getServiceEndDate()){
				dto.setServiceEndDateStr(DateUtils.convertDateToStringWithoutTime(luftCustomerMap.getLiftMaster().getServiceEndDate()));
			}
		}else{
			dto.setBlank(true);
		}
		
		return dto;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerDtlsDto getAddressDetailsOfLift(Integer branchCustoMapId){
		CustomerDtlsDto dto = new CustomerDtlsDto();
		RlmsBranchCustomerMap branchCustomerMap = this.branchDao.getBranchCustomerMapDtls(branchCustoMapId);
		if(null != branchCustomerMap){
			dto.setAddress(branchCustomerMap.getCustomerMaster().getAddress());
			dto.setArea(branchCustomerMap.getCustomerMaster().getArea());
			dto.setCity(branchCustomerMap.getCustomerMaster().getCity());
			dto.setPinCode(branchCustomerMap.getCustomerMaster().getPincode());
		}
		return dto;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String updateLiftDetails(LiftDtlsDto dto, UserMetaInfo userMetaInfo){
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapById(dto.getLiftCustomerMapId());
		RlmsLiftMaster liftMaster = liftCustomerMap.getLiftMaster();
		if(!StringUtils.isEmpty(dto.getArea())){
			liftMaster.setArea(dto.getArea());			
		}if(!StringUtils.isEmpty(dto.getCity())){
			liftMaster.setCity(dto.getCity());
		}if(dto.getPinCode()!=null){
			liftMaster.setPincode(dto.getPinCode());
		}
		if(!StringUtils.isEmpty(dto.getAmcAmount())){
			liftMaster.setAmcAmount(dto.getAmcAmount());
		}
		if(!StringUtils.isEmpty(dto.getLatitude())){
			liftMaster.setLatitude(dto.getLatitude());
		}
		
		if(!StringUtils.isEmpty(dto.getLongitude())){
			liftMaster.setLongitude(dto.getLongitude());
		}
		if(dto.getDoorType()!=null){
			liftMaster.setDoorType(dto.getDoorType());
		}
		if(!StringUtils.isEmpty(dto.getNoOfStops())){
			liftMaster.setNoOfStops(dto.getNoOfStops());
		}
		if(!StringUtils.isEmpty(dto.getMachineCapacity())){
			liftMaster.setMachineCapacity(dto.getMachineCapacity());
		}
		if(!StringUtils.isEmpty(dto.getMachineCurrent())){
			liftMaster.setMachineCurrent(dto.getMachineCurrent());
		}
		if(!StringUtils.isEmpty(dto.getMachineMake())){
			liftMaster.setMachineMake(dto.getMachineMake());
		}
		if(!StringUtils.isEmpty(dto.getBreakVoltage())){
			liftMaster.setBreakVoltage(dto.getBreakVoltage());
		}
		if(!StringUtils.isEmpty(dto.getPanelMake())){
			liftMaster.setPanelMake(dto.getPanelMake());
		}
		if(!StringUtils.isEmpty(dto.getArd())){
			liftMaster.setARD(dto.getArd());
		}
		if(!StringUtils.isEmpty(dto.getCopMake())){
			liftMaster.setCOPMake(dto.getCopMake());
		}
		if(!StringUtils.isEmpty(dto.getLopMake())){
			liftMaster.setLOPMake(dto.getLopMake());
		}
		this.liftDao.mergeLiftM(liftMaster);
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.PHOTO_UPDATED.getMessage());	
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getLiftStatusForBranch(List<Integer> companyBranchIds, UserMetaInfo metaInfo){
		List<RlmsLiftCustomerMap> listOFAllLifts = this.liftDao.getAllLiftsStatusForBranchs(companyBranchIds);
		List<LiftDtlsDto> listOfAllDtos = new ArrayList<LiftDtlsDto>();
		Set<RlmsBranchCustomerMap> branchCustomerMapIdList = new HashSet<RlmsBranchCustomerMap>();
		for (RlmsLiftCustomerMap liftCustomerMap : listOFAllLifts) {
			RlmsLiftMaster liftM = liftCustomerMap.getLiftMaster();
			LiftDtlsDto dto = new LiftDtlsDto();
			dto.setActiveFlag(liftM.getActiveFlag());
			dto.setAccessControl(liftM.getAccessControl());
			dto.setAddress(liftM.getAddress());
			dto.setArea(liftM.getArea());
			dto.setPinCode(liftM.getPincode());
			dto.setCity(liftM.getCity());
			dto.setAlarm(liftM.getAlarm());
			dto.setAlarmBattery(liftM.getAlarmBattery());
			dto.setAmcAmount(liftM.getAmcAmount());
			dto.setAmcStartDate(liftM.getAmcStartDate());
			if(null != liftM.getAmcStartDate()){
				dto.setAmcStartDateStr(DateUtils.convertDateToStringWithoutTime(liftM.getAmcStartDate()));
			}
			dto.setAmcType(liftM.getAmcType());
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
			dto.setCompanyName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			dto.setLiftId(liftM.getLiftId());
			listOfAllDtos.add(dto);
		}
		return listOfAllDtos;
		
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getLiftCountForBranch(List<Integer> companyBranchIds, UserMetaInfo metaInfo){
		List<RlmsLiftCustomerMap> listOFAllLifts = this.liftDao.getAllLiftsStatusForBranchs(companyBranchIds);
		List<LiftDtlsDto> listOfAllDtos = new ArrayList<LiftDtlsDto>();
		Set<RlmsBranchCustomerMap> branchCustomerMapIdList = new HashSet<RlmsBranchCustomerMap>();
		for (RlmsLiftCustomerMap liftCustomerMap : listOFAllLifts) {
			//new code
			branchCustomerMapIdList.add(liftCustomerMap.getBranchCustomerMap());
		}
		for (RlmsBranchCustomerMap rlmsBranchCustomerMap : branchCustomerMapIdList) {
			   List<Object []> liftCountObj =  liftDao.liftCountByBranchCustomerMapId(rlmsBranchCustomerMap.getBranchCustoMapId());
		
		            for (Object[] objects : liftCountObj) {
		            	//RlmsLiftMaster liftMaster = new RlmsLiftMaster();
		    			LiftDtlsDto dto = new LiftDtlsDto();

		            	RlmsBranchCustomerMap  branchCustomerMap = new RlmsBranchCustomerMap();
		            	BigInteger liftCount =  (BigInteger) objects[1];
		            	
		            	branchCustomerMap = liftDao.getBranchCustomerMapByBranchCustomerMapId((int)objects[0]);
		            	dto.setBranchName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
		            	dto.setCustomerName(branchCustomerMap.getCustomerMaster().getCustomerName());
		            	dto.setCity(branchCustomerMap.getCustomerMaster().getCity());
		            	dto.setTotalLiftCountForCustomer(liftCount);
     	            	listOfAllDtos.add(dto);
                         }
			}
        return listOfAllDtos;
		
	}
	
}
