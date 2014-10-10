package com.qait.happyhours.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

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
	
	public static Double distance(Double latitude, Double longitude,
			Double latitude2, Double longitude2) {
		Long R = (long) 6371; // km
		Double dLat = (latitude2 - latitude) * Math.PI / 180;
		Double dLon = (longitude2 - longitude) * Math.PI / 180;
		Double lat1 = latitude * Math.PI / 180;
		Double lat2 = latitude2 * Math.PI / 180;

		Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = (Double) (R * c);
		/* Double m = d * 0.621371; */
		return d;
	}
	
	/**
	 * Method will append server url to image path
	 * 
	 * @param path
	 * @return
	 */
	public static String appendServerUrlToPath(String path) {
		return HappyHoursPropertiesFileReaderUtil.getApplicationProperty("server.url")
				+ "/" + path;
	}
	
	public static String uploadImageOnServer(InputStream in, Long dealID) throws IOException, FileNotFoundException {

		FileOutputStream fOut = null;
		Date date = new Date();

		// Make root folder to save images if not there.
		String rootFolderPath = createRootFolder();
		
		// Make deal folder to save images if not there.
		String dealFolderPath = rootFolderPath + "/" + dealID.toString();
		createDealFolder(dealFolderPath);
		
		String convertedImageName = (new Long(date.getTime())).toString()+ ".jpg";
		
		String storagePath = dealFolderPath + "/" + convertedImageName;
		
		fOut = new FileOutputStream(storagePath);

		byte[] buf = new byte[1024];
		int len;

		while ((len = in.read(buf)) > 0) {
			fOut.write(buf, 0, len);
		}

		in.close();
		fOut.close();

		return convertedImageName;
	}
	
	private static String createRootFolder() {
		String rootFolderPath = HappyHoursPropertiesFileReaderUtil.getApplicationProperty("deal.image.storage.path");
		File rootFolder = new File(rootFolderPath);
		rootFolder.mkdirs();
		return rootFolderPath;
	}
	
	private static File createDealFolder(String dealFolderPath) {
		File dealFolder = new File(dealFolderPath);
		dealFolder.mkdirs();
		return dealFolder;
	}
}
