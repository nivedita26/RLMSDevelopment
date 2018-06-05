package com.rlms.constants;

public enum RLMSCallType {

	  LIFT_INSTALLATION_CALL(1,"Lift Installation call"),
	  CONFIGURATION_CALL(2,"Configuration call/setting call"),
	  AMC_CALL(3,"AMC call"),
	  UNDER_WARRANTY_SUPPORT_CALL(4,"Under Warranty Support call"),
	  LMS_ALERT_CALL(5,"LMS alert call"),
      OPERATOR_ASSIGNED_CALL(6,"Operator assigned/Generic call"),
	  USER_RAIGED_CALL_THROUGH_APP(7,"User raised call through App"),
	  USER_RAISED_CALL_THROUGH_TELEPHONE(8,"User raised call through Telephone"),
	  REASSIGNING_CALL(9,"Reassign call");
	
	  private int id;
	  private String type;
	  
	  private RLMSCallType(int id, String type) {
		this.id = id;
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	  public static String getStringFromID(Integer statusId){
			if(statusId == RLMSCallType.AMC_CALL.getId()){
				return RLMSCallType.AMC_CALL.getType();
			}
			else	if(statusId == RLMSCallType.CONFIGURATION_CALL.getId()){
					return RLMSCallType.CONFIGURATION_CALL.getType();
			}
			else	if(statusId == RLMSCallType.LIFT_INSTALLATION_CALL.getId()){
				return RLMSCallType.LIFT_INSTALLATION_CALL.getType();
		}
			else	if(statusId == RLMSCallType.LMS_ALERT_CALL.getId()){
				return RLMSCallType.LMS_ALERT_CALL.getType();
		}
			else	if(statusId == RLMSCallType.OPERATOR_ASSIGNED_CALL.getId()){
				return RLMSCallType.OPERATOR_ASSIGNED_CALL.getType();
		}

			else	if(statusId == RLMSCallType.REASSIGNING_CALL.getId()){
				return RLMSCallType.REASSIGNING_CALL.getType();
		}
			else	if(statusId == RLMSCallType.UNDER_WARRANTY_SUPPORT_CALL.getId()){
				return RLMSCallType.UNDER_WARRANTY_SUPPORT_CALL.getType();
		}
			else	if(statusId == RLMSCallType.USER_RAIGED_CALL_THROUGH_APP.getId()){
				return RLMSCallType.USER_RAIGED_CALL_THROUGH_APP.getType();
		}
			else	if(statusId == RLMSCallType.USER_RAISED_CALL_THROUGH_TELEPHONE.getId()){
				return RLMSCallType.USER_RAISED_CALL_THROUGH_TELEPHONE.getType();
		}	
			return "";
		}
}

