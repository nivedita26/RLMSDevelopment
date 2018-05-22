package com.rlms.predicates;
import org.apache.commons.collections.Predicate;

import com.rlms.model.RlmsLiftAmcDtls;

public class LiftPredicate implements Predicate{
	 private Integer liftId;

	  public LiftPredicate(Integer liftId) {
	    super();
	    this.liftId = liftId;
	  }

	  /**
	   * returns true when the salary is >= iValue
	   */
	  public boolean evaluate(Object object) {
	    if (object instanceof RlmsLiftAmcDtls) {     
	        return ((RlmsLiftAmcDtls) object).getLiftCustomerMap().getLiftMaster().getLiftId().equals(this.liftId);
	   
	  }else{
		  return false;
	  }
	    
	 }
}
