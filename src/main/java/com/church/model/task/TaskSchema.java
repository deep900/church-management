/**
 * 
 */
package com.church.model.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author pradheep
 * 
 */
@Setter
@Getter
@ToString
public class TaskSchema {

	public String name;
	
	public Double timePlannedInDays = 1.0;
	
	public String assignation = "auto";
	
	public int maxAssigned = 1;
	
	public int sequence;
	
	public boolean isParallel = false;
	
}
