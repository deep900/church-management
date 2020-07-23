/**
 * 
 */
package com.church.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.church.model.Task;
import com.church.task.TaskAllotmentManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class DelegateHandler {
	
	@Autowired
	private TaskAllotmentManager taskAllotmentManager;

	public void handleTask(Task taskObj) {
		log.info("Auto assigning the task to engineer.");
		taskAllotmentManager.autoAssignTask(taskObj);
	}
}
