package com.rlms.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.EmailTemplateEnum;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.XMPPServerDetails;
import com.rlms.dao.EmailDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.model.EmailTemplate;
import com.rlms.model.RlmsUserRoles;
import com.rlms.utils.PropertyUtils;
import com.telesist.email.EmailService;
import com.telesist.email.MailDTO;
import com.telesist.xmpp.AndroidNotificationService;
import com.telesist.xmpp.FCMMessaging;


@Service("MessagingService")
public class MessagingServiceImpl implements MessagingService{

	private static final Logger log = Logger.getLogger(MessagingServiceImpl.class);
	
	 
	@Resource(name = "emailDao")
	private EmailDao emailDao;
	 
	@Resource(name = "emailService")
	private EmailService emailService;
	 
	@Resource(name = "androidNotificationService")
	private AndroidNotificationService androidNotificationService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public EmailTemplate getEmailTemplate(Integer templateId){
		return this.emailDao.getEmailTemplate(templateId);
	}
	
	
	/*public String replaceDyanamicValue(List<String> listOfDyanamicValues, String template){
		int paramCount = 1;
		String dynamictemplate = template;
		for (String param : listOfDyanamicValues) {
		template = dynamictemplate;
		String patternString = "%param" + paramCount + "%";

		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

		       Matcher matcher = pattern.matcher(template);
		       
		       if(matcher.find()){
		       	dynamictemplate  = matcher.replaceFirst(param);	
		       	
		       }
		       
		       paramCount++;
		}

		return dynamictemplate;
		}
	*/
	/*public void sendEmail(MailDTO mailDto) throws UnsupportedEncodingException {
	      // Recipient's email ID needs to be mentioned.
	      List<String> to =mailDto.getToList();

	      // Sender's email ID needs to be mentioned
	      String from = mailDto.getFrom();
	      final String username = "99transporters.dev@gmail.com";//change accordingly
	      final String password = "Abcd@12345";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = mailDto.getSmtpHost();

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", "smtp.gmail.com");
	      props.put("mail.smtp.port", "587");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from,mailDto.getDisplayName()));
		   

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,this.convertToListOfIntrntAddrss(to));
		   
		   message.setRecipients(Message.RecipientType.CC,this.convertToListOfIntrntAddrss(mailDto.getCcList()));
		   
		   

		   // Set Subject: header field
		   message.setSubject(mailDto.getSubject());

		   // Send the actual HTML message, as big as you like
		   message.setContent(
	              mailDto.getContent(),
	             "text/html");

		   // Send message
		   Transport.send(message);

		   System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
		   e.printStackTrace();
		   throw new RuntimeException(e);
	      }
	   }*/
	
	/*private InternetAddress[] convertToListOfIntrntAddrss(List<String> listOfEmailId) throws AddressException{
		 InternetAddress[] recipientAddress = new InternetAddress[listOfEmailId.size()];
		 int counter = 0;
		 for (String recipient : listOfEmailId) {
		     recipientAddress[counter] = new InternetAddress(recipient.trim());
		     counter++;
		 }
		 return recipientAddress;
	 }*/
	
	/*public MailDTO constructMailDto(List<String> toList, String subject, String content){
		
		String from = "sanket.tagalpallewar@gmail.com";
		
		List<String> ccList = new ArrayList<String>();
		ccList.add("sanket.tagalpallewar@gmail.com");
		
		String displayName =  "RLMS";
		
		MailDTO mailDto = new MailDTO();
		mailDto.setCcList(ccList);
		mailDto.setToList(toList);
		mailDto.setContent(content);
		mailDto.setSubject(subject);
		mailDto.setFrom(from);
		mailDto.setDisplayName(displayName);
		return mailDto;
	}*/
	
	public void sendAssgnRoleEmail(String userRoleId, RlmsUserRoles userRole) throws UnsupportedEncodingException{
		EmailTemplate emailTemplate = this.getEmailTemplate(EmailTemplateEnum.USER_ROLE_ASSIGNED.getTemplateId());
		List<String> toList = new ArrayList<String>();
		toList.add(userRole.getRlmsUserMaster().getEmailId());
		
		List<String> listOfDyanamicValues = new ArrayList<String>();
		listOfDyanamicValues.add(userRoleId.toString());
		
		String content = this.emailService.replaceDyanamicValue(listOfDyanamicValues, emailTemplate.getEmailContent());
		
		emailTemplate.setEmailContent(content);
		
		MailDTO dto = this.emailService.constructMailDto(toList, emailTemplate.getEmailSubject(), emailTemplate.getEmailContent(), "sanket.tagalpallewar@gmail.com", toList);
		
		log.debug(content);
		this.emailService.sendEmail(dto);
	}
	
	public void sendAMCMail(List<String> listOfDyanamicValues, List<String> toList, Integer mailTemplateId) throws UnsupportedEncodingException{
		
		EmailTemplate emailTemplate = this.getEmailTemplate(mailTemplateId);
		
		String content = this.emailService.replaceDyanamicValue(listOfDyanamicValues, emailTemplate.getEmailContent());
		
		emailTemplate.setEmailContent(content);
		
		MailDTO dto = this.emailService.constructMailDto(toList, emailTemplate.getEmailSubject(), emailTemplate.getEmailContent(), "sanket.tagalpallewar@gmail.com", toList);
		
		this.emailService.sendEmail(dto);
	}
	
	public String encryptToByteArr(Integer id) throws InvalidKeyException, Exception{
		return this.emailService.encryptToByteArr(id.toString());
	}
	
	public String decryptToInteger(String data) throws InvalidKeyException, Exception{
		return this.emailService.decryptToInteger(data);
	}
	
	public String encrypt( String value) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        
		 	String key = "Bar12345Bar12345"; // 128 bit key
	        String initVector = "RandomInitVector"; // 16 bytes IV
	        
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String encrypted) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
    		String key = "Bar12345Bar12345"; // 128 bit key
    		String initVector = "RandomInitVector"; // 16 bytes IV
         
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
    }
    
	public void sendNotification(String regId, String message, Map<String, String> dataPayload, String fcmProjectSenderId, String fcmServerKey, String messageId) throws SmackException, IOException, RunTimeException{
		FCMMessaging fcmMessaging = new FCMMessaging();
		try{
			fcmMessaging.sendMessage(regId, message, fcmProjectSenderId, fcmServerKey, messageId);
			log.debug("Notification sent succesfully.");
		}catch(Exception e){
			log.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.PUSH_NOTIFICATION_FAILED.getMessage()));
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendUserNotification(String appRegId, JSONObject dataPayload) throws SmackException, IOException{
	//	String fcmProjectSenderId = PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_PROJECT_SENDER_ID.getMessage());
	//	String fcmServerKey = PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_SERVER_KEY.getMessage());
		String fcmApiUrl =PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_API_URL.getMessage());
		String authKey =PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_USER_APP_AUTH_KEY.getMessage());
		JSONObject json = new JSONObject();
    	try {
				json.put("to",appRegId);
				json.put("notification", dataPayload);
				json.put("priority", "high");
			//	json.put("data",jsonObject);
				json.put("content_available", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			pushFCMNotification(json,fcmApiUrl,authKey);
		//	this.androidNotificationService.sendNotification(appRegId, dataPayload, fcmProjectSenderId, fcmServerKey);
	}
	

	@Transactional(propagation = Propagation.REQUIRED)
	public void sendTechnicianNotification(String appRegId, JSONObject dataPayload) throws SmackException, IOException{
	//	String fcmProjectSenderId = PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_PROJECT_SENDER_ID.getMessage());
	//	String fcmServerKey = PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_SERVER_KEY.getMessage());
		String fcmApiUrl =PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_API_URL.getMessage());
		String authKey =PropertyUtils.getPrpertyFromContext(XMPPServerDetails.FCM_TECH_APP_AUTH_KEY.getMessage());
		JSONObject json = new JSONObject();
    	try {
				json.put("to",appRegId);
				json.put("notification", dataPayload);
				json.put("priority", "high");
			//	json.put("data",jsonObject);
				json.put("content_available", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			pushFCMNotification(json,fcmApiUrl,authKey);
	
	//	this.androidNotificationService.sendNotification(appRegId, dataPayload, fcmProjectSenderId, fcmServerKey);
	}
	
	public static void pushFCMNotification(JSONObject json,String fcmApiUrl,String authKey) {
		log.trace("::start::FirebaseUtil:::::pushFCMNotification:::");
		try {

			URL url = new URL(fcmApiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key="+authKey);
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			log.debug("Json Sending To User Mobile Phones" + json.toString());
			System.out.println(json.toString());
			wr.write(json.toString());
			log.info("Write json.string");
			wr.flush();
			log.info("Flush writer");
			conn.getInputStream();
			log.info("conn.getInputStream()");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("::Error::::pushFCMNotification:::", e);
		}
	}
}
