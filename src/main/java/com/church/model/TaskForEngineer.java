/**
 * 
 */
package com.church.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.church.data.repository.TaskAndUserRepository;

import lombok.Getter;
import lombok.Setter;

/**
 * Mapping a task for a user, one task can be assigned to multiple users.
 * 
 * @author pradheep
 * @see TaskAndUserRepository	
 */
@Document
@Setter
@Getter
public class TaskForEngineer {
	
	@Id
	private String taskId;
	
	private String userId;
}
