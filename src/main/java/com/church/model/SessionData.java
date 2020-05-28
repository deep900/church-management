/**
 * 
 */
package com.church.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
@Setter
@Getter
@ToString
public class SessionData {

	@Id
	private String sessionId;
	
	private String ipAddress;
	
	private String client;
	
	private Timestamp timeIssued;
	
	private String captcha;
	
}
