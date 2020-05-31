/**
 * 
 */
package com.church.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.church.security.SecurityUserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for denoting the person working in the tasks assigned.
 * 
 * @author pradheep
 *
 */
@Document
@Getter
@Setter
public class ApplicationUser {

	public String name;
	
	public String contactNumber;
	
	public String countryCode;
	
	public String emailAddress;
	
	public int priority;
	
	public List<Task> taskList;
	
	public SecurityUserDetails userDetails;
	
	public List<String> previleges;	
	
	@JsonIgnore
	public byte[] salt;
	
}
