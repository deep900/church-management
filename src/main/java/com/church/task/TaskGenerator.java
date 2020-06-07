/**
 * 
 */
package com.church.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.church.data.TaskRepository;
import com.church.model.Event;
import com.church.model.Reminder;
import com.church.model.Task;
import com.church.model.task.DefaultTask;
import com.church.model.task.EventSchema;
import com.church.model.task.EventSchemaLoader;
import com.church.model.task.TaskSchema;
import com.church.serviceimpl.ChurchEventServiceImpl;
import com.church.util.EventTypeEnum;
import com.church.util.StatusEnum;

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

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private EventSchemaLoader eventSchemaLoader;
	
	@Autowired
	private ChurchEventServiceImpl churchEventServiceImpl;

	/**
	 * Task shall be generated one month before the event starts.
	 * 
	 * @param event
	 * @return
	 */
	public boolean canTaskBeCreated(Event event) {
		if (null == event) {
			log.error("Event was null cannot determine.");
			return false;
		}
		log.info("Task shall be created one month before the event starts:");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(GregorianCalendar.MONTH, -1);
		return event.getStartTime().after(calendar.getTime());
	}

	public void prepareTaskAndReminder(Event event) {
		if (event.isTaskGenerated()) {
			log.info("Task already generated.");
			return;
		}
		if (canTaskBeCreated(event)) {
			log.info("Preparing the tasks and reminders for event:" + event);
			log.info("Generating task for event name :" + event.getEventName());
			persistTasks(createEventTasks(event));
			taskCreatedUpdateEvent(event);
		} else {
			log.warn("To early to create the task for :" + event.getEventName() + " because the event start at "
					+ event.getStartTime());
		}
	}

	private ArrayList<Task> createEventTasks(Event event) {
		List<EventSchema> eventSchemaList = eventSchemaLoader.getEventSchemaList();
		Iterator<EventSchema> eventSchemaIterator = eventSchemaList.iterator();
		ArrayList<Task> taskList = new ArrayList<Task>();
		while (eventSchemaIterator.hasNext()) {
			EventSchema eventSchema = eventSchemaIterator.next();
			Iterator<TaskSchema> taskSchemaIterator = eventSchema.getTaskSchemaList().iterator();
			while (taskSchemaIterator.hasNext()) {
				TaskSchema taskSchema = taskSchemaIterator.next();
				DefaultTask task = new DefaultTask();
				Double totalEstimatedHours = taskSchema.getTimePlannedInDays() * 24;
				task.setEstimatedHours(totalEstimatedHours);
				task.setEventId(event.getId());
				task.setPresentState(StatusEnum.scheduled);
				Date currentDate = new Date();
				task.setStartTime(new Timestamp(currentDate.getTime()));
				task.setTaskSequence(taskSchema.getSequence());
				task.setTaskName(taskSchema.getName());
				task.setReminders(getTaskReminders());
				taskList.add(task);
			}
		}
		return taskList;
	}

	private List<Reminder> getTaskReminders() {
		ArrayList<Reminder> reminders = new ArrayList<Reminder>();
		return reminders;
	}

	private void persistTasks(ArrayList<Task> taskList) {
		taskRepository.saveAll(taskList);
	}
	
	private void taskCreatedUpdateEvent(Event event){
		event.setTaskGenerated(true);
		churchEventServiceImpl.updateEvent(event);
		log.info("Updated event.");
	}
}
