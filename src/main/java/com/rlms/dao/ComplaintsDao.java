package com.rlms.dao;

import java.util.Date;
import java.util.List;

import com.rlms.contract.SiteVisitReportDto;
import com.rlms.contract.TechnicianWiseReportDTO;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsSiteVisitDtls;

public interface ComplaintsDao {
	public List<RlmsComplaintTechMapDtls> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
	public List<RlmsComplaintMaster> getAllComplaintsForBranchOrCustomer(Integer branchCompanyMapId, Integer branchCustomerMapId, List<Integer> statusList,Integer callType);
	public Integer saveComplaintM(RlmsComplaintMaster complaintMaster);
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(Integer branchCompanyMapId, Integer branchCustomerMapId,List<Integer> listOfLioftIds,  List<Integer> statusList,  Date fromDate, Date toDate, Integer callType);
	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(Integer complaintId);
	public RlmsComplaintMaster getComplaintMasterObj(Integer complaintId,Integer callType);
	public void saveComplaintTechMapDtls(RlmsComplaintTechMapDtls complaintTechMapDtls);
	public List<RlmsComplaintMaster> getAllComplaintsByMemberId(Integer memberId,Integer callType);
	public void mergeComplaintM(RlmsComplaintMaster complaintMaster);
	public List<RlmsComplaintTechMapDtls> getListOfComplaintDtlsForTechies(SiteVisitReportDto dto);
	public void saveComplaintSiteVisitDtls(RlmsSiteVisitDtls siteVisitDtls);
	public RlmsComplaintTechMapDtls getComplTechMapByComplaintTechMapId(Integer complaintTechMapId);
	public RlmsComplaintTechMapDtls getComplTechMapByComplaintId(Integer complaintId);
	public List<RlmsSiteVisitDtls> getAllVisitsForComnplaints(Integer complaintTechMapId);
	public List<RlmsComplaintTechMapDtls> getListOfComplaintDtlsForTechies(TechnicianWiseReportDTO dto);
	public void updateComplaints(RlmsComplaintTechMapDtls complaintTechMapDtls);
	public void updateComplaintsMatser(RlmsComplaintMaster complaintMaster);
	public void deleteComplaintsTechMap(Integer complaintsTechMapId);
}
