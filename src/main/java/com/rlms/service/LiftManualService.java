package com.rlms.service;

import com.rlms.contract.CompanyManualDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsCompanyManual;
import com.rlms.model.RlmsLiftManualMapDtls;

public interface LiftManualService {

	public void saveCompanyManual(CompanyManualDto companyManualDto,UserMetaInfo metaInfo);
	public RlmsCompanyManual getCompanyManual(int companyId ,int liftType);
	
	public RlmsLiftManualMapDtls getLiftManualMapDtls(int liftCustomerMapId);

}
