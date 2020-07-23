/**
 * 
 */
package com.church.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.extern.slf4j.Slf4j;

/**
 * Sends email notifications to users.
 * 
 * @author pradheep
 *
 */
@Slf4j
public final class EmailNotifyService {

	@Autowired
	private JavaMailSender mailSender;

	public boolean notifyMessage(SimpleMailMessage mailMessage) {
		log.info("Sending an email message:");
		try {
			mailSender.send(mailMessage);
		} catch (Exception err) {
			log.error("Error while sending the email");
			return false;
		}
		return true;
	}	
}
