package com.rlms.service;

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.contract.LiftConfigurationDTO;
import com.rlms.dao.LiftConfigurationDao;
import com.rlms.model.RlmsLiftConfiguationP;
import com.rlms.model.RlmsLiftConfigurationA;
import com.rlms.model.RlmsLiftConfigurationBD;
import com.rlms.model.RlmsLiftConfigurationRTC;

public class LiftConfigurationServiceImpl implements LiftConfigurationService{

	@Autowired
	private LiftConfigurationDao liftConfigurationDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveAndSendLiftConfiguration(LiftConfigurationDTO dto){
		RlmsLiftConfigurationA liftConfigurationA = this.constructLiftConfigA(dto);
		this.liftConfigurationDao.saveLiftConfigA(liftConfigurationA);
		
		RlmsLiftConfigurationBD liftConfigurationBD = this.constructLiftConfigBD(dto);
		this.liftConfigurationDao.saveLiftConfigBD(liftConfigurationBD);
		
		RlmsLiftConfigurationRTC liftConfigurationRTC = this.constrConfigurationRTC(dto);
		this.liftConfigurationDao.saveLiftConfigRTC(liftConfigurationRTC);
		
		RlmsLiftConfiguationP liftConfiguationP = this.constructLiftConfigP(dto);
		this.liftConfigurationDao.saveLiftConfigP(liftConfiguationP);
		
		return "Configuration of lift is done successfully.";
		
	}
	
	private RlmsLiftConfiguationP constructLiftConfigP(LiftConfigurationDTO dto){
		RlmsLiftConfiguationP liftConfiguationP = new RlmsLiftConfiguationP();
		liftConfiguationP.setLiftCustoMapId(dto.getLiftCustoMapId());
		liftConfiguationP.setP0(dto.getP0());
		liftConfiguationP.setP1(dto.getP1());
		liftConfiguationP.setP2(dto.getP2());
		liftConfiguationP.setP3(dto.getP3());
		liftConfiguationP.setP4(dto.getP4());
		liftConfiguationP.setP5(dto.getP5());
		liftConfiguationP.setP6(dto.getP6());
		liftConfiguationP.setP7(dto.getP7());
		liftConfiguationP.setP8(dto.getP8());
		liftConfiguationP.setP9(dto.getP9());
		
		return liftConfiguationP;
	}
	
	private RlmsLiftConfigurationA constructLiftConfigA(LiftConfigurationDTO dto){
		RlmsLiftConfigurationA liftConfigurationA = new RlmsLiftConfigurationA();
		liftConfigurationA.setA0(dto.getA0());
		liftConfigurationA.setA1(dto.getA1());
		liftConfigurationA.setA2(dto.getA2());
		liftConfigurationA.setA3(dto.getA3());
		liftConfigurationA.setA4(dto.getA4());
		liftConfigurationA.setA5(dto.getA5());
		liftConfigurationA.setA6_0(dto.getA6_0());
		liftConfigurationA.setA6_1(dto.getA6_1());
		liftConfigurationA.setA6_2(dto.getA6_2());
		liftConfigurationA.setA6_3(dto.getA6_3());
		liftConfigurationA.setA6_4(dto.getA6_4());
		liftConfigurationA.setA6_5(dto.getA6_5());
		liftConfigurationA.setA7(dto.getA7());
		liftConfigurationA.setA8_0(dto.getA8_0());
		liftConfigurationA.setA8_1(dto.getA8_1());
		liftConfigurationA.setA8_2(dto.getA8_2());
		liftConfigurationA.setA8_3(dto.getA8_3());
		liftConfigurationA.setA8_4(dto.getA8_4());
		liftConfigurationA.setA8_5(dto.getA8_5());
		liftConfigurationA.setA9_0(dto.getA9_0());
		liftConfigurationA.setA9_1(dto.getA9_1());
		liftConfigurationA.setA9_2(dto.getA9_2());
		liftConfigurationA.setA9_3(dto.getA9_3());
		liftConfigurationA.setA9_4(dto.getA9_4());
		liftConfigurationA.setA9_5(dto.getA9_5());
		return liftConfigurationA;
	}
	
	private RlmsLiftConfigurationBD constructLiftConfigBD(LiftConfigurationDTO dto){
		RlmsLiftConfigurationBD liftConfigurationBD = new RlmsLiftConfigurationBD();
		
		liftConfigurationBD.setB0(dto.getB0());
		liftConfigurationBD.setB1(dto.getB1());
		liftConfigurationBD.setB2(dto.getB2());
		liftConfigurationBD.setB3(dto.getB3());
		liftConfigurationBD.setB4(dto.getB4());
		liftConfigurationBD.setB5(dto.getB5());
		liftConfigurationBD.setB6(dto.getB6());
		liftConfigurationBD.setB7(dto.getB7());
		liftConfigurationBD.setB8(dto.getB8());
		liftConfigurationBD.setB9(dto.getB9());
		
		liftConfigurationBD.setbA(dto.getbA());
		liftConfigurationBD.setbB(dto.getbB());
		liftConfigurationBD.setbC(dto.getbC());
		liftConfigurationBD.setbD(dto.getbD());
		liftConfigurationBD.setbE(dto.getbE());
		liftConfigurationBD.setbF(dto.getbF());
		
		liftConfigurationBD.setD0(dto.getD0());
		liftConfigurationBD.setD1(dto.getD1());
		liftConfigurationBD.setD2(dto.getD2());
		liftConfigurationBD.setD3(dto.getD3());
		liftConfigurationBD.setD4(dto.getD4());
		liftConfigurationBD.setD5(dto.getD5());
		liftConfigurationBD.setD6(dto.getD6());
		liftConfigurationBD.setD7(dto.getD7());
		liftConfigurationBD.setD8(dto.getD8());
		liftConfigurationBD.setD9(dto.getD9());
		
		
		liftConfigurationBD.setdA(dto.getdA());
		liftConfigurationBD.setdB(dto.getdB());
		liftConfigurationBD.setdC(dto.getdC());
		liftConfigurationBD.setdD(dto.getdD());
		liftConfigurationBD.setdE(dto.getdE());
		liftConfigurationBD.setdF(dto.getdF());
		
		return liftConfigurationBD;
		
		
	}
	
	private RlmsLiftConfigurationRTC constrConfigurationRTC(LiftConfigurationDTO dto){
		RlmsLiftConfigurationRTC liftConfigurationRTC =  new RlmsLiftConfigurationRTC();
		
		liftConfigurationRTC.setR0(dto.getR0());
		liftConfigurationRTC.setR1(dto.getR1());
		liftConfigurationRTC.setR2(dto.getR2());
		liftConfigurationRTC.setR3(dto.getR3());
		liftConfigurationRTC.setR4(dto.getR4());
		liftConfigurationRTC.setR5(dto.getR5());
		
		liftConfigurationRTC.setT0(dto.getT0());
		liftConfigurationRTC.setT1(dto.getT1());
		liftConfigurationRTC.setT2(dto.getT2());
		liftConfigurationRTC.setT3(dto.getT3());
		liftConfigurationRTC.setT4(dto.getT4());
		liftConfigurationRTC.setT5(dto.getT5());
		
		liftConfigurationRTC.setC0(dto.getC0());
		liftConfigurationRTC.setC1(dto.getC1());
		liftConfigurationRTC.setC2(dto.getC2());
		liftConfigurationRTC.setC3(dto.getC3());
		liftConfigurationRTC.setC4(dto.getC4());
		
		return liftConfigurationRTC;
		
	}
}
