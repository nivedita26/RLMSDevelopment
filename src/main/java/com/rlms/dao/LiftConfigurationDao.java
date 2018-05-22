package com.rlms.dao;

import com.rlms.model.RlmsLiftConfiguationP;
import com.rlms.model.RlmsLiftConfigurationA;
import com.rlms.model.RlmsLiftConfigurationBD;
import com.rlms.model.RlmsLiftConfigurationRTC;

public interface LiftConfigurationDao {

	public void saveLiftConfigA(RlmsLiftConfigurationA liftConfigurationA);
	public void saveLiftConfigBD(RlmsLiftConfigurationBD liftConfigurationBD);
	public void saveLiftConfigP(RlmsLiftConfiguationP liftConfigurationP);
	public void saveLiftConfigRTC(RlmsLiftConfigurationRTC liftConfigurationrtc);
	
}
