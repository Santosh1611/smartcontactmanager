package com.smart.service;



import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(String subject, String message, String to) {

		boolean f = false;

		String from = "santghawate1611@gmail.com";



		SimpleMailMessage m = new SimpleMailMessage();
		

		try {

			m.setFrom(from);
			m.setTo(to);
			m.setSubject(subject);
			m.setText(message);


			mailSender.send(m);
			
			f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
}
