package com.rlms.dao;

import java.util.Date;
import java.util.List;

import com.rlms.contract.EventDtlsDto;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsUserRoles;

public interface DashboardDao {

	public List<RlmsLiftAmcDtls> getAMCDetilsForLifts();

	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(
			Integer branchCompanyMapId, Integer branchCustomerMapId,
			List<Integer> listOfLiftCustoMapId, List<Integer> statusList,
			Date fromDate, Date toDate,Integer callType);

	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(Integer complaintId);
	public List<RlmsUserRoles> getAllUserWithRoleFor(List<Integer> commpBranchMapId, Integer spocRoleId);
	public List<RlmsCompanyBranchMapDtls> getAllBranchesForDashboard(Integer companyId);
	public List<RlmsCompanyBranchMapDtls> getAllBranchDtlsForDashboard(List<Integer> ListOfCompanyIds);
	public RlmsCompanyBranchMapDtls getCompanyBranchMapDtlsForDashboard(Integer compBranchMapId);

	public List<RlmsEventDtls> getAllEventDtlsForDashboard(
			List<Integer> companyBranchIds);
	
	public void saveEventDtls(RlmsEventDtls eventDtls);
	
    public List<RlmsEventDtls> getListOfEventsByType(RlmsEventDtls rlmsEventDtls);
    
    public List<Object[]> getTechnicianCountByCompanyBranchMap(
			List<Integer> commpBranchMapId, Integer spocRoleId) ;
    
    public List<Object[]> getTechnicianActiveStatusCountByCompanyBranchMap(
			Integer  commpBranchMapId, Integer spocRoleId);
    
    public List<Object[]> getEventCountDtlsForDashboard(
			List<Integer> companyBranchIds);


}
