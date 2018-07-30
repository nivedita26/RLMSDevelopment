package com.rlms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.UserService;

@Controller
@RequestMapping("/")
public class BaseController {
		
	@Autowired
	private UserService userService;
	
	private UserMetaInfo userMetaInfo;
	
    public String  getLoggedInUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userName = authentication.getName();
    	authentication.getCredentials();
    	if(userName!=null) {
    	//RlmsUserRoles  rlmsUserRoles = this.userService.getUserByUserName(userName);
      //if(rlmsUserRoles!=null) {
    	return userName;
    	}
    	   return "login.jsp";
 //     return this.userService.getUserByUserName(userName);
    }
    public RlmsUserRoles isLoggedIn(String userName) {
    	RlmsUserRoles  rlmsUserRoles = this.userService.getUserByUserName(userName);
   // 	if(rlmsUserRoles!=null) {
    	return rlmsUserRoles;
  //  	}
      //return null;
    }
    public void setMetaInfo(RlmsUserRoles userRoles){
    	if(userRoles!=null) {
    	this.userMetaInfo = new UserMetaInfo();
    	this.userMetaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
    	this.userMetaInfo.setUserName(userRoles.getUsername());
    	this.userMetaInfo.setUserRole(userRoles);
    	}
    }
    public UserMetaInfo getMetaInfo(){
    	String userName = this.getLoggedInUser();
    		RlmsUserRoles userRoles =this.isLoggedIn(userName);
    		this.setMetaInfo(userRoles);
    		return this.userMetaInfo;
    }
}
