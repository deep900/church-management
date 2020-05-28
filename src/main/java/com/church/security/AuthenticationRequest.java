/**
 * 
 */
package com.church.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author pradheep
 *
 */
@Setter
@Getter
@ToString
public class AuthenticationRequest {

	private String emailAddress;
	
	private String password;
	
}
