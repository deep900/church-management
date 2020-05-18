/**
 * 
 */
package com.church.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author pradheep
 *
 */
@Document
public class ChurchEvent extends Event {

	@Id
	private String Id;
	
}
