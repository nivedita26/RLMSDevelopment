package com.rlms.service;

import com.rlms.contract.CompanyManualDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsCompanyManual;
import com.rlms.model.RlmsLiftManualMapDtls;

public interface LiftManualService {

	public ResponseDto  saveCompanyManual(CompanyManualDto companyManualDto,UserMetaInfo metaInfo);
	public RlmsCompanyManual getCompanyManual(CompanyManualDto companyManualDto);
	
	public RlmsLiftManualMapDtls getLiftManualMapDtls(int liftCustomerMapId);

}
