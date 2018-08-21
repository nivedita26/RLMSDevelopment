package com.rlms.dao;

import com.rlms.contract.CompanyManualDto;
import com.rlms.model.RlmsCompanyManual;
import com.rlms.model.RlmsLiftManualMapDtls;

public interface LiftManualDao {

	public void saveCompanyManual(RlmsCompanyManual  companyManual);
	public RlmsCompanyManual getCompanyManual(CompanyManualDto companyManualDto);
	public void saveLiftManualMapDtls(RlmsLiftManualMapDtls  liftManualMapDtls);
	public RlmsLiftManualMapDtls getLiftManualMapDtls(int liftCustomerMapId) ;

}
