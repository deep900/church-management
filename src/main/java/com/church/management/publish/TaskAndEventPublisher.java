/**
 * 
 */
package com.church.management.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.church.model.Event;
import com.church.model.Task;
import com.church.util.ApplicationConstants;

/**
 * @author pradheep
 *
 */
@Component
public class TaskAndEventPublisher {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void publishEvent(Event event) {
		applicationEventPublisher.publishEvent(new ManagementEvent(event));
	}

	public void publishAutoAssignTask(Task task) {
		ManagementEvent event = new ManagementEvent(task);
		event.setEventNature(ApplicationConstants.AUTO_ASSIGN);
		applicationEventPublisher.publishEvent(event);
	}

}
