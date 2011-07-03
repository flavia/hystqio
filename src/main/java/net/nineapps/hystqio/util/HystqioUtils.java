package net.nineapps.hystqio.util;

import java.util.Random;

public class HystqioUtils {

	private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final Random random = new Random(System.currentTimeMillis());
	
	/**
	 * Randomly generates a short code of 6 characters
	 * @return
	 */
	public static String generateShortCode() {
		StringBuilder buffer = new StringBuilder();
		for(int i=0; i<6; i++) {
			int r = random.nextInt(CHARSET.length());
			buffer.append(CHARSET.charAt(r));
		}
		return buffer.toString();
	}
	
	public static String getShortCodeFromURL(String URL) {
		
		int index=0;
		for(index=URL.length()-1; index>=0 && URL.charAt(index)!= '/' ;index--);
		String shortCode = URL.substring(index+1);
		
		return shortCode;
	}
}
