/**
 * 
 */
package com.church.model;

import java.sql.Timestamp;
import java.util.List;

import com.church.util.EventTypeEnum;
import com.church.util.StatusEnum;

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
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private EventTypeEnum eventType;

	private StatusEnum eventStatus;

	private ReOccurance reOccurance;

	private List<Reminder> reminders;	
	
}
