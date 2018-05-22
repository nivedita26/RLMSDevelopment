package com.rlms.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.SiteVisitReportDto;
import com.rlms.contract.TechnicianWiseReportDTO;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.service.ReportService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/report")
public class ReportController extends BaseController{

	@Autowired
	private ReportService reportService;
	
	private static final Logger logger = Logger.getLogger(ComplaintController.class);
	
	 @RequestMapping(value = "/getAMCDetailsForLifts", method = RequestMethod.POST)
	    public @ResponseBody List<AMCDetailsDto> getAMCDetailsForLifts(@RequestBody AMCDetailsDto dto) throws RunTimeException, ValidationException {
	        
		 List<AMCDetailsDto> listOFAmcDtls = null;
	        try{
	        	logger.info("In getAMCDetailsForLifts method");
	        	listOFAmcDtls = this.reportService.getAMCDetailsForLifts(dto);
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOFAmcDtls;
	    }
	 
	 @RequestMapping(value = "/addAMCDetailsForLift", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto addAMCDetailsForLift(@RequestBody AMCDetailsDto dto) throws RunTimeException, ValidationException {
	        
		 ResponseDto responseDto = new ResponseDto();
	        try{
	        	logger.info("In addAMCDetailsForLift method");
	        	responseDto.setResponse(this.reportService.addAMCDetailsForLift(dto, this.getMetaInfo()));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return responseDto;
	    }
	 
	 @RequestMapping(value = "/getSiteVisitReport", method = RequestMethod.POST)
	    public @ResponseBody List<SiteVisitReportDto>  getSiteVisitReport(@RequestBody SiteVisitReportDto dto) throws RunTimeException, ValidationException {
	        
		 List<SiteVisitReportDto>  listOfVisitDtls = new ArrayList<SiteVisitReportDto>();
	        try{
	        	logger.info("In getSiteVisitReport method");
	        	listOfVisitDtls = this.reportService.getSiteVisitReport(dto);
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfVisitDtls;
	    }
	 
	 @RequestMapping(value = "/getTechnicianWiseReport", method = RequestMethod.POST)
	    public @ResponseBody List<TechnicianWiseReportDTO>  getTechnicianWiseReport(@RequestBody TechnicianWiseReportDTO dto) throws RunTimeException, ValidationException {
	        
		 List<TechnicianWiseReportDTO>  listOfTEchis = new ArrayList<TechnicianWiseReportDTO>();
	        try{
	        	logger.info("In getTechnicianWiseReport method");
	        	listOfTEchis = this.reportService.getTechnicianWiseReport(dto);
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfTEchis;
	    }
}
