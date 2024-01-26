package com.dailystudy.dtmsapi.util;

public class UnitUtil {

	public static <T> T cast(Object obj) {
		return (T)obj;
	}
	
	public static int toInt(Object obj) {
		if(obj == null) return 0;
		return (int) Double.parseDouble(obj +"");
	}
	
	public static double toDouble(Object obj) {
		if(obj == null) return 0;
		return Double.parseDouble(obj +"");
	}
	
}
