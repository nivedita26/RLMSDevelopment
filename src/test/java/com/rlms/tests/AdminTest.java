package com.rlms.tests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.CompanyService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AdminTest.class})
public class AdminTest {
	
	
		//DI
	   @Autowired
	   private CompanyService companyService;

	    @Test
	    public void validateAndSaveCompanyObj() throws ValidationException{

	    	CompanyDtlsDTO companyDtlsDTO = new CompanyDtlsDTO();
	    	
	    	companyDtlsDTO.setCompanyName("Telesist Technologies");
	    	companyDtlsDTO.setAddress("Aundh, Pune");
	    	companyDtlsDTO.setContactNumber("020 45789145");
	    	companyDtlsDTO.setEmailId("support@telesist.com");
			companyDtlsDTO.setPanNumber("AQDPT9121G");
			companyDtlsDTO.setTinNumber("AQDPT9121G");
			companyDtlsDTO.setVatNumber("AQDPT9121G");
			companyDtlsDTO.setCity("Pune");
			companyDtlsDTO.setArea("Wakad");
			companyDtlsDTO.setPinCode(411057);
			companyDtlsDTO.setOwnerName("Sanket Tagalpallewar");
			companyDtlsDTO.setOwnerNumber("9096136232");
			companyDtlsDTO.setOwnerEmail("sanket.tagalpallewar@gmail.com");
			
			
			RlmsUserRoles userRoles = new RlmsUserRoles();
			userRoles.setRole("Developer");
			UserMetaInfo metaInfo = new UserMetaInfo();
			metaInfo.setUserId(0);
			metaInfo.setUserName("System");
			metaInfo.setUserRole(userRoles);
			
	    	this.companyService.validateAndSaveCompanyObj(companyDtlsDTO, metaInfo);

	    }
	
}
