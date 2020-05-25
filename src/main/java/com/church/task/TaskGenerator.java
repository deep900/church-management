/**
 * 
 */
package com.church.task;

import com.church.model.Event;

import lombok.extern.slf4j.Slf4j;

/**
 * Generates the tasks for a particular event. This can be triggered by
 * registering an event from front end application. This is also used to create
 * tasks for perpetual events in the database. The events in the database are
 * scanned automatically when the server startup.
 * 
 * @author pradheep
 *
 */
@Slf4j
public abstract class TaskGenerator {

	public void prepareTaskAndReminder(Event event) {
		log.info("Preparing the tasks and reminders for event:" + event);
	}
}
