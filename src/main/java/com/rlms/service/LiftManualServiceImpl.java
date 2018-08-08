package com.rlms.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rlms.contract.CompanyManualDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.LiftManualDao;
import com.rlms.model.RlmsCompanyManual;

	@Service
	public class LiftManualServiceImpl implements LiftManualService{
    @Autowired
    LiftManualDao liftManualDao;
	@Override
	public void saveCompanyManual(CompanyManualDto companyManualDto ,UserMetaInfo metaInfo) {
		
		RlmsCompanyManual companyManual = new RlmsCompanyManual();
		companyManual.setLiftType(companyManualDto.getLiftType());
		companyManual.setCompanyId(companyManualDto.getCompanyId());
		companyManual.setUserManual(companyManualDto.getUserManual());
		companyManual.setSafetyGuide(companyManualDto.getSafetyGuide());
		companyManual.setCreatedDate(new Date());
		companyManual.setUpdatedDate(new Date());
		companyManual.setCreatedBy(metaInfo.getUserId());
		companyManual.setUpdatedBy(metaInfo.getUserId());
		
		liftManualDao.saveCompanyManual(companyManual);
	}
	@Override
	public RlmsCompanyManual getCompanyManual(int companyId,int liftType) {
		RlmsCompanyManual companyManual = liftManualDao.getCompanyManual(companyId,liftType);
		 return companyManual;

	}
}
