/**
 * 
 */
package com.church.model.task;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class define how the event and task to behave. 
 * 
 * This is created from the event-task-mapper.xml
 * 
 * @author pradheep
 *
 */
@Getter
@Setter
@ToString
public class EventSchema {
	
	public String name;
	
	public List<TaskSchema> taskSchemaList;	
	
}
