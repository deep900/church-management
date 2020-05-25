/**
 * 
 */
package com.church.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author pradheep
 *
 */
@ToString
@Setter
@Getter
@Document
public class ChurchEvent extends Event {

	@Id
	private String Id;
	
}
