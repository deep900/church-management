package com.church.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.church.notify.EmailNotifyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailTaskReminder extends Reminder {

	private String messageContent = "";

	private EmailNotifyService emailMessenger;

	private List<ApplicationUser> assignedUsers = new ArrayList<ApplicationUser>();
	
	@Autowired
	@Qualifier("emailProperties")
	private Properties emailProperties;

	@Override
	public boolean remind() {
		if (canRemindNow() && active) {
			log.info("Reminding the task now ");
			Properties props = new Properties();
			props.put("mail.smtp.auth", emailProperties.get("mail.smtp.auth"));
			props.put("mail.smtp.starttls.enable", emailProperties.get("mail.smtp.starttls.enable"));
			props.put("mail.smtp.host", emailProperties.get("email.host"));
			props.put("mail.smtp.port", emailProperties.get("email.sender.port"));

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailProperties.getProperty("username"),
							emailProperties.getProperty("password"));
				}
			});
			MimeMessage message = new MimeMessage(session);
			List<String> toList = new ArrayList<String>();
			Iterator<ApplicationUser> iter = assignedUsers.iterator();
			String prefix = "";
			while (iter.hasNext()) {
				ApplicationUser user = iter.next();
				prefix = "Dear " + user.getName() + ",<br>";
				toList.add(user.emailAddress);
			}
			try {
				message.setFrom(emailProperties.getProperty("from.address"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toList.get(0)));
				message.setContent(prefix + messageContent, "text/html");
				message.setSubject("Task reminder - CMII - Important");

			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return emailMessenger.notifyMessage(message);
		} else {
			log.info("Cannot remind the task now / not active ");
			return false;
		}
	}

	@Override
	public void addTaskUser(ApplicationUser applicationUser) {
		assignedUsers.add(applicationUser);
	}

	private boolean canRemindNow() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date());
		long timeDiff = gregorianCalendar.getTimeInMillis() - this.reminderTime.getTime();
		if (timeDiff > 0) {
			return true;
		}
		log.info("Printing the time diff " + timeDiff);
		return false;
	}

	@Override
	public void setMessage(String message) {
		this.messageContent = message;
	}

	@Override
	public void setEmailNotifyService(EmailNotifyService emailNotifyService) {
		this.emailMessenger = emailNotifyService;
	}
}
