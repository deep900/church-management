/**
 * 
 */
package com.church.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.church.util.EventTypeEnum;
import com.church.util.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pradheep
 * @see event-task-mapper.xml
 */
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Id
	private String Id;

	/**
	 * Unique name for the event.
	 */
	private String eventName;

	/**
	 * Represents the start time of the event.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp startTime;

	/**
	 * Represents the end time of the event.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;

	/**
	 * Classification of event.
	 * 
	 * @see EventTypeEnum
	 */
	private EventTypeEnum eventType;

	/**
	 * Represents the current status of event.
	 */
	private StatusEnum eventStatus;

	/**
	 * This can be specified if the event occurs in regular intervals.
	 */
	private ReOccurance reOccurance;

	/**
	 * This reminds about the actual event happening , to be used when the event
	 * has to be reminded on that day of occurance.
	 */
	private List<Reminder> reminders;

	/**
	 * This flag denotes if the tasks are generated for this event already in a
	 * cycle. This helps to identify and creating the tasks again for this
	 * event.
	 */
	private boolean taskGenerated;

	/**
	 * Special note for the events.
	 */
	private String remarks;

}
