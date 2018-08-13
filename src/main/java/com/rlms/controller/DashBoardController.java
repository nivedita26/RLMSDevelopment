package com.rlms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.AMCStatusCount;
import com.rlms.contract.BranchCountDtls;
import com.rlms.contract.BranchDtlsDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.ComplaintsCount;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerCountDtls;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.EventCountDtls;
import com.rlms.contract.EventDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.TechnicianCount;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.dao.ComplaintsDao;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsSiteVisitDtls;
import com.rlms.service.CompanyService;
import com.rlms.service.ComplaintsService;
import com.rlms.service.CustomerService;
import com.rlms.service.DashboardService;
import com.rlms.service.LiftService;
import com.rlms.service.MessagingService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController extends BaseController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private LiftDao liftDao;

	@Autowired
	ComplaintsDao complaintsDao;

	@Autowired
	private LiftService liftService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ComplaintsService complaintsService;

	@Autowired
	private MessagingService messagingService;

	private static final Logger logger = Logger.getLogger(ComplaintController.class);

	@RequestMapping(value = "/getAMCDetails", method = RequestMethod.POST)
	public @ResponseBody List<AMCDetailsDto> getAMCDetailsForDashboard(@RequestBody AMCDetailsDto amcDetailsDto)
			throws RunTimeException, ValidationException {

		List<AMCDetailsDto> listOFAmcDtls = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService.getAllBranches(amcDetailsDto.getCompanyId());
			if(listOfAllBranches !=null && !listOfAllBranches.isEmpty()) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}
			List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchIds);
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			if(allCustomersForBranch!=null && !allCustomersForBranch.isEmpty()) {
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dto = new LiftDtlsDto();
				dto.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dto);
				if(list!=null && !list.isEmpty()) {
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
				}
			}
			}
			listOFAmcDtls = this.dashboardService.getAMCDetailsForDashboard(liftCustomerMapIds, amcDetailsDto);
			} 
		}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		
	}
		return listOFAmcDtls;
	}

	@RequestMapping(value = "/getAllAMCDetailsCount", method = RequestMethod.POST)
	public @ResponseBody List<AMCStatusCount> getAMCDetailsCountForDashboard(@RequestBody AMCDetailsDto amcDetailsDto)
			throws RunTimeException, ValidationException {
		List<AMCStatusCount> amcStatusDetailsCountList = new ArrayList<>();
		List<AMCStatusCount> amcStatusCounts = new ArrayList<>();
		List<AMCDetailsDto> listOFAmcDtls = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			if(amcDetailsDto.getBranchCompanyMapId()!=null) {
				companyBranchIds.add(amcDetailsDto.getBranchCompanyMapId());
			}
			else {
			listOfAllBranches = this.companyService.getAllBranches(amcDetailsDto.getCompanyId());
			if (listOfAllBranches != null && !listOfAllBranches.isEmpty()) {
				for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
					companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
				 }
			 }
		 }
				List<CustomerDtlsDto> allCustomersForBranch = dashboardService
						.getAllCustomersForBranch(companyBranchIds);

				if (allCustomersForBranch != null && !allCustomersForBranch.isEmpty()) {

					for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
						List<Integer> liftCustomerMapIds = new ArrayList<>();

						LiftDtlsDto dto = new LiftDtlsDto();
						dto.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
						List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dto);

						if (list != null && !list.isEmpty()) {
							for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
								liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
							}

							amcStatusCounts = this.dashboardService.getAMCDetailsCountForDashboard(liftCustomerMapIds,
									amcDetailsDto);
							AMCStatusCount amcStatusCount = new AMCStatusCount();

							if (amcStatusCounts != null && !amcStatusCounts.isEmpty()) {
								for (AMCStatusCount statusCount : amcStatusCounts) {
									if ((statusCount.getStatusId()) == (Status.UNDER_WARRANTY.getStatusId())) {
										amcStatusCount.setUnderWarrantyCount(statusCount.getStatusCount());
									}
									if ((statusCount.getStatusId()) == (Status.RENEWAL_DUE.getStatusId())) {
										amcStatusCount.setRenewalDueCount(statusCount.getStatusCount());
									}
									if ((statusCount.getStatusId()) == (Status.AMC_PENDING.getStatusId())) {
										amcStatusCount.setAmcPendingCount(statusCount.getStatusCount());
									}
									if ((statusCount.getStatusId()) == (Status.UNDER_AMC.getStatusId())) {
										amcStatusCount.setUnderAMCCount(statusCount.getStatusCount());
									}
									/*if ((statusCount.getStatusId()) == (Status.NOT_UNDER_AMC.getStatusId())) {
										amcStatusCount.setNotUnderAMCCount(statusCount.getStatusCount());
									}*/

									if ((statusCount.getStatusId()) == (Status.WARRANTY_EXPIRED.getStatusId())) {
										amcStatusCount.setNotUnderWarranty(statusCount.getStatusCount());
									}
								}
								amcStatusCount.setBranchName(customerDtlsDto.getBranchName());
								amcStatusCount.setCustomerName(customerDtlsDto.getCustomerName());
								amcStatusCount.setCity(customerDtlsDto.getCity());
								amcStatusCount.setTotalLiftCount(list.size());
								amcStatusDetailsCountList.add(amcStatusCount);
							}
						}
					}
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return amcStatusDetailsCountList;
	}

	@RequestMapping(value = "/getListOfTechniciansForDashboard", method = RequestMethod.POST)
	public @ResponseBody List<UserRoleDtlsDTO> getListOfTechnicians(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<UserRoleDtlsDTO> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		List<Integer> branchCustomerMapIds = new ArrayList<>();
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}
		try {
			logger.info("Method :: getListOfComplaints");
			listOfComplaints = this.dashboardService.getListOfTechnicians(companyBranchMapIds);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}

		return listOfComplaints;
	}
	@RequestMapping(value = "/getTotalCountOfTechniciansForBranch", method = RequestMethod.POST)
	public @ResponseBody List<TechnicianCount> getTotalCountOfTechniciansForBranch(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<TechnicianCount> technicianCounts = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		
		if(dto.getBranchCompanyMapId()!=null) {
			companyBranchMapIds.add(dto.getBranchCompanyMapId());
		}
		else {
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
			}
		}
		}
		try {
			logger.info("Method :: getListOfComplaints");
			technicianCounts = this.dashboardService.getListOfTechniciansForBranch(companyBranchMapIds);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return technicianCounts;
	}
	@RequestMapping(value = "/getListOfComplaintsForDashboard", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsDto> getListOfComplaints(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsDto> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if(listOfAllBranches!=null &&!listOfAllBranches.isEmpty()) {
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		if(allCustomersForBranch!=null && !allCustomersForBranch.isEmpty()) {
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			if(list!=null &&!list.isEmpty()) {
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
				liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
			}
		}
		if(liftCustomerMapIds!=null && !liftCustomerMapIds.isEmpty()) {
			dto.setListOfLiftCustoMapId(liftCustomerMapIds);
			listOfComplaints = this.dashboardService.getListOfComplaintsBy(dto);
		 }	}
		}
	}
		return listOfComplaints;
	}
	@RequestMapping(value = "/getListOfTotalComplaintsCountByCallType", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsCount> getListOfComplaintsCountByCallType(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsCount> listOfComplaintsCount = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		if(dto.getBranchCompanyMapId()!=null) {
			companyBranchMapIds.add(dto.getBranchCompanyMapId());
		}
		else {
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if (listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}}
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		if(allCustomersForBranch!=null && !allCustomersForBranch.isEmpty()) {
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			if(list!=null && !list.isEmpty()){
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
				liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
			}	}
		}
		if(liftCustomerMapIds!=null && !liftCustomerMapIds.isEmpty()) {
			dto.setListOfLiftCustoMapId(liftCustomerMapIds);
			listOfComplaintsCount = this.dashboardService.getListOfTotalComplaintsCountByCallType(dto);
		}}	
		return listOfComplaintsCount;
	}
	@RequestMapping(value = "/getListOfTodaysComplaintsCountByCallType", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsCount> getListOfTodaysComplaintsCountByCallType(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsCount> listOfComplaintsCount = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		if(dto.getBranchCompanyMapId()!=null) {
			companyBranchMapIds.add(dto.getBranchCompanyMapId());
		}
		else {
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if(listOfAllBranches!=null &&!listOfAllBranches.isEmpty()) {
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
			}
			}
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		if(allCustomersForBranch!=null  && !allCustomersForBranch.isEmpty() ) {
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			if(list!=null && !list.isEmpty()) {
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
					}}}
			if(liftCustomerMapIds!=null && !liftCustomerMapIds.isEmpty()) {
					dto.setListOfLiftCustoMapId(liftCustomerMapIds);
					listOfComplaintsCount = this.dashboardService.getListOfTodaysComplaintsCountByCallType(dto);
				}
		}
		return listOfComplaintsCount;
	}
	@RequestMapping(value = "/getListOfTotalComplaintsCountByStatus", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsCount> getListOfTotalComplaintsCountByStatus(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsCount> listOfComplaintsCount = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		if(dto.getBranchCompanyMapId()!=null) {
			companyBranchMapIds.add(dto.getBranchCompanyMapId());
		}
		else {
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if(listOfAllBranches!=null  && !listOfAllBranches.isEmpty() ) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
			}
		}
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		if(allCustomersForBranch!=null  && !allCustomersForBranch.isEmpty() ) {
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
				if(list!=null  && !list.isEmpty() ) {
					for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
						liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
					}
				}
		 }
		 	if(liftCustomerMapIds!=null && !liftCustomerMapIds.isEmpty()) {
		 		dto.setListOfLiftCustoMapId(liftCustomerMapIds);
		 		listOfComplaintsCount = this.dashboardService.getListOfTotalComplaintsCountByStatus(dto);
		    }
		}
		return listOfComplaintsCount;
	}
	
	@RequestMapping(value = "/getListOfTodaysComplaintsCountByStatus", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsCount> getListOfTodaysComplaintsCountByStatus(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsCount> listOfComplaintsCount = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		if(dto.getBranchCompanyMapId()!=null) {
			companyBranchMapIds.add(dto.getBranchCompanyMapId());
		}
		else {
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if(listOfAllBranches!=null  && !listOfAllBranches.isEmpty() ) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
			}
		}
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		if(allCustomersForBranch!=null  && !allCustomersForBranch.isEmpty() ) {
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
				dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
				if(list!=null && !list.isEmpty()) {
					for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
						liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
					}
				}
			}
				if(liftCustomerMapIds!=null && !liftCustomerMapIds.isEmpty()) {
					dto.setListOfLiftCustoMapId(liftCustomerMapIds);
					listOfComplaintsCount = this.dashboardService.getListOfTodaysComplaintsCountByStatus(dto);
				}
		}
		return listOfComplaintsCount;
	}
	
	@RequestMapping(value = "/getListOfTodaysTotalComplaintsCountByStatus", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsCount> getListOfTodaysTotalComplaintsCountByStatus(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsCount> listOfComplaintsCount = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		if(dto.getBranchCompanyMapId()!=null) {
			companyBranchMapIds.add(dto.getBranchCompanyMapId());
		}
		else {
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
			}
		}
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		if(allCustomersForBranch!=null && !allCustomersForBranch.isEmpty()) {
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			if(list!=null && !list.isEmpty()) {
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
				}
			}
		}
		if(liftCustomerMapIds!=null &&! liftCustomerMapIds.isEmpty()) {
			dto.setListOfLiftCustoMapId(liftCustomerMapIds);
			listOfComplaintsCount = this.dashboardService.getListOfTodaysTotalComplaintsCountByStatus(dto);
		}
		}
		return listOfComplaintsCount;
	}
	@RequestMapping(value = "/getListOfComplaintsForSiteVisited", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsDto> getListOfComplaintsForSiteVisited(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsDto> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		Set<Integer> siteVisitedTodayComplaintIds = new HashSet<>();
		List<Integer> companyBranchMapIds = new ArrayList<>();
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
			}
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
				liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
			}
		}
		dto.setListOfLiftCustoMapId(liftCustomerMapIds);
		try {
			logger.info("Method :: getListOfComplaints");
			listOfComplaints = this.dashboardService.getListOfComplaintsBy(dto);
			List<Integer> listOfComplaintIds = new ArrayList<>();
			for (ComplaintsDto complaintsDto : listOfComplaints) {
				listOfComplaintIds.add(complaintsDto.getComplaintId());
			}
			Date todayDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			for (Integer complaintId : listOfComplaintIds) {
				RlmsComplaintTechMapDtls rlmsTechMapId = dashboardService.getComplTechMapObjByComplaintId(complaintId);
				if (rlmsTechMapId != null) {
					List<RlmsSiteVisitDtls> allVisits = dashboardService
							.getAllVisitsForComnplaints(rlmsTechMapId.getComplaintTechMapId());
					for (RlmsSiteVisitDtls rlmsSiteVisitDtls : allVisits) {
						if (sdf.format(rlmsSiteVisitDtls.getCreatedDate()).equals(sdf.format(todayDate))) {
							siteVisitedTodayComplaintIds.add(complaintId);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		List<ComplaintsDto> finalListOfComplaints = new ArrayList<>();
		for (ComplaintsDto complaintsDto : listOfComplaints) {
			if (siteVisitedTodayComplaintIds.contains(complaintsDto.getComplaintId())) {
				finalListOfComplaints.add(complaintsDto);
			}
		}
		return finalListOfComplaints;
	}
	@RequestMapping(value = "/getAllAMCDetails", method = RequestMethod.POST)
	public @ResponseBody List<AMCDetailsDto> getAMCForDashboard(@RequestBody AMCDetailsDto amcDetailsDto)
			throws RunTimeException, ValidationException {

		List<AMCDetailsDto> listOFAmcDtls = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService.getAllBranches(amcDetailsDto.getCompanyId());
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}

			List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchIds);
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dto = new LiftDtlsDto();
				dto.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dto);
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
				}
			}
			listOFAmcDtls = this.dashboardService.getAllAMCDetails(liftCustomerMapIds, amcDetailsDto);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOFAmcDtls;
	}

	@RequestMapping(value = "/getLiftStatus", method = RequestMethod.POST)
	public @ResponseBody List<LiftDtlsDto> getLiftStatus(@RequestBody LiftDtlsDto liftDtlsDto)
			throws RunTimeException, ValidationException {

		List<LiftDtlsDto> listOfLifts = new ArrayList<LiftDtlsDto>();
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			if(liftDtlsDto.getBranchCompanyMapId()!=null) {
				companyBranchIds.add(liftDtlsDto.getBranchCompanyMapId());
			}
			else {
			listOfAllBranches = this.companyService.getAllBranches(liftDtlsDto.getCompanyId());
			if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
				for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
					companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
				}
			}
			}
			listOfLifts = liftService.getLiftStatusForBranch(companyBranchIds, this.getMetaInfo());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfLifts;
	}

	@RequestMapping(value = "/getLiftCount", method = RequestMethod.POST)
	public @ResponseBody List<LiftDtlsDto> getLiftCount(@RequestBody LiftDtlsDto liftDtlsDto)
			throws RunTimeException, ValidationException {
		List<LiftDtlsDto> listOfLifts = new ArrayList<LiftDtlsDto>();
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			if(liftDtlsDto.getBranchCompanyMapId()!=null) {
				companyBranchIds.add(liftDtlsDto.getBranchCompanyMapId());
			}
			else {
			listOfAllBranches = this.companyService.getAllBranches(liftDtlsDto.getCompanyId());
			if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}}
			}
			listOfLifts = liftService.getLiftCountForBranch(companyBranchIds, this.getMetaInfo());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfLifts;
	}

	@RequestMapping(value = "/getLiftStatusCountByCustomer", method = RequestMethod.POST)
	public @ResponseBody List<LiftDtlsDto> getLiftStatusCountByCustomer(@RequestBody LiftDtlsDto liftDtlsDto)
			throws RunTimeException, ValidationException {
		List<LiftDtlsDto> listOfLifts = new ArrayList<LiftDtlsDto>();
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			if(liftDtlsDto.getBranchCompanyMapId()!=null) {
				companyBranchIds.add(liftDtlsDto.getBranchCompanyMapId());
			}
			else {
			listOfAllBranches = this.companyService.getAllBranches(liftDtlsDto.getCompanyId());
			if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}
			}}
			listOfLifts = liftService.getLiftStatusForBranch(companyBranchIds, this.getMetaInfo());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfLifts;
	}

	@RequestMapping(value = "/getListOfCustomerForDashboard", method = RequestMethod.POST)
	public @ResponseBody List<CustomerDtlsDto> getListOfCustomerDtls(@RequestBody CustomerDtlsDto customerDtlsDto)
			throws RunTimeException {
		List<CustomerDtlsDto> listOfCustomers = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService.getAllBranches(customerDtlsDto.getCompanyId());
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}
			listOfCustomers = this.customerService.getAllApplicableCustomersForDashboard(companyBranchIds,
					this.getMetaInfo());

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfCustomers;
	}
	@RequestMapping(value = "/getCustomerCountForDashboard", method = RequestMethod.POST)
	public @ResponseBody List<CustomerCountDtls> getCustomerCountForDashboard(
			@RequestBody CustomerDtlsDto customerDtlsDto) throws RunTimeException {
		List<CustomerCountDtls> listOfCustomersCount = new ArrayList<>();
		List<RlmsBranchCustomerMap> listOfBranchCustomersMap = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		int activeCount = 0;
		int inactiveCount = 0;
		try {
			logger.info("Method :: getAllBranchesForCompany");
			if(customerDtlsDto.getBranchCompanyMapId()!=null) {
				List<Integer> companyBranchIds = new ArrayList<>();
				activeCount = 0; 
				inactiveCount = 0;
				companyBranchIds.add(customerDtlsDto.getBranchCompanyMapId());
				listOfBranchCustomersMap = this.customerService
						.getAllApplicableCustomersCountForDashboard(companyBranchIds, this.getMetaInfo());
				if (listOfBranchCustomersMap != null && !listOfBranchCustomersMap.isEmpty()) {
					for (RlmsBranchCustomerMap branchCustomerMap : listOfBranchCustomersMap) {

						if (branchCustomerMap.getCustomerMaster().getActiveFlag() == 1) {
							activeCount = activeCount + 1;
						} else {
							inactiveCount = inactiveCount + 1;
						}
					}
					CustomerCountDtls countDtls = new CustomerCountDtls();
					countDtls.setCustomerCount(listOfBranchCustomersMap.size());
					countDtls.setActiveFlagCount(activeCount);
					countDtls.setInactiveFlagCount(inactiveCount);
					listOfCustomersCount.add(countDtls);
				}
			}
			else {
			listOfAllBranches = this.companyService.getAllBranches(customerDtlsDto.getCompanyId());
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				activeCount = 0; 
				inactiveCount = 0;
				List<Integer> companyBranchIds = new ArrayList<>();
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
				listOfBranchCustomersMap = this.customerService
						.getAllApplicableCustomersCountForDashboard(companyBranchIds, this.getMetaInfo());
				if (listOfBranchCustomersMap != null && !listOfBranchCustomersMap.isEmpty()) {
					for (RlmsBranchCustomerMap branchCustomerMap : listOfBranchCustomersMap) {

						if (branchCustomerMap.getCustomerMaster().getActiveFlag() == 1) {
							activeCount = activeCount + 1;
						} else {
							inactiveCount = inactiveCount + 1;
						}
					}
					CustomerCountDtls countDtls = new CustomerCountDtls();
					countDtls.setBranchName(companyBranchMap.getRlmsBranchMaster().getBranchName());
					countDtls.setCity(companyBranchMap.getRlmsBranchMaster().getCity());
					countDtls.setCustomerCount(listOfBranchCustomersMap.size());
					countDtls.setActiveFlagCount(activeCount);
					countDtls.setInactiveFlagCount(inactiveCount);
					listOfCustomersCount.add(countDtls);
				}
			}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfCustomersCount;
	}

	@RequestMapping(value = "/getAllCompanyDetailsForDashboard", method = RequestMethod.POST)
	public @ResponseBody List<CompanyDtlsDTO> getAllCompanyDetailsForDashboard() throws RunTimeException {
		List<CompanyDtlsDTO> listOfApplicableCompaniesDetails = null;
		try {
			logger.info("Method :: getAllCompanyDetails");
			listOfApplicableCompaniesDetails = this.companyService.getAllCompanyDetailsForDashboard(this.getMetaInfo());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfApplicableCompaniesDetails;
	}
	@RequestMapping(value = "/getAllBranchesForDashboard", method = RequestMethod.POST)
	public @ResponseBody List<RlmsCompanyBranchMapDtls> getAllBranchesForDashboard(
			@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException {
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		try {
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.dashboardService.getAllBranchesForDashBoard(companyDtlsDTO.getCompanyId());

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfAllBranches;
	}
	@RequestMapping(value = "/getListOfBranchDtlsForDashboard", method = RequestMethod.POST)
	public @ResponseBody List<BranchDtlsDto> getListOfBranchDtls(@RequestBody BranchDtlsDto dto)
			throws RunTimeException {
		List<BranchDtlsDto> listOfBranches = null;
		try {
			logger.info("Method :: getListOfBranchDtls");
			listOfBranches = this.dashboardService.getListOfBranchDtlsForDashboard(dto.getCompanyId(),
					this.getMetaInfo());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfBranches;
	}

	@RequestMapping(value = "/getListOfBranchCountDtlsForDashboard", method = RequestMethod.POST)
	public @ResponseBody Set<BranchCountDtls> getListOfBranchCountDtlsForDashboard(@RequestBody BranchDtlsDto dto)
			throws RunTimeException {
		Set<BranchCountDtls> branchCountDtls = null;
		try {
			logger.info("Method :: getListOfBranchDtls");
			branchCountDtls = this.dashboardService.getListOfBranchCountDtlsForDashboard(dto.getCompanyId(),
					this.getMetaInfo());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return branchCountDtls;
	}
	
	@RequestMapping(value = "/getListOfAmcServiceCalls", method = RequestMethod.POST)
	public @ResponseBody List<ComplaintsDto> getListOfAmcServiceCalls(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsDto> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchMapIds = new ArrayList<>();
		listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}
		List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchMapIds);
		List<Integer> liftCustomerMapIds = new ArrayList<>();
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
				liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
			}
		}
		dto.setListOfLiftCustoMapId(liftCustomerMapIds);
		try {
			logger.info("Method :: getListOfComplaints");
			listOfComplaints = this.dashboardService.getListOfComplaintsBy(dto);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return listOfComplaints;
	}
	@RequestMapping(value = "/getEventCountForLift", method = RequestMethod.POST)
	public @ResponseBody List<EventCountDtls> getEventCountForLift(@RequestBody EventDtlsDto dto)
			throws RunTimeException {
		List<EventCountDtls> eventCountDtls = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			if(dto.getBranchCompanyMapId()!=null) {
				companyBranchIds.add(dto.getBranchCompanyMapId());
			}
			else {
			listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
			if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}}
			}
			List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchIds);
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			if(allCustomersForBranch!=null && !allCustomersForBranch.isEmpty()) {
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dtoTemp = new LiftDtlsDto();
				dtoTemp.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoTemp);
				if(list!=null && !list.isEmpty()) {
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
				}
				}
			}
			eventCountDtls = this.dashboardService.getEventCountDetails(liftCustomerMapIds, this.getMetaInfo());
		} }catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return eventCountDtls;
	}
	
	
	@RequestMapping(value = "/getTodaysEventCountForLift", method = RequestMethod.POST)
	public @ResponseBody List<EventCountDtls> getTodaysEventCountForLift(@RequestBody EventDtlsDto dto)
			throws RunTimeException {
		List<EventCountDtls> eventCountDtls = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			if(dto.getBranchCompanyMapId()!=null) {
				companyBranchIds.add(dto.getBranchCompanyMapId());
			}
			else {
			listOfAllBranches = this.companyService.getAllBranches(dto.getCompanyId());
			if(listOfAllBranches!=null && !listOfAllBranches.isEmpty()) {
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}}
			}
			List<CustomerDtlsDto> allCustomersForBranch = dashboardService.getAllCustomersForBranch(companyBranchIds);
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			if(allCustomersForBranch!=null && !allCustomersForBranch.isEmpty()) {
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dtoTemp = new LiftDtlsDto();
				dtoTemp.setBranchCustomerMapId(customerDtlsDto.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService.getAllLiftsForBranchsOrCustomer(dtoTemp);
				if(list!=null && !list.isEmpty()) {
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
				}
				}
			}
			eventCountDtls = this.dashboardService.getTodaysEventCountDetails(liftCustomerMapIds, this.getMetaInfo());
		} }catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
		}
		return eventCountDtls;
	}
	
	@RequestMapping(value = "/getUnidentifiedImeiEventCount", method = RequestMethod.POST)
	public @ResponseBody List<RlmsEventDtls> getUnidentifiedImeiEventCount(@RequestBody int companyId)
			throws RunTimeException {
			List<RlmsEventDtls> eventDtls = null;
	
			logger.info("Method :: getAllBranchesForCompany");
			eventDtls = this.dashboardService.getUnidentifiedEventCountDetails();
	
			return eventDtls;
	}
}
