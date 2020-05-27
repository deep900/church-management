/**
 * 
 */
package com.church.model;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Base class for denoting the person working in the tasks assigned.
 * 
 * @author pradheep
 *
 */
public abstract class Worker {

	public String name;
	
	public String contactNumber;
	
	public String countryCode;
	
	public String emailAddress;
	
	public int priority;
	
	public List<Task> taskList;
	
	public UserDetails userDetails;
	
	public List<String> previleges;
	
}
