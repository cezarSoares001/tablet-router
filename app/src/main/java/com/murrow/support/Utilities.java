package com.murrow.support;

import java.util.Calendar;

public class Utilities {

	/**
	 * This static variable is the value for the number of seconds in the current time since some time back in the 70's.
	 * It's used to calculate the number of seconds since the program began by the method which follows.
	 */
	public static long baseDateSeconds = Calendar.getInstance().getTimeInMillis()/1000;
	
	/**
	 * This method returns the number of seconds since the program began.  
	 * @return
	 */
	public static int getTimeInSeconds(){
		return (int) (Calendar.getInstance().getTimeInMillis()/1000 - baseDateSeconds); 
	}
}
