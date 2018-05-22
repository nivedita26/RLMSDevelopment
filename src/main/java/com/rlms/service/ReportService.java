package com.rlms.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.SiteVisitReportDto;
import com.rlms.contract.TechnicianWiseReportDTO;
import com.rlms.contract.UserMetaInfo;

public interface ReportService {

	public List<AMCDetailsDto> getAMCDetailsForLifts(AMCDetailsDto dto);
	public String addAMCDetailsForLift(AMCDetailsDto dto, UserMetaInfo metaInfo) throws ParseException;
	public List<SiteVisitReportDto> getSiteVisitReport(SiteVisitReportDto dto);
	public List<TechnicianWiseReportDTO> getTechnicianWiseReport(TechnicianWiseReportDTO dto);
	public void changeStatusToAMCRenewalAndNotifyUser() throws UnsupportedEncodingException;
	public void changeStatusToAMCExpiryAndNotifyUser() throws UnsupportedEncodingException;
}
