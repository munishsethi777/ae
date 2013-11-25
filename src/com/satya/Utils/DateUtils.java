package com.satya.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static String getGridDateFormat(Date date){
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh.mm a");
		String reportDate = df.format(date);
		return reportDate;
	}
}
