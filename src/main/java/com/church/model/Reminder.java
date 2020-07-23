package com.church.model;

import java.sql.Timestamp;

import com.church.notify.EmailNotifyService;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is to make a reminder for the task worker, can be a engineer or a reviewer.
 * 
 * @author pradheep
 *
 */
@Data
@NoArgsConstructor
@Setter
@Getter
public abstract class Reminder {
	
	public Integer reminderCnt;

    public Timestamp reminderTime;

    public abstract boolean remind();
    
    public abstract void setMessage(String message);
    
    public abstract void addTaskUser(ApplicationUser applicationUser);
    
    public abstract void setEmailNotifyService(EmailNotifyService emailNotifyService);
    
    public boolean active;

}
