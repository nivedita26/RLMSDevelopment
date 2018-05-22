package com.rlms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.contract.UserAppDtls;
import com.rlms.dao.LiftDao;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;
import com.rlms.propertyconfiguration.ParameterIndexPropertyConfig;
import com.rlms.propertyconfiguration.PropertyConfiguration;

@Service
public class RlmsLiftEventServiceImpl implements RlmsLiftEventService{

	private static final Logger log = Logger.getLogger(MessagingServiceImpl.class);

	static ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PropertyConfiguration.class);
	static   ParameterIndexPropertyConfig paramIndex= applicationContext.getBean(ParameterIndexPropertyConfig.class);
	
	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private ComplaintsService complaintService;
	
	@Autowired
	private MessagingService messagingService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addEvent(String msgFromContact, String msg) {
		log.debug("inside add Event serviceImpl");
		String lmsMsg = msg;
		String dateStr = null;
		String  timeStr= null;
	    String dateTime =null;
	    RlmsEventDtls eventDtls = null;
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");
		Date date = new Date();
        if(msg.contains("%20")) {
	       lmsMsg  = msg.replaceAll("%20", " ");
	       log.debug("string with replacement of %20"+lmsMsg);
        }
		if(lmsMsg.contains("*") && lmsMsg.contains(";")) {	
			String [] lmsParamArray= lmsMsg.split("[\\,*;]+");
			if(lmsParamArray.length==8 || lmsParamArray.length==9) {
				
				timeStr =lmsParamArray[paramIndex.getMsgTime()];
		        dateStr=lmsParamArray[paramIndex.getMessageDate()];
		        dateTime  =  timeStr.concat(" ").concat(dateStr);
			    RlmsLiftMaster rlmsLiftMaster = liftDao.getLiftIdByImei(lmsParamArray[paramIndex.getImei()]);
				rlmsLiftMaster = liftDao.getLiftIdByImei(lmsParamArray[paramIndex.getImei()]);
				if(rlmsLiftMaster!=null ) {
			 //get rlms lift_customer_map
				 RlmsLiftCustomerMap rlmsLiftCustomerMap = liftDao.getLiftCustomerMapByLiftId(rlmsLiftMaster.getLiftId());
			     if(rlmsLiftCustomerMap!=null) {
			    	 eventDtls = this.constructLmsEventDtls(lmsParamArray,rlmsLiftCustomerMap);
					 try {
						 date = simpleDateFormat.parse(dateTime);
					 } catch (ParseException e) {
						 e.printStackTrace();
					 }
					 eventDtls.setEventDate(date);
					 eventDtls.setFromContact(msgFromContact);
					 if(lmsParamArray.length==9) {
						 String lmsResContactNo = lmsParamArray[paramIndex.getLmsResponseUserContactNo()];
						 eventDtls.setLmsResponseUserContactNo(lmsResContactNo.split(" ")[1]);
					 }
					 this.saveEventDtls(eventDtls,rlmsLiftCustomerMap);
			     }
				     else {
				    	 log.error("lift customer mapping is not found");
				     }
				}
				else {
					log.error("lift id is not found for imei id message="+lmsParamArray);
				}
			}
		}
	}
			
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsEventDtls constructLmsEventDtls(String[] eventParam,RlmsLiftCustomerMap liftCustomerMap){
			
		RlmsEventDtls rlmsEventDtls = new RlmsEventDtls();
		
		rlmsEventDtls.setEventDescription(eventParam[paramIndex.getErrorMsg()]);
        rlmsEventDtls.setEventCode(eventParam[paramIndex.getErrorCode()]);
        String[] floorNo= eventParam[paramIndex.getFloorNo()].split("\\.");
        rlmsEventDtls.setFloorNo(Integer.parseInt(floorNo[1]));
		
        rlmsEventDtls.setEventService(eventParam[paramIndex.getService()]);
        rlmsEventDtls.setImeiId(eventParam[paramIndex.getImei()]); 
		
		String lmsEventType = eventParam[paramIndex.getEventType()];
		
		rlmsEventDtls.setEventType(lmsEventType.split(" ")[1]);
		
		rlmsEventDtls.setRlmsLiftCustomerMap(liftCustomerMap);
		rlmsEventDtls.setGeneratedBy(liftCustomerMap.getBranchCustomerMap().getCustomerMaster().getCustomerId());
		rlmsEventDtls.setUpdatedBy(liftCustomerMap.getBranchCustomerMap().getCustomerMaster().getCustomerId());
		rlmsEventDtls.setActiveFlag(1);
		rlmsEventDtls.setGeneratedDate(new Date());
		rlmsEventDtls.setUpdatedDate(new Date());
		
   return rlmsEventDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveEventDtls(RlmsEventDtls eventDtls,RlmsLiftCustomerMap liftCustomerMap){
			this.liftDao.saveEventDtls(eventDtls);
			//this.sendNotificationsAboutLmsEvent(eventDtls,liftCustomerMap);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNotificationsAboutLmsEvent(RlmsEventDtls eventDtls,RlmsLiftCustomerMap liftCustomerMap){
		 JSONObject  dataPayload = new JSONObject();
		try {
			dataPayload.put("title","LMS Event Generated" );
			dataPayload.put("body",eventDtls.getEventDescription());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		List<UserAppDtls> listOfUsers = complaintService.getRegIdsOfAllApplicableUsers(liftCustomerMap.getLiftCustomerMapId());
		if(listOfUsers !=null && !listOfUsers.isEmpty()) {
			for (UserAppDtls userAppDtls : listOfUsers) {
				try{
						log.debug("sendNotificationsAboutLmsEvent :: Sending notification");
						if(userAppDtls.getAppRegId()!=null) {
							this.messagingService.sendUserNotification(userAppDtls.getAppRegId(), dataPayload);
						log.debug("sendNotificationsAboutLmsEventGeneration :: Notification sent to Id :" + userAppDtls.getAppRegId());
					}				
				}catch(Exception e){
					log.error(ExceptionUtils.getFullStackTrace(e));
				}
			}
		}
		else {
			log.error("user app registration id not found");
		}
	}
}
