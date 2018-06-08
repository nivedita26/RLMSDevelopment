package com.rlms.service;

import java.io.UnsupportedEncodingException;

import com.rlms.model.RlmsLiftAmcDtls;

public interface AMCMonitorService {
	
	public RlmsLiftAmcDtls getAllAMCDtlsAndUpdateStatus() throws UnsupportedEncodingException;

}
