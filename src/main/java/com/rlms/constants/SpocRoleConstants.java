package com.rlms.constants;

public enum SpocRoleConstants {

	INDITECH_ADMIN(1,"INDITECH_ADMIN",1),
	INDITECH_OPERATOR(2,"INDITECH_OPERATOR",1),
	COMPANY_ADMIN(3,"COMPANY_ADMIN",2),
	COMPANY_OPERATOR(4,"COMPANY_OPERATOR",2),
	BRANCH_ADMIN(5,"BRANCH_ADMIN",3),
	BRANCH_OPERATOR(6,"BRANCH_OPERATOR",3),
	TECHNICIAN(7,"TECHNICIAN",4),
	END_USER(8,"END_USER",5),
	ROLE_LEVEL_ONE(1,"Role Level One",1),
	ROLE_LEVEL_TWO(2,"Role Level Two",2),
	ROLE_LEVEL_THREE(3,"Role Level Three",3);
	
	private Integer spocRoleId;
	private String spocRoleName;
	private Integer roleLevel;
	
	private SpocRoleConstants(Integer spocRoleId, String spocRoleName, Integer roleLevel){
		this.spocRoleId = spocRoleId;
		this.spocRoleName = spocRoleName;
		this.roleLevel = roleLevel;
	}

	public Integer getSpocRoleId() {
		return spocRoleId;
	}

	public void setSpocRoleId(Integer spocRoleId) {
		this.spocRoleId = spocRoleId;
	}

	public String getSpocRoleName() {
		return spocRoleName;
	}

	public void setSpocRoleName(String spocRoleName) {
		this.spocRoleName = spocRoleName;
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}
	
	
	
}
