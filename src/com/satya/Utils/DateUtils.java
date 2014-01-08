package com.satya.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class DateUtils {
	
	public static String getGridDateFormat(Date date){
		if (date==null){
			return null;
		}
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh.mm a");
		String reportDate = df.format(date);
		return reportDate;
	}
	
	public static Date getDateFromString(String strDate){
		if (strDate == null || strDate == ""){
			return null;
		}
		try{
			Date date= new SimpleDateFormat("dd/MM/yyyy hh.mm a").parse(strDate);
			return date;
		} catch (java.text.ParseException e) {
			return null;
		}
		
		
	}
}
