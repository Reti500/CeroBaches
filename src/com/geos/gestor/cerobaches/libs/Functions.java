package com.geos.gestor.cerobaches.libs;

import android.annotation.SuppressLint;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {
	public Functions(){
		super();
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String linuxToGTM(String date){
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(date);
		
		String fechaFin = "";
		
		while (m.find()) {
			fechaFin = m.group();
		}
		
		Date d = new Date(Long.parseLong(fechaFin));
//		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");
		f.setTimeZone(TimeZone.getTimeZone("GTM"));
		String s = f.format(d);
		
		return s;
	}
	
	public static String linuxFormat(String date){
		return date.replaceAll("\\D+","");
	}
}
