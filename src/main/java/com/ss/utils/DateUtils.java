package com.ss.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final String format = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat sdf = new SimpleDateFormat(format);

	public static String format(Date date) {
		return sdf.format(date);
	}

	public static Date parse(String s) {
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
