/**
 * 
 */
package com.church.model;

import java.util.Date;
import java.util.List;

import com.church.util.StatusEnum;
import com.church.util.EventTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pradheep
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Event {
	
	private String eventName;
	
	private Date startTime;
	
	private Date endTime;
	
	private EventTypeEnum eventType;

	private StatusEnum eventStatus;

	private ReOccurance reOccurance;

	private List<Reminder> reminders;
}