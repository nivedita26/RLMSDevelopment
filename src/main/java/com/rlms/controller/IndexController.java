package com.rlms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController{

	  @RequestMapping(value="index",method = RequestMethod.GET)
	    public String getIndexPage() {
		  RlmsUserRoles userrole = this.getLoggedInUser();
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
}