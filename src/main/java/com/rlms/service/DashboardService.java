package com.rlms.service;

import java.util.List;

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
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsSiteVisitDtls;

public interface DashboardService {

	public List<AMCDetailsDto> getAMCDetailsForDashboard(
			List<Integer> listOfApplicableBranchIds, AMCDetailsDto amcDetailsDto);

	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto);

	public List<CustomerDtlsDto> getAllCustomersForBranch(
			List<Integer> listOfApplicableBranchIds);
	

	public List<RlmsLiftCustomerMap> getAllLiftsForBranchsOrCustomer(
			LiftDtlsDto dto);

	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(
			Integer complaintId);

	public List<RlmsSiteVisitDtls> getAllVisitsForComnplaints(
			Integer complaintTechMapId);

	public List<UserRoleDtlsDTO> getListOfTechnicians(
			List<Integer> companyBranchMapIds);
	
	public List<AMCDetailsDto> getAllAMCDetails(
			List<Integer> listOfApplicableBranchIds, AMCDetailsDto amcDetailsDto);
	
	public List<RlmsCompanyBranchMapDtls> getAllBranchesForDashBoard(Integer companyId);
	
	public List<BranchDtlsDto> getListOfBranchDtlsForDashboard(Integer companyId, UserMetaInfo metaInfo);

	public List<EventDtlsDto> getListOfEvetnDetails(List<Integer> companyBranchIds,
			UserMetaInfo metaInfo);
	
	public List<ComplaintsDto> getListOfAmcCallsBy(ComplaintsDtlsDto dto);
	
    public List<EventDtlsDto> getListOfEventsByType(RlmsEventDtls rlmsEventDtls);
    
    public List<TechnicianCount> getListOfTechniciansForBranch(
			List<Integer> companyBranchMapIds) ;
    
    public List<EventCountDtls> getEventCountDetails(List<Integer> companyBranchIds,
			UserMetaInfo metaInfo);

}
	

