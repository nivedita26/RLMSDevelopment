package com.rlms.service;

import java.text.ParseException;
import java.util.List;

import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.SiteVisitDtlsDto;
import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserDtlsDto;
//import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserMetaInfo;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;

public interface ComplaintsService {
	public List<ComplaintsDto> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
	public String validateAndRegisterNewComplaint(ComplaintsDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException;
	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto);
	public String assignComplaint(ComplaintsDto complaintsDto, UserMetaInfo metaInfo) throws ValidationException;
	public List<LiftDtlsDto> getAllLiftsForBranchsOrCustomer(LiftDtlsDto dto);
	public List<UserAppDtls> getRegIdsOfAllApplicableUsers(Integer complaintId);
	public List<ComplaintsDto> getAllComplaintsByMember(Integer memberId,Integer serviceCall);
	public List<UserRoleDtlsDTO> getAllTechniciansToAssignComplaint(ComplaintsDtlsDto complaintsDtlsDto);
	public String updateComplaintStatus(ComplaintsDto dto);
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(ComplaintsDtlsDto dto);
	public String validateAndSaveSiteVisitDtls(SiteVisitDtlsDto dto) throws ValidationException, ParseException;
	public List<SiteVisitDtlsDto> getAllVisitsForComplaint(SiteVisitDtlsDto siteVisitDtlsDto);
	public UserDtlsDto getTechnicianDtls(Integer complaintId);
	public String validateAndUpdateComplaint(ComplaintsDto dto, UserMetaInfo metaInfo) throws ValidationException, ParseException;
	public String deleteComplaint(ComplaintsDto dto);
}
