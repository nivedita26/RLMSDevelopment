package com.rlms.service;

import com.rlms.contract.LiftConfigurationDTO;

public interface LiftConfigurationService {

	public String saveAndSendLiftConfiguration(LiftConfigurationDTO dto);
}
