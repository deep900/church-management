/**
 * 
 */
package com.church.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.church.data.repository.TaskRepository;
import com.church.model.Reminder;
import com.church.model.Task;
import com.church.notify.EmailNotifyService;
import com.church.util.StatusEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is to manage the task status.
 * 
 * @author pradheep
 *
 */
@Slf4j
public class TaskManager {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private EmailNotifyService emailNotifyService;

	public void remindTasks() {
		log.info("- Reminding the tasks -");
		List<Task> tasksNotCompleted = taskRepository.findTaskByState(StatusEnum.scheduled.label);
		if (null != tasksNotCompleted) {
			log.info("Number of tasks found for reminding:" + tasksNotCompleted.size());
			Iterator<Task> iter = tasksNotCompleted.iterator();
			while (iter.hasNext()) {
				Task taskObj = iter.next();
				List<Reminder> reminderList = taskObj.getReminders();
				Iterator<Reminder> reminderIterator = reminderList.iterator();
				while (reminderIterator.hasNext()) {
					Reminder reminderObj = reminderIterator.next();
					String message = "Check the task assigned to you on :" + taskObj.getStartTime() + "<br>";
					reminderObj.setMessage(message);
					reminderObj.setEmailNotifyService(emailNotifyService);
					if (reminderObj.remind()) {
						reminderObj.setActive(false);
						log.info("The reminder job is executed successfully: " + taskObj.getId());
						taskRepository.save(taskObj);
						log.info("Updated the task with the reminder set inactive.");
					}
				}
			}
		}
	}
}
