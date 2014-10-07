package com.qait.happyhours.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.qait.happyhours.exception.HappyHoursException;

/**
 * This class defines a utility to read the property file of constants.
 */
public class HappyHoursPropertiesFileReaderUtil {

	private static final Logger logger = Logger
			.getLogger(HappyHoursPropertiesFileReaderUtil.class);
	private static Properties properties = System.getProperties();

	public static String getPropertyValue(String key) {
		try {
			properties.load(HappyHoursPropertiesFileReaderUtil.class
					.getClassLoader().getResourceAsStream(
							"properties/message.properties"));
		} catch (FileNotFoundException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		} catch (IOException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		}
		return properties.getProperty(key) != null ? properties
				.getProperty(key).trim() : "";
	}

	/*
	 * Get property values for Email
	 */

	public static String getEmailProperty(String key) {
		try {
			properties.load(HappyHoursPropertiesFileReaderUtil.class
					.getClassLoader().getResourceAsStream(
							"properties/mailserver.properties"));
		} catch (FileNotFoundException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		} catch (IOException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		}
		return properties.getProperty(key) != null ? properties
				.getProperty(key).trim() : "";
	}

	/*
	 * Get property value for velocity templates
	 */
	public static String getVelocityTemplateProperties(String key) {
		try {
			properties.load(HappyHoursPropertiesFileReaderUtil.class
					.getClassLoader().getResourceAsStream(
							"properties/velocity.properties"));
		} catch (FileNotFoundException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		} catch (IOException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		}
		return properties.getProperty(key) != null ? properties
				.getProperty(key).trim() : "";
	}

	public static String getApplicationProperty(String key) {
		try {

			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("win") >= 0) {
				properties.load(HappyHoursPropertiesFileReaderUtil.class
						.getClassLoader().getResourceAsStream(
								"properties/application.properties"));
			} else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0
					|| OS.indexOf("aix") > 0) {
				properties.load(HappyHoursPropertiesFileReaderUtil.class
						.getClassLoader().getResourceAsStream(
								"properties/application_unix.properties"));
			}
		} catch (FileNotFoundException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		} catch (IOException e) {
			logger.fatal(HappyHoursUtil.getExceptionDescriptionString(e));
			throw new HappyHoursException();
		}
		return properties.getProperty(key) != null ? properties
				.getProperty(key).trim() : "";
	}

}
