package com.openserviceproject.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service("mailService")
public class ApplicationMailer {
	
	@Autowired
	private MailSender mailSender;
	@Autowired
	private SimpleMailMessage preConfiguredMessage;
	
	/**
	 * Send a message with input parameters
	 * @param to
	 * @param subject
	 * @param text
	 */
	public void sendMail(String to, String subject, String text){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}
	
	/**
	 * Send a pre-configured message
	 * @param message
	 */
	public void sendPreConfiguredMail(String message){
		SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
		mailMessage.setText(message);
		mailSender.send(mailMessage);
	}
}
