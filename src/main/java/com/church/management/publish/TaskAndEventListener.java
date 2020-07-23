/**
 * 
 */
package com.church.management.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.church.model.Task;
import com.church.util.ApplicationConstants;
import com.church.util.DelegateHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class TaskAndEventListener implements ApplicationListener<ApplicationEvent> {

	@Autowired
	private DelegateHandler delegateHandler;

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		log.info("Received the event: " + applicationEvent.getClass().toString());
		if (applicationEvent instanceof ManagementEvent) {
			ManagementEvent event = (ManagementEvent) applicationEvent;
			log.info("Event Nature:" + event.getEventNature());
			if (event.getSource() instanceof Task && event.getEventNature().equals(ApplicationConstants.AUTO_ASSIGN)) {
				Task taskObj = (Task) applicationEvent.getSource();
				delegateHandler.handleTask(taskObj);
			}
		}
	}
}
