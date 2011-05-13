package net.nineapps.hystqio.util;

import java.util.Date;
import java.util.Random;

public class ShortyUtils {
	
	public static String base48Encode(Long no) {
		Double num = Double.valueOf(no);
		String charSet = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
		Integer length = charSet.length();
		String encodeString = new String();
		while(num > length) {
			encodeString = charSet.charAt(num.intValue() % length)+encodeString;
			 num = Math.ceil(new Double(num / length) - 1) ;
		}
		encodeString = charSet.charAt(num.intValue())+encodeString;
		
		return encodeString;
	}
	
	public static String getShortCodeFromURL(String URL) {
		
		int index=0;
		for(index=URL.length()-1; index>=0 && URL.charAt(index)!= '/' ;index--);
		String shortCode = URL.substring(index+1);
		
		return shortCode;
	}
	
}
