package com.church.model;

import java.sql.Timestamp;

import com.church.notify.EmailNotifyService;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is to make a reminder for the task worker, can be a engineer or a
 * reviewer.
 * 
 * @author pradheep
 *
 */
@Data
@NoArgsConstructor
@Setter
@Getter
public class Reminder {

	public Integer reminderCnt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	public Timestamp reminderTime;

	public boolean remind() {
		return true;
	}

	public void setMessage(String message) {

	}

	public void addTaskUser(ApplicationUser applicationUser) {

	}

	public void setEmailNotifyService(EmailNotifyService emailNotifyService) {

	}

	public boolean active;

}
