package com.rlms.contract;

import com.rlms.model.RlmsUserRoles;

public class UserMetaInfo {

	private Integer userId;
	private String userName;
	private RlmsUserRoles userRole;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public RlmsUserRoles getUserRole() {
		return userRole;
	}
	public void setUserRole(RlmsUserRoles userRole) {
		this.userRole = userRole;
	}
	
	
	
}
