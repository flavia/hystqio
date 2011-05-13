/*
Copyright (C) 2010 Viral Patel
Copyright (C) 2011 9Apps.net

This file is part of hystqio.

hystqio is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

hystqio is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with hystqio. If not, see <http://www.gnu.org/licenses/>.
*/

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
