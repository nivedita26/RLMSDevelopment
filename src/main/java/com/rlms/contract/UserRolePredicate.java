package com.rlms.contract;

import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsUserRoles;

import org.apache.commons.collections.Predicate;

public class UserRolePredicate implements Predicate{
	 private Integer userRoleId;

	  public UserRolePredicate(Integer userRoleId) {
	    super();
	    this.userRoleId = userRoleId;
	  }

	  /**
	   * returns true when the salary is >= iValue
	   */
	  public boolean evaluate(Object object) {
	    if (object instanceof RlmsComplaintTechMapDtls) {     
	        return ((RlmsComplaintTechMapDtls) object).getUserRoles().getUserRoleId().equals(this.userRoleId);
	   
	  }else{
		  return false;
	  }
	    
	 }
}
