package com.rlms.dao;

import com.rlms.model.RlmsCompanyManual;
import com.rlms.model.RlmsLiftManualMapDtls;

public interface LiftManualDao {

	public void saveCompanyManual(RlmsCompanyManual  companyManual);
	public RlmsCompanyManual getCompanyManual(int companyId,int liftType);
	public void saveLiftManualMapDtls(RlmsLiftManualMapDtls  liftManualMapDtls);
	public RlmsLiftManualMapDtls getLiftManualMapDtls(int liftCustomerMapId) ;

}
