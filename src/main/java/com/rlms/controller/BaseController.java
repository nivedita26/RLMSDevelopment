package com.rlms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.service.UserService;

@Controller
@RequestMapping("/")
public class BaseController {
	
	
	@Autowired
	private UserService userService;
	
	private UserMetaInfo userMetaInfo;
	
    public RlmsUserRoles getLoggedInUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userName = authentication.getName();
    	authentication.getCredentials();
    	return this.userService.getUserByUserName(userName);
    }
    public void setMetaInfo(RlmsUserRoles userRoles){
    	this.userMetaInfo = new UserMetaInfo();
    	this.userMetaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
    	this.userMetaInfo.setUserName(userRoles.getUsername());
    	this.userMetaInfo.setUserRole(userRoles);
    }
    public UserMetaInfo getMetaInfo(){
    	this.setMetaInfo(this.getLoggedInUser());
    	return this.userMetaInfo;
    }
	
}
