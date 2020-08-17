/**
 * 
 */
package com.church.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.church.model.Event;
import com.church.model.Task;
import com.church.task.TaskAllotmentManager;
import com.church.task.TaskGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class DelegateHandler {
	
	@Autowired
	private TaskAllotmentManager taskAllotmentManager;
	
	@Autowired
	private TaskGenerator taskGenerator;

	public void handleTask(Task taskObj) {
		log.info("Auto assigning the task to engineer.");
		taskAllotmentManager.autoAssignTask(taskObj);
	}
	
	public void createTasks(Event eventObj){
		log.info("Creating the tasks for the new event.");
		taskGenerator.prepareTaskAndReminder(eventObj);
	}
}
