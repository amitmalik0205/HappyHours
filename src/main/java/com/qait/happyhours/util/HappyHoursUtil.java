package com.qait.happyhours.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.log4j.Logger;

public class HappyHoursUtil {

	private static final Logger logger = Logger.getLogger(HappyHoursUtil.class);

	/*
	 * Method returns the stack trace of exception in string format. Used for
	 * logging of exception.
	 */
	public static String getExceptionDescriptionString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}

	public static String getAuthToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}
