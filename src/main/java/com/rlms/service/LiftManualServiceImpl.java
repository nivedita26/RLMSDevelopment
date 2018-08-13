package com.rlms.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.CompanyManualDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.CompanyDao;
import com.rlms.dao.LiftManualDao;
import com.rlms.model.RlmsCompanyManual;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsLiftManualMapDtls;

	@Service
	public class LiftManualServiceImpl implements LiftManualService{
		
    @Autowired
    LiftManualDao liftManualDao;
    
    @Autowired
    CompanyDao  companyDao;
    
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveCompanyManual(CompanyManualDto companyManualDto ,UserMetaInfo metaInfo) {
	//RlmsCompanyManual companyManual = new RlmsCompanyManual();
	RlmsCompanyManual companyManual = getCompanyManual(companyManualDto.getCompanyId(),companyManualDto.getLiftType());
	if(companyManual ==null) { 
	   	companyManual = new RlmsCompanyManual();
		}
			companyManual.setLiftType(companyManualDto.getLiftType());
			RlmsCompanyMaster companyMaster = companyDao.getCompanyById(companyManualDto.getCompanyId());
			if(companyMaster!=null){
				companyManual.setRlmsCompanyMaster(companyMaster);
			}
			companyManual.setUserManual(companyManualDto.getUserManual());
			companyManual.setSafetyGuide(companyManualDto.getSafetyGuide());
			companyManual.setCreatedDate(new Date());
			companyManual.setUpdatedDate(new Date());
			companyManual.setActiveFlag(RLMSConstants.ACTIVE.getId());
			//companyManual.setCreatedBy(metaInfo.getUserId());
			//	companyManual.setUpdatedBy(metaInfo.getUserId());
			liftManualDao.saveCompanyManual(companyManual);
	}
	@Override
	public RlmsCompanyManual getCompanyManual(int companyId,int liftType) {
		RlmsCompanyManual companyManual = liftManualDao.getCompanyManual(companyId,liftType);
		 return companyManual;

	}
	
	@Override
	@Transactional
	public RlmsLiftManualMapDtls getLiftManualMapDtls(int liftCustomerMapId) {
		return liftManualDao.getLiftManualMapDtls(liftCustomerMapId);
	}
}
