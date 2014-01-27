/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
/**
 * Service mailService for sending email
 * with configured properties in file root-context.xml
 * 
 * @author Giulia Canobbio
 *
 */
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
