/**
 * 
 */
package com.church.notification;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author pradheep
 *
 */
@Getter
@Setter
@ToString
public class Message {

	public String subject;
	
	public List<String> receipientList;
	
	public String content;
}
