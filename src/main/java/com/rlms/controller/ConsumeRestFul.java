package com.rlms.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import com.rlms.utils.DateUtils;

public class ConsumeRestFul {

	// http://localhost:8080/RESTfulExample/json/product/post
		public static void main(String[] args) {
			//submitVisitDetails();
			//getAllComplaintsByTech();
			//uploadLiftPhoto();
			//updateLiftDetails();
			//addNewComplaint();
		//	getVisitDetails();
			addEvent();
		}
		
		  public static void getAllComplaintsByTech(){
			  try{
			  /* 1.*/ // URL url = new URL("http://139.162.5.222:8000/RLMS/API/getAllComplaintsAssigned"); //(userRoleId 17)*/ 
				//URL url = new URL("http://139.162.5.222:8000/RLMS/API/register/registerMemeberDeviceByMblNo"); 
				/*3. */// URL url = new URL("http://139.162.5.222:8000/RLMS/API/register/registerTechnicianDeviceByMblNo");
				/* 4.*///URL url = new URL("http://localhost:8000/RLMS/API/lift/getAllLiftsForMember");
				 /*5.*/  URL url = new URL("http://139.162.5.222:8000/RLMS/API/complaints/getAllComplaintsByMember");
				//URL url = new URL("http://localhost:8000/RLMS/API/loginIntoApp");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

			/*	2.*///String input = "{\"contactNumber\":\"9420192324\", \"latitude\":\"18.12457898\", \"longitude\":\"72.12457896\", \"appRegId\":\"AAAAGqJGmak:APA91bH5wu5DXT01MIyN2LF0n46WqR0ZXtuTCaV8qHGEe738r-fAfoIGG1ytz_k6oHiFEgo6nX9VSopGXi2qhylnjpXKdh4U-tzGoMIA78QDDqnxIVJQFo56AN1uKrmz0UiLo6_-lb3\", \"address\":\"WAKAD\"}";
				/* 3. */// String input = "{\"contactNumber\":\"9096136234\", \"latitude\":\"18.12457898\", \"longitude\":\"72.12457896\", \"appRegId\":\"AAAAGqJGmak:APA91bH5wu5DXT01MIyN2LF0n46WqR0ZXtuTCaV8qHGEe738r-fAfoIGG1ytz_k6oHiFEgo6nX9VSopGXi2qhylnjpXKdh4U-tzGoMIA78QDDqnxIVJQFo56AN1uKrmz0UiLo6_-lb3b\", \"address\":\"WAKAD\"}";
				// String input = "{\"userName\":\"admin\",\"password\":\"rlms1234\"}";
				/*/4 and 5.*/ String input = "{\"memberId\":\"5\"}";
				//String input = "{\"userRoleId\":\"43\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
		  }
		  
		  public static void uploadLiftPhoto(){
			  try{
				 URL url = new URL("http://localhost:9090/RLMS/API/lift/uploadPhoto");
				 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true); 
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				byte[] machinePhotoBytes = null;
				File imgPath=new File("F:\\SpringProject\\RLMS\\src\\main\\java\\com\\rlms\\controller\\APP.jpg");
				machinePhotoBytes=Files.readAllBytes(imgPath.toPath());
				String base64String = Base64.encodeBase64String(machinePhotoBytes);
				String input="{\"liftCustomerMapId\":\"7\",\"photoType\":\"1\",\"machinePhoto\":\""+base64String+"\"}";
				
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
		  }
		  
		  public static void updateLiftDetails(){
			  try{
				 URL url = new URL("http://localhost:9090/RLMS/API/lift/updateLiftDetails");
				 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true); 
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				String input="{\"liftCustomerMapId\":\"7\",\"area\":\"Hadapsar\",\"city\":\"Pune\",\"pinCode\":\"411028\",\"amcAmount\":\"800\"}";
				
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
		  }
		  
		public static void addNewComplaint(){


			  try {

				/* 1. */ URL url = new URL("http://139.162.5.222:8000/RLMS/API/complaint/validateAndRegisterNewComplaint"); //(userRoleId 17)*/ 
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

			
				String input = "{\"liftCustomerMapId\":\"6\",\"registrationType\":\"30\", \"complaintsRemark\":\"not done yet\", \"complaintsTitle\":\"Door Speed\", \"memberId\":\"2\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }

			  //Example Response
			  
			  //Output from Server .... 

/*{"status":true,"response":"Complaint has been registered successfully. Admin will contact you shortly.","jsonElement":null,"jsonArray":null}

			
		} */
		}
		
		public static void getAllLiftsForMemeber(){


			  try {

				/* 1. */ URL url = new URL("http://139.162.5.222:8000/RLMS/API/lift/getAllLiftsForMember"); //(userRoleId 17)*/ 
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

			
				String input = "{\"memberId\":\"3\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }

			  //Example Response
			  
			  //Output from Server .... 

/*{"status":true,"response":"Complaint has been registered successfully. Admin will contact you shortly.","jsonElement":null,"jsonArray":null}

			
		} */
		}
		
		
		public static void submitVisitDetails(){


			  try {

				/* 1. */ URL url = new URL("http://139.162.5.222:8000/RLMS/API/complaint/validateAndSaveSiteVisitDtls"); //(userRoleId 17)*/ 
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

				String fromDate = DateUtils.convertDateToStringWithTime(new Date());
				String toDate = DateUtils.convertDateToStringWithTime(new Date());
			
				String input = "{\"complaintTechMapId\":\"27\",\"userRoleId\":\"43\",\"fromDateDtr\":\"" + fromDate +"\",\"toDateStr\":\"" + toDate + "\",\"remark\":\"not done yet\"}";

//				String input = "{\"complaintTechMapId\":\"3\",\"userRoleId\":\"2\",\"remark\":\"not done yet\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }

			  //Example Response
			  
			//  Output from Server .... 

			  //{"status":true,"response":"Visit details updated successfully.","jsonElement":null,"jsonArray":null}

		}
		
		public static void getVisitDetails(){


			  try {

				/* 1. */ URL url = new URL("http://139.162.5.222:8000/RLMS/API/complaint/getAllVisitsForComplaint"); //(userRoleId 17)*/ 
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
			
				String input = "{\"complaintTechMapId\":\"27\"}";

//				String input = "{\"complaintTechMapId\":\"3\",\"userRoleId\":\"2\",\"remark\":\"not done yet\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }

			  //Example Response
			  
			//  Output from Server .... 

			  //{"status":true,"response":"Visit details updated successfully.","jsonElement":null,"jsonArray":null}

		}
		
		public static void addEvent(){


			  try {

				URL url = new URL("http://localhost:9090/RLMS/API/addEvents"); 
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
			
				String input = "{\"eventType\":\"In\",\"eventDescription\":\"In event created\", \"liftCustomerMapId\":\"1\", \"generatedDateStr\":\""+DateUtils.convertDateToStringWithTime(new Date())+"\", \"generatedBy\":\"1\", \"updatedDateStr\":\""+DateUtils.convertDateToStringWithTime(new Date())+"\", \"updatedBy\":\"1\", \"activeFlag\":\"1\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
		}
}
