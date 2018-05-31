package com.rlms.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.EventDtlsDto;
import com.rlms.contract.SiteVisitReportDto;
import com.rlms.contract.TechnicianWiseReportDTO;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.RunTimeException;

public interface ReportService {

	public List<AMCDetailsDto> getAMCDetailsForLifts(AMCDetailsDto dto);
	public String addAMCDetailsForLift(AMCDetailsDto dto, UserMetaInfo metaInfo) throws ParseException;
	public List<SiteVisitReportDto> getSiteVisitReport(SiteVisitReportDto dto);
	public List<TechnicianWiseReportDTO> getTechnicianWiseReport(TechnicianWiseReportDTO dto);
	public void changeStatusToAMCRenewalAndNotifyUser() throws UnsupportedEncodingException;
	public void changeStatusToAMCExpiryAndNotifyUser() throws UnsupportedEncodingException;
	//public List<EventDtlsDto> getListOfEvetnDetails(List<Integer> companyBranchIds	,String eventType);

	public List<EventDtlsDto> getAllInOutEventsData(@RequestBody EventDtlsDto dto);


}