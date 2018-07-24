package com.rlms.dao;

import com.rlms.model.RlmsFyaTranDtls;

public interface FyaDao {

	public void saveFyaTranDtls(RlmsFyaTranDtls fyaTranDtls);

	public RlmsFyaTranDtls getFyaForLift(Integer liftCustomerMapID, Integer status);

	public RlmsFyaTranDtls getFyaByFyaTranIDt(Integer fyaTranId);

	public void updateFyaTranDtls(RlmsFyaTranDtls fyaTranDtls);

}
