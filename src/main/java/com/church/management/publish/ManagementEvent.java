/**
 * 
 */
package com.church.management.publish;

import org.springframework.context.ApplicationEvent;

/**
 * @author pradheep
 *
 */
public class ManagementEvent extends ApplicationEvent {	
	
	public ManagementEvent(Object event){
		super(event);		
	}
}
