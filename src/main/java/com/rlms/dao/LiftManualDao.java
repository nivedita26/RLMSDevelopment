package com.rlms.dao;

import com.rlms.contract.CompanyManualDto;
import com.rlms.model.RlmsCompanyManual;

public interface LiftManualDao {

	public void saveCompanyManual(RlmsCompanyManual  companyManual);
	public RlmsCompanyManual getCompanyManual(int companyId,int liftType);

}
