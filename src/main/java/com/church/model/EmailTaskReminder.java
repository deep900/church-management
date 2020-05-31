package com.church.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.church.notification.EmailMessenger;
import com.church.notification.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailTaskReminder extends Reminder {

	private String messageContent = "";
	
	@Autowired
	private EmailMessenger emailMessenger;
	
	@Override
	public void remind() {
		log.info("Task Reminder..");
		Message message = new Message();
		message.setContent(messageContent);
		List<String> toList = new ArrayList<String>();
		Iterator<ApplicationUser> iter = getToWhomToRemind().iterator();
		while(iter.hasNext()){
			toList.add(iter.next().emailAddress);
		}
		message.setReceipientList(toList);
		log.info("Message " + message.toString());
		emailMessenger.communicate(message);
	}

	@Override
	public void setMessage(String message) {
		this.messageContent = message;
	}
}
