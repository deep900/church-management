/**
 * 
 */
package com.church.util;

import com.church.model.Task;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class DelegateHandler {

	public void autoAssignTask(Task taskObj) {
		log.info("Auto assigning the task to engineer.");
		String taskName = taskObj.getTaskName();
		if (taskName.equalsIgnoreCase("create-card")) {
			log.info("Auto assigning the create card task to engineer");
		} else if (taskName.equalsIgnoreCase("review-card")) {
			log.info("Auto assigning the review card task to engineer");
		}

	}
}
