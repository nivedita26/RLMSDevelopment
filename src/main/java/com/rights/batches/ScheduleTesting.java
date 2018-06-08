package com.rights.batches;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.rlms.service.AMCMonitorService;
import com.rlms.service.ReportService;

@Configuration
@EnableScheduling
public class ScheduleTesting {

	@Autowired
	private ReportService reportService;

	@Autowired
  AMCMonitorService  aMCMonitorService;
	@Scheduled(cron="0 0 12 * * ?")
	//@Scheduled(cron="0 28 13 * * ?")
	//@Scheduled(fixedRate =6000)
	 public void schedule() {
		System.out.println("Batch start");
		 try {
			 aMCMonitorService.getAllAMCDtlsAndUpdateStatus();
			 this.reportService.changeStatusToAMCExpiryAndNotifyUser();
			 // this.executeAMCBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void executeAMCBatch() throws UnsupportedEncodingException{
		
		this.reportService.changeStatusToAMCExpiryAndNotifyUser();
		this.reportService.changeStatusToAMCRenewalAndNotifyUser();
	}
}
