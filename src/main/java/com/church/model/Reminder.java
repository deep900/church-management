package com.church.model;

import java.sql.Timestamp;
import java.util.List;

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

    public abstract void remind();
    
    public abstract void setMessage(String message);
    
    public List<ApplicationUser> toWhomToRemind;

}
