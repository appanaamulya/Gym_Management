package com.gym;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	static Properties emailProperties;
	static Session mailSession;
	static MimeMessage emailMessage;
	public static void setMailServerProperties(String port) {

		//String emailPort = "587";// gmail's smtp port

				emailProperties = System.getProperties();
				emailProperties.put("mail.smtp.port", port);
				emailProperties.put("mail.smtp.auth", "true");
				emailProperties.put("mail.smtp.ssl.trust", "*");
				emailProperties.put("mail.smtp.starttls.enable", "true");

			}
	public static void createEmailMessage(String rec,String body)
			throws AddressException, MessagingException {
//String[] toEmails = { "aamulya18@gmail.com" };
//String [] toEmails=arr;
		String emailSubject = "CrossFit GYM";
		String emailBody =body;

		mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);

//		for (int i = 0; i < arr.length; i++) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(rec));
//		}

		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailBody, "text/html");
//for a html email
//emailMessage.setText(emailBody);// for a text email

	}

	public static void sendEmail(/* String user, String pass, String emailHost */)
			throws AddressException, MessagingException {

String emailHost = "smtp.gmail.com";
String fromUser = "acmsamazon";//just the id alone without @gmail.com
String fromUserEmailPassword = "Asaramal1";
		Transport transport = mailSession.getTransport("smtp");

		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}
	
}
