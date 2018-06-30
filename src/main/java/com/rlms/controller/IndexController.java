package com.rlms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.SpocRoleConstants;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.UserService;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController{
	@Autowired
	UserService userService;
	
	
	
	
	  @RequestMapping(value="index",method = RequestMethod.GET)
	    public  String getIndexPage() {
		  RlmsUserRoles userrole = this.getLoggedInUser();
		  if(userrole.getRlmsSpocRoleMaster().getSpocRoleId()==SpocRoleConstants.TECHNICIAN.getSpocRoleId()) {
			  System.out.println("user role"+userrole.getRole());
			  return  "login.jsp";
		  }
		  System.out.println(userrole.getUsername());
	      return "index.jsp";
	    }
	  
	  @RequestMapping(value="login",method = RequestMethod.GET)
	    public String getLoginPage() {
		 System.out.println("hello world");
	        return "login.jsp";
	    }
	  
	  @RequestMapping(value="signup",method = RequestMethod.GET)
	    public String getSignUpPage() {
		 
	        return "signup.jsp";
	  }

	  @RequestMapping(value="getLoggedInUser",method = RequestMethod.POST)
	  public @ResponseBody UserMetaInfo getMetaInfoObj(){
		  return this.getMetaInfo();
	  }
	  
	  @RequestMapping(value="changePassword",method = RequestMethod.POST)
	  public @ResponseBody String changePassword(UserDtlsDto userDto){
		  
		  return userService.changePassword(userDto);
	  }
	  
}