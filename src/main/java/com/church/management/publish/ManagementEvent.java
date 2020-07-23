/**
 * 
 */
package com.church.management.publish;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pradheep
 *
 */
@Setter
@Getter
public class ManagementEvent extends ApplicationEvent {
	
	private String eventNature;
	
	public ManagementEvent(Object event){
		super(event);
	}	
}
