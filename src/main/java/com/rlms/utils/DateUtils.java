package com.rlms.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

	public static String convertDateToStringWithoutTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String s = formatter.format(inputDate);
		return s;
	}
	
	public static String convertDateToStringWithTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:MM:ss a");
		String s = formatter.format(inputDate);
		return s;
	}
	
	public static String convertDateTimestampToStringWithTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		String s = formatter.format(inputDate);
		return s;
	}
	
	public static Date convertStringToDateWithoutTime(String inputDate) throws ParseException{
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date s = (Date) formatter.parseObject(inputDate);
		return s;
	}
	
	public static Date convertStringToDateWithTime(String inputDate) throws ParseException{
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		Date s = (Date) formatter.parseObject(inputDate);
		return s;
	}
	
	public static Date convertStringToDateWithTimezone(String inputDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(inputDate);
		return d;
	}
	
	
	
	public static Date addDaysToDate(Date date, int days){
		Calendar c = Calendar.getInstance();
		c.setTime(date); // Now use today date.
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
	public static boolean isBeforeToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(beforeDate.before(afterDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isAfterToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(afterDate.after(beforeDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isBeforeOrEqualToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(beforeDate.before(afterDate)){
				return true;
			}else if(beforeDate.equals(afterDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isAfterOrEqualTo(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(afterDate.after(beforeDate)){
				return true;
			}else if(afterDate.equals(beforeDate)){
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	
	public static Integer daysBetween(Date d1, Date d2){
		 return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public static Long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	 public static String convertTimeIntoDaysHrMin(Long duration, TimeUnit timeUnit)
	 {
		 String durationInDaysHrMin;
		 Long days = timeUnit.toDays(duration);
		 Long hrs = timeUnit.toHours(duration) - (days * 24);
		 Long mins = timeUnit.toMinutes(duration)- (timeUnit.toHours(duration)*60);
		 if(days > 0)
		 {
		   durationInDaysHrMin = days.toString() + " days " + hrs.toString() + " hours " + mins.toString() + " mins";
		 }else if(hrs > 0)
		 {
			 durationInDaysHrMin = hrs.toString() + " hours " + mins.toString() + " mins";
		 }else{
			 durationInDaysHrMin = mins.toString() + " mins";
		 }
		 return durationInDaysHrMin;
		
	 }
}
