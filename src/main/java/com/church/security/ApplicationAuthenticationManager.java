/**
 * 
 */
package com.church.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class ApplicationAuthenticationManager implements AuthenticationProvider{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication arg0) throws AuthenticationException {
		log.info("Authenticating the request:");		
		return null;
	}

	@Override
	public boolean supports(Class<?> className) {		
		if(className.equals(Authentication.class)){
			return true;
		}
		return false;
	}

}
