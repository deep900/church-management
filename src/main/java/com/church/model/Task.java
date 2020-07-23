package com.church.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.church.util.StatusEnum;

import lombok.Data;

@Data
@Document
public abstract class Task {

	@Id
	public String id;

	/**
	 * Foreign key (Reference of event entity).
	 */
	public String eventId;

	/**
	 * Unique name to identify the task.
	 */
	public String taskName;

	/**
	 * Start time of the task.
	 */
	public Timestamp startTime;

	/**
	 * Estimated time to complete this task.
	 */
	public double estimatedHours;

	/**
	 * Represents the current state of the event.
	 */
	public StatusEnum presentState;

	/**
	 * Denotes the sequence of process of task. For example 1. Open the door 2.
	 * Enter the house.
	 */
	public int taskSequence;

	/**
	 * Comments are used to describe about the task progress. can be used to
	 * mark the status of work done for this task.
	 */
	public List<String> comments;

	/**
	 * This reminder is to remind the person working on a task and accomplish
	 * that before the event occurs.
	 */
	public List<Reminder> reminders;
	
	/**
	 * List of child tasks id for any task.
	 */
	public Set<String> childTasks;

}
