package com.rlms.dao;

import java.util.List;

import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;

public interface LiftDao {	
	public List<RlmsLiftCustomerMap> getAllLiftsForCustomers(List<Integer> listOfCuistomers);

	Integer saveLiftM(RlmsLiftMaster liftMaster);

	Integer saveLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap);

	List<RlmsLiftCustomerMap> getAllLiftsToBeApproved();

	RlmsLiftCustomerMap getLiftCustomerMapByLiftId(Integer liftId);

	void updateLiftM(RlmsLiftMaster liftMaster);

	void updateLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap);
	
	public List<RlmsLiftCustomerMap> getAllLiftsForBranchs(Integer branchCompanyId);

	RlmsLiftCustomerMap getLiftCustomerMapById(Integer liftCustomerMapId);
	
	public List<RlmsLiftCustomerMap> getAllLiftsForBranchsOrCustomer(LiftDtlsDto dto);
	
	public List<RlmsLiftCustomerMap> getAllLiftsForCustomres(List<Integer> branchCustoMapId);

	public Integer mergeLiftM(RlmsLiftMaster liftMaster);
	
	public RlmsLiftCustomerMap getLiftMasterForType(Integer branchCustoMapId, Integer liftType);
	
	public List<RlmsLiftAmcDtls> getAMCDetilsForLifts(List<Integer> listOfLiftsForAMCDtls, AMCDetailsDto dto);

	public Integer saveLiftAMCDtls(RlmsLiftAmcDtls liftAmcDtls);
	
	public List<RlmsLiftCustomerMap> getAllLiftsByIds(List<Integer> liftCustomerMapIds);
	
	public List<RlmsLiftAmcDtls> getAllLiftsWithTodaysDueDate();

	public void mergeLiftAMCDtls(RlmsLiftAmcDtls liftAmcDtls);
	
	public List<RlmsLiftAmcDtls> getAllLiftsWithTodaysExpiryDate();
	
	public List<RlmsLiftAmcDtls> getAllAMCDetils(List<Integer> listOfLiftsForAMCDtls, AMCDetailsDto dto);
	
	public List<RlmsLiftCustomerMap> getAllLiftsStatusForBranchs(List<Integer> companyBranchIds);
	
	public List<Object []> liftCountByBranchCustomerMapId (int liftCountByBranchCustomerMapId);

	public void saveEventDtls(RlmsEventDtls eventDtls);
	
	public RlmsLiftMaster getLiftIdByImei(String Imei);
	
	public RlmsBranchCustomerMap getBranchCustomerMapByBranchCustomerMapId(int  Imei);
	
	public RlmsLiftAmcDtls getRlmsLiftAmcDtlsByLiftCustomerMapId(int rlmsLiftCustomerMapId);

}
