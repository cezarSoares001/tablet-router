package com.murrow.support;

import android.util.Base64;

import java.util.Arrays;
import java.util.Calendar;

public class Utilities {

	/**
	 * This static variable is the value for the number of seconds in the current time since some time back in the 70's.
	 * It's used to calculate the number of seconds since the program began by the method which follows.
	 */
	public static long baseDateSeconds = Calendar.getInstance().getTimeInMillis()/1000;

    public static String padHex(String hex, int len)
    {
        return prependString(hex, len).replaceAll(" ", "0");
    }

    public static String bytesToString(byte[] bytes)
    {
        return new String(Base64.decode(bytes, Base64.DEFAULT));
    }

    public static byte[] stringToBytes(String string)
    {
        return Base64.encode(string.getBytes(), Base64.DEFAULT);
    }

    public static String prependString(String str, int len)
    {
        try
        {
            char[] tmp = new char[len - str.length()];
            Arrays.fill(tmp, ' ');
            return new String(tmp) + str;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static Integer getNetworkFromInteger(Integer addr)
    {
        return Integer.valueOf(Integer.toHexString(addr).substring(0, 1), 16);
    }

	/**
	 * This method returns the number of seconds since the program began.  
	 * @return
	 */
	public static int getTimeInSeconds()
	{
		return (int) (Calendar.getInstance().getTimeInMillis()/1000 - baseDateSeconds); 
	}
}
