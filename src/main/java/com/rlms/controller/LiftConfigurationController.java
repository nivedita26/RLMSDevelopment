package com.rlms.controller;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.LiftConfigurationDTO;
import com.rlms.contract.ResponseDto;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.service.LiftConfigurationService;
import com.rlms.utils.PropertyUtils;

public class LiftConfigurationController {

	
	@Autowired
	private LiftConfigurationService liftConfigurationService;
	
	private static final Logger logger = Logger.getLogger(ComplaintController.class);
	
	@RequestMapping(value = "/saveAndSendLiftConfiguration", method = RequestMethod.POST)
	 public  @ResponseBody ResponseDto saveAndSendLiftConfiguration(@RequestBody LiftConfigurationDTO dto) throws RunTimeException{
		ResponseDto reponseDto = new ResponseDto();
		 
		 try{
	        	logger.info("Method :: saveAndSendLiftConfiguration");
	        	reponseDto.setResponse(this.liftConfigurationService.saveAndSendLiftConfiguration(dto));
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	}
}
