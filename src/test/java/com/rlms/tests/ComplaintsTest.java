package com.rlms.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.ComplaintsService;
import com.rlms.service.UserService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/mvc-dispatcher-servlet.xml"})
public class ComplaintsTest {

	 @Autowired
	 @Qualifier("ComplaintsService")	 
	 private ComplaintsService complaintsService;
	 
	 @Autowired
	 @Qualifier("userService")	 
	 private UserService userService;

	    @Test
	    public void validateAndRegisterNewComplaint() throws ValidationException{

	    	UserMetaInfo metaInfo = this.getMetaInfo();
	    	ComplaintsDtlsDto dto = new ComplaintsDtlsDto();
	    	dto.setComplaintsRemark("Door is opening very slow.");
	    	dto.setComplaintsTitle("Door Speed");
	    	dto.setRegistrationType(0);
	    	dto.setLiftCustomerMapId(6);
	    	this.complaintsService.validateAndRegisterNewComplaint(dto, metaInfo);
	    	
	    	
	    	

	    }
	    
	    private UserMetaInfo getMetaInfo(){
	    	RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(1);
	    	UserMetaInfo metaInfo = new UserMetaInfo();
	    	metaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
	    	metaInfo.setUserName(userRoles.getRlmsUserMaster().getUsername());
	    	metaInfo.setUserRole(userRoles);
	    	return metaInfo;
	    }
}
