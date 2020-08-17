/**
 * 
 */
package com.church.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pradheep
 *
 */
@Document
@Getter
@Setter
public class BlackListToken {
	
	@Id
	private String blackListedToken;
	
	private Timestamp cleanTime;
	
}
