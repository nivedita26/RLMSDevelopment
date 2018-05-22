package com.rlms.service;

import java.text.ParseException;
import java.util.List;

import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsLiftCustomerMap;

public interface LiftService {

	public List<RlmsLiftCustomerMap> getAllLiftsForBranch(Integer companyBranchMapId);

	String validateAndAddNewLiftDtls(LiftDtlsDto dto, UserMetaInfo metaInfo) throws ParseException;
	
	public String approveLift(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo);

	List<LiftDtlsDto> getLiftsToBeApproved();
	
	public List<LiftDtlsDto> getLiftDetailsForBranch(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo);
	
	public String uploadPhoto(LiftDtlsDto dto);
	
	public LiftDtlsDto getLiftMasterForType(LiftDtlsDto loftDtlsDto);
	
	public CustomerDtlsDto getAddressDetailsOfLift(Integer branchCustoMapId);
	
	public String updateLiftDetails(LiftDtlsDto dto, UserMetaInfo userMetaInfo);
	
	public List<LiftDtlsDto> getLiftStatusForBranch(List<Integer> companyBranchIds, UserMetaInfo metaInfo);
	
	public List<LiftDtlsDto> getLiftCountForBranch(List<Integer> companyBranchIds, UserMetaInfo metaInfo);

	
	
}
