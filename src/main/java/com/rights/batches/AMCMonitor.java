package com.rights.batches;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rlms.service.ReportService;

public class AMCMonitor {
	
	private static final Logger logger = Logger.getLogger(AMCMonitor.class);
	
	@Autowired
	private ReportService reportService;
	
	public static void main(String arg[])
	{
		System.out.println("Batch start");
		ApplicationContext context = new ClassPathXmlApplicationContext("BatchBeans.xml");
		 logger.debug(context);
		 AMCMonitor amcMonitor = (AMCMonitor) context.getBean("aMCMonitor");
		 try {
			 amcMonitor.executeAMCBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));	 
		}
	}
	
	private void executeAMCBatch() throws UnsupportedEncodingException{
		this.reportService.changeStatusToAMCExpiryAndNotifyUser();
		this.reportService.changeStatusToAMCRenewalAndNotifyUser();
	}
}
