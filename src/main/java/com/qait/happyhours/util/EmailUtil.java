package com.qait.happyhours.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.qait.happyhours.dto.SendPasswordDTO;
import com.qait.happyhours.enums.EmailType;

public class EmailUtil {
	
	private static final Logger logger = Logger.getLogger(EmailUtil.class);
	
	private static VelocityEngine velocityEngine;
	private static String templateFileFolder;

	final static String senderEmailID = HappyHoursPropertiesFileReaderUtil
			.getEmailProperty("mail.username");
	final static String senderPassword = HappyHoursPropertiesFileReaderUtil
			.getEmailProperty("mail.password");
	
	public static boolean sendEmail(String subject, String recipeintEmail, EmailType emailtype, Object templateModel) {
		
		logger.info("Sending Email to "+recipeintEmail);
		
		boolean isEmailSent = true;
		Map<String, Object> model = createTemplateModel(emailtype, templateModel);
		String templateFilePath = getTemplateFilePath(emailtype);
		
		String messageBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateFilePath, model);	
		
		final String emailSMTPserver = HappyHoursPropertiesFileReaderUtil.getEmailProperty("mail.host");
		final String emailServerPort = HappyHoursPropertiesFileReaderUtil.getEmailProperty("mail.port");

		Properties props = new Properties();
		props.put("mail.smtp.user", senderEmailID);
		props.put("mail.smtp.host", emailSMTPserver);
		props.put("mail.smtp.port", emailServerPort);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.starttls.enable", "true");

		try {
				Authenticator auth = new SMTPAuthenticator();
				javax.mail.Session session = javax.mail.Session.getInstance(props,auth);
	
				MimeMessage message = new MimeMessage(session);
				message.setSubject(subject);
				message.setFrom(new InternetAddress(senderEmailID));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipeintEmail));
				message.setSentDate(new Date());
				message.setText(messageBody);
			
				Transport.send(message);
				
		} catch (MessagingException e) {
			isEmailSent = false;
			logger.error("Email Sending failed to "+recipeintEmail+" "+HappyHoursUtil.getExceptionDescriptionString(e));
		} catch (Exception mex) {
			isEmailSent = false;
			if(templateFilePath.length() <= 0) {
				logger.error("Template file not found fot email type "+emailtype);
			} else {
				logger.error("Email Sending failed to "+recipeintEmail+" "+HappyHoursUtil.getExceptionDescriptionString(mex));
			}
		}
		if(isEmailSent) {
			logger.info("Email sent to "+recipeintEmail);
		}
		
		return isEmailSent;
	}
	
	private static class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(senderEmailID, senderPassword);
		}
	}

	public static Map<String, Object> createTemplateModel(EmailType emailType, Object templateModel) {
		logger.info("Inside createTemplateModel......");
		Map<String, Object> model = new HashMap<String, Object>();
		if (emailType == EmailType.SEND_PASSWORD) {
			SendPasswordDTO sendPasswordDTO = (SendPasswordDTO)templateModel;
			model.put("sendPasswordDTO", sendPasswordDTO);
		}
		return model;
	}

	public static String getTemplateFilePath(EmailType emailType) {
		logger.info("Inside getTemplateFilePath......");
		StringBuffer templateFilePath = new StringBuffer(templateFileFolder);
		templateFilePath.append("/");
		if (emailType == EmailType.SEND_PASSWORD) {
			templateFilePath.append(HappyHoursPropertiesFileReaderUtil.getVelocityTemplateProperties("send.password.template.name"));
		}
		templateFilePath.append(".vm");
		logger.info("Exiting getTemplateFilePath......");
		return templateFilePath.toString();
	}

	
	public static VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public static void setVelocityEngine(VelocityEngine velocityEngine) {
		EmailUtil.velocityEngine = velocityEngine;
	}

	public static String getTemplateFileFolder() {
		return templateFileFolder;
	}

	public static void setTemplateFileFolder(String templateFileFolder) {
		EmailUtil.templateFileFolder = templateFileFolder;
	}
}
