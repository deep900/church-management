/**
 * 
 */
package com.church.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author pradheep
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseModel {

	private Integer responseCode;
	
	private String responseMessage;
	
	private Object object;
}
