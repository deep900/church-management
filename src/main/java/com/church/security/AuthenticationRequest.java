/**
 * 
 */
package com.church.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class AuthenticationRequest  implements Authentication {

	private String name;

	private String emailAddress;

	private String password;

	private List<? extends GrantedAuthority> grantedAuthorities;

	private boolean isAuthenticated = false;

	private Object details;

	@JsonIgnore
	private String sessionId;	
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	
	public Object getCredentials() {
		return getPassword();
	}
	
	public Object getDetails() {
		return details;
	}
	
	public Object getPrincipal() {
		return getEmailAddress();
	}
	
	public boolean isAuthenticated() {
		return isAuthenticated;
	}
	
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		isAuthenticated = arg0;
	}

}
