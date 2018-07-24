package com.rlms.contract;

import org.apache.commons.collections.Predicate;
import com.rlms.model.RlmsComplaintTechMapDtls;

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
