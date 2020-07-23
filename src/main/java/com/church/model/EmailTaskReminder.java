package com.church.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;

import com.church.notify.EmailNotifyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailTaskReminder extends Reminder {

	private String messageContent = "";
	
	private EmailNotifyService emailMessenger;

	private List<ApplicationUser> assignedUsers = new ArrayList<ApplicationUser>();		

	@Override
	public boolean remind() {
		if (canRemindNow() && active) {
			log.info("Reminding the task now ");			
			SimpleMailMessage message = new SimpleMailMessage();
			List<String> toList = new ArrayList<String>();
			Iterator<ApplicationUser> iter = assignedUsers.iterator();
			String prefix = "";
			while (iter.hasNext()) {
				ApplicationUser user = iter.next();
				prefix = "Dear " + user.getName() + ",<br>";
				toList.add(user.emailAddress);
			}
			message.setTo(toList.get(0));			
			message.setText(prefix + messageContent);
			log.info("Message :" + message.getText());
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
