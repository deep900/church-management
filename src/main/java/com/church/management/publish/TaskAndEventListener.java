/**
 * 
 */
package com.church.management.publish;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Component
@Slf4j
public class TaskAndEventListener implements ApplicationListener<ApplicationEvent>{

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		log.info("Received the event: "+ applicationEvent.getClass().toString());
	}	
}
