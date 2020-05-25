/**
 * 
 */
package com.church.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.church.exception.TaskAlreadyExistsException;
import com.church.model.Reminder;
import com.church.model.Task;
import com.church.util.StatusEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class TaskManager {
	
	@Autowired
	private TaskReminderRunner taskReminderRunner;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private HashMap<String, Task> tasks = new HashMap<String, Task>();

	public boolean doesTaskAlreadyExits(String taskId) {
		return tasks.containsKey(taskId);
	}

	public void addTask(Task taskObj) throws TaskAlreadyExistsException {
		if (tasks.containsKey(taskObj.getId())) {
			log.error("Task already exists:" + taskObj.getId());
			throw new TaskAlreadyExistsException(taskObj.getId());
		}
		tasks.put(taskObj.getId(), taskObj);
		log.info("Successfully added the task :" + taskObj.getId());
	}

	public void remindTasks() {
		log.info("--- Reminding the tasks ---");
		List<Reminder> reminders = new ArrayList<Reminder>();
		Set taskSet = tasks.entrySet();
		Iterator<Task> iter = taskSet.iterator();
		while(iter.hasNext()){
			Task obj = iter.next();
			if(!obj.getPresentState().equals(StatusEnum.completed)){
				reminders.addAll(obj.getReminders());
			}
		}
		taskReminderRunner.setReminders(reminders);
		threadPoolTaskExecutor.execute(taskReminderRunner);
	}

	public void updateTask(Task taskObj) {
		if (doesTaskAlreadyExits(taskObj.getId())) {
			tasks.put(taskObj.getId(), taskObj);
			log.info("Task updated successfully");
		}
	}

	public boolean removeTask(Task taskObj) {
		if (doesTaskAlreadyExits(taskObj.getId())) {
			tasks.remove(taskObj.getId());
			return true;
		}
		return false;
	}

}
