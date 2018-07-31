package com.rights.batches;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.rlms.service.AMCMonitorService;
import com.rlms.service.ReportService;

@Configuration
@EnableScheduling
public class ScheduleTesting {
	private static final Logger logger = Logger.getLogger(ScheduleTesting.class);

	@Autowired
	private ReportService reportService;

	@Autowired
  AMCMonitorService  aMCMonitorService;
	
	//@Scheduled(cron="0 28 13 * * ?")
   // @Scheduled(fixedRate =60000)
	 @Scheduled(cron="0 01 00 * * ?")
	 public void schedule() {
		logger.debug("Batch start");
		 try {
			 aMCMonitorService.getAllAMCDtlsAndUpdateStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void executeAMCBatch() throws UnsupportedEncodingException{
		this.reportService.changeStatusToAMCExpiryAndNotifyUser();
		this.reportService.changeStatusToAMCRenewalAndNotifyUser();
	}
}
