/**
 * 
 */
package com.church.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.church.util.EventTypeEnum;
import com.church.util.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pradheep
 *
 */
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Event {
	
	@Id
	private String Id;
	
	private String eventName;
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private EventTypeEnum eventType;

	private StatusEnum eventStatus;

	private ReOccurance reOccurance;

	private List<Reminder> reminders;
	
	private boolean taskGenerated;
	
}
