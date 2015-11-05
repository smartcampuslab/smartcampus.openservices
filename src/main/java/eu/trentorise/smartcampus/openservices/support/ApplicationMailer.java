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
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Service mailService for sending email with configured properties in file
 * root-context.xml.
 * 
 * @author Giulia Canobbio
 * 
 */
@Service("mailService")
public class ApplicationMailer {
	/**
	 * Instance of {@link MailSender}
	 */
	@Autowired
	private MailSender mailSender;

	@Autowired
	private Environment env;

	/**
	 * Set mail sender.
	 * 
	 * @param mailSender
	 *            : instance of {@link MailSender}
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Send an email with all input parameters.
	 * 
	 * @param from
	 *            : String
	 * @param to
	 *            : String
	 * @param subject
	 *            : String
	 * @param msg
	 *            : String
	 */
	public void sendMail(String from, String to, String subject, String msg) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);
	}

	/**
	 * Send an email with all input parameters. It takes the from address from
	 * global configuration.
	 * 
	 * @param to
	 *            : String
	 * @param subject
	 *            : String
	 * @param msg
	 *            : String
	 */

	public void sendMail(String to, String subject, String msg) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(env.getProperty("email.username", "openservice"));
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);
	}
}
