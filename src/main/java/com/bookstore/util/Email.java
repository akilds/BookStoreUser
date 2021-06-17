package com.bookstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class Email {

	@Autowired
	private JavaMailSender mailSender;
	
	public void sendMail(String receiver,String subject, String text) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("akilrainadesigan@gmail.com");
		message.setTo(receiver);
		message.setText(text);
		message.setSubject(subject);
		
		mailSender.send(message);
	}
}
