package com.rights.batches;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.rlms.service.AMCMonitorService;

@Configuration
@EnableScheduling
public class AMCMonitor {
	private static final Logger logger = Logger.getLogger(AMCMonitor.class);

	@Autowired
  AMCMonitorService  aMCMonitorService;
	
	//@Scheduled(cron="0 28 13 * * ?")
   //@Scheduled(fixedRate =60000)
	@Scheduled(cron="0 57 10 * * ?")
	 public void schedule() {
		logger.debug("Batch start");
		 try {
			 aMCMonitorService.getAllAMCDtlsAndUpdateStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
