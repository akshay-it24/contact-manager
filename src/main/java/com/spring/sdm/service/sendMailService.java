package com.spring.sdm.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class sendMailService {

	public boolean sendEmail(String message, String subject, String to) {
		
		String from="savaniraj307@gmail.com";
		boolean flag=false;
	
		Properties properties = System.getProperties();
			
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("savaniraj307@gmail.com","180280116020");
			}
			
		});
		
		session.setDebug(true);
		MimeMessage m = new MimeMessage(session);
		
		 try {
			 m.setFrom(from);
			 m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			 m.setSubject(subject);
			 m.setText(message);
			
			 Transport.send(m);
			 flag=true;
			 System.out.println("successfully sent");
			 
		} catch (Exception e) {
			// TODO: handle exception
		} 
		return flag;
	}
}
