package net.nineapps.hystqio.util;

import java.util.Date;
import java.util.Random;

public class HystqioUtils {

	private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final Random random = new Random(new Date().getTime());
	
	/**
	 * Randomly generates a short code of 6 characters
	 * @return
	 */
	public static String generateShortCode() {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<6; i++) {
			int r = random.nextInt(CHARSET.length());
			buffer.append(CHARSET.charAt(r));
		}
		return buffer.toString();
	}
	
}
