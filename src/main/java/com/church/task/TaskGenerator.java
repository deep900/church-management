/**
 * 
 */
package com.church.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.church.data.repository.EventRepository;
import com.church.data.repository.TaskRepository;
import com.church.management.publish.TaskAndEventPublisher;
import com.church.model.EmailTaskReminder;
import com.church.model.Event;
import com.church.model.Reminder;
import com.church.model.Task;
import com.church.model.task.DefaultTask;
import com.church.model.task.EventSchema;
import com.church.model.task.EventSchemaLoader;
import com.church.model.task.TaskSchema;
import com.church.serviceimpl.ChurchEventServiceImpl;
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
@Component
public final class TaskGenerator  {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private EventSchemaLoader eventSchemaLoader;

	@Autowired
	private ChurchEventServiceImpl churchEventServiceImpl;

	@Autowired
	private TaskAndEventPublisher taskAndEventPublisher;

	@Autowired
	private EventRepository eventsRepository;

	private final int INITIAL_REMINDER_COUNTER = 0;

	List<EventSchema> eventSchemaList = null;
	
	@PostConstruct
	private void loadEventSchema() {
		eventSchemaList = eventSchemaLoader.getEventSchemaList();
	}

	/**
	 * Can be called during server startup , picks up all events and create
	 * tasks.
	 */
	public void prepareAllEvents() {
		eventsRepository.findAll().forEach(x -> prepareTaskAndReminder(x));
	}

	/**
	 * Generates the task and reminders for the given event.
	 * 
	 * @param event
	 */
	public synchronized void prepareTaskAndReminder(Event event) {
		if (event.isTaskGenerated()) {
			log.info("Task already generated.");
			return;
		}
		log.info("Preparing the tasks and reminders for event:" + event);
		log.info("Generating task for event name :" + event.getEventName());
		persistTasks(createEventTasks(event));		
		taskCreatedUpdateEvent(event);
	}

	private ArrayList<Task> createEventTasks(Event event) {
		log.info("Creating tasks for event :" + event.getEventName());
		ArrayList<Task> taskList = new ArrayList<Task>();
		Optional<List<TaskSchema>> taskSchemaList = getTaskSchemaForEvent(event);
		if (taskSchemaList.isPresent()) {
			taskSchemaList.get().forEach(taskSchema -> {
				Optional<DefaultTask> taskObj = getTaskBasedOnSchema(taskSchema, event);
				if (taskObj.isPresent()) {
					Task task = taskObj.get();
					taskList.add(task);
					Optional<Task> parentTaskObj = getParentTask(taskList, taskSchema);
					if (parentTaskObj.isPresent()) {
						Task parentTask = parentTaskObj.get();
						if (parentTask.getChildTasks() == null) {
							HashSet<String> set = new HashSet();
							set.add(task.getId());
							parentTask.setChildTasks(set);
						} else {
							parentTask.getChildTasks().add(task.getId());
						}
					}
					else{
						log.debug("No child task found for:" + task.getTaskName());
					}
					taskAndEventPublisher.publishAutoAssignTask(task);
				}
			});
		}
		return taskList;
	}

	private Optional<Task> getParentTask(ArrayList<Task> taskList, TaskSchema taskSchema) {
		if (!taskSchema.getDependsOn().isEmpty()) {
			List<Task> taskListFiltered = taskList.stream().filter(x -> x.taskName.equals(taskSchema.getDependsOn()))
					.collect(Collectors.toList());
			if (!taskListFiltered.isEmpty()) {
				return Optional.of(taskListFiltered.get(0));
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns the task schema for the given event , which has the
	 * specifications of the tasks and events.
	 * 
	 * @param event
	 * @return
	 */
	private Optional<List<TaskSchema>> getTaskSchemaForEvent(Event event) {
		Iterator<EventSchema> eventSchemaIterator = eventSchemaList.iterator();
		while (eventSchemaIterator.hasNext()) {
			EventSchema eventSchema = eventSchemaIterator.next();
			log.debug("Printing the event name in schema:" + eventSchema.getName());
			if (eventSchema.getName().equalsIgnoreCase(event.getEventName())) {
				log.info("Event Name:" + event.getEventName());
				return Optional.of(eventSchema.getTaskSchemaList());
			}
		}
		return Optional.empty();
	}

	private int getCurrentYear() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		return calendar.get(GregorianCalendar.YEAR);
	}

	private boolean canCreateTaskNow(TaskSchema taskSchema, Event event) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(event.getStartTime().getTime());
		calendar.set(GregorianCalendar.YEAR, getCurrentYear());

		GregorianCalendar calendar1 = new GregorianCalendar();
		calendar1.setTime(new Date());

		int dateDiff = calendar.get(GregorianCalendar.DAY_OF_YEAR) - calendar1.get(GregorianCalendar.DAY_OF_YEAR);
		log.info("Printing the date difference :" + dateDiff);
		return (Math.abs(taskSchema.getDaysBeforeToCreateTask()) <= dateDiff);
	}

	private Optional<DefaultTask> getTaskBasedOnSchema(TaskSchema taskSchema, Event event) {
		if (!canCreateTaskNow(taskSchema, event)) {
			log.info("Too early to create the task now :" + event.getEventName());
			return Optional.empty();
		}
		DefaultTask task = new DefaultTask();
		task.setId(UUID.randomUUID().toString());
		Double totalEstimatedHours = taskSchema.getTimePlannedInDays() * 24;
		task.setEstimatedHours(totalEstimatedHours);
		task.setEventId(event.getId());
		task.setPresentState(StatusEnum.scheduled);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(event.getStartTime().getTime());
		calendar.set(GregorianCalendar.YEAR, getCurrentYear());
		calendar.add(GregorianCalendar.DAY_OF_MONTH, taskSchema.getDaysBeforeToCreateTask());
		task.setStartTime(new Timestamp(calendar.getTime().getTime()));
		task.setTaskSequence(taskSchema.getSequence());
		task.setTaskName(taskSchema.getName());
		task.setReminders(getTaskReminders(task, taskSchema.getNumberOfDaysToRemind()));
		return Optional.of(task);
	}

	private List<Reminder> getTaskReminders(Task taskObj, int numberOfDaysToRemind) {
		ArrayList<Reminder> reminders = new ArrayList<Reminder>();
		EmailTaskReminder emailReminder = new EmailTaskReminder();
		emailReminder.setActive(true);
		emailReminder.setReminderCnt(INITIAL_REMINDER_COUNTER);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(taskObj.getStartTime());
		GregorianCalendar calendar1 = new GregorianCalendar();
		calendar1.setTime(new Date());
		int daysDiff = Math
				.abs(calendar1.get(GregorianCalendar.DAY_OF_YEAR) - calendar.get(GregorianCalendar.DAY_OF_YEAR));
		log.info("Days difference between days:" + daysDiff);
		Timestamp reminderTimeStamp = null;
		if (daysDiff > Math.abs(numberOfDaysToRemind)) {
			calendar.add(GregorianCalendar.DAY_OF_MONTH, numberOfDaysToRemind);
			reminderTimeStamp = new Timestamp(calendar.getTimeInMillis());
		} else {
			reminderTimeStamp = getReminderTime(calendar, calendar1);
		}
		emailReminder.setReminderTime(reminderTimeStamp);	
		reminders.add(emailReminder);
		return reminders;
	}

	private Timestamp getReminderTime(GregorianCalendar reminderCal, GregorianCalendar currentDateCalendar) {
		// Calculate the average time to remind.
		long avgTime = ((currentDateCalendar.getTimeInMillis() + reminderCal.getTimeInMillis()) / 2);
		return new Timestamp(avgTime);

	}

	private void persistTasks(ArrayList<Task> taskList) {
		taskRepository.saveAll(taskList);
	}

	private void taskCreatedUpdateEvent(Event event) {
		event.setTaskGenerated(true);
		churchEventServiceImpl.updateEvent(event);
		log.info("Updated event.");
	}	
}
