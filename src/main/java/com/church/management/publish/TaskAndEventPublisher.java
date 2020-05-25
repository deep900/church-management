/**
 * 
 */
package com.church.management.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.church.model.ChurchEvent;

/**
 * @author pradheep
 *
 */
@Component
public class TaskAndEventPublisher {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void publishEvent(ChurchEvent event) {
		applicationEventPublisher.publishEvent(new ManagementEvent(event));
	}

}
