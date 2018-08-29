package com.rlms.dao;

import com.rlms.model.RlmsMemberMaster;

public interface MemberDao {

	public RlmsMemberMaster getMemberById(int id);
	public RlmsMemberMaster getMemberByContactNumber(String contactNumber);
	public RlmsMemberMaster getMemberByEmailId(String EmailId);

}
