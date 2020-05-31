package com.church.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.church.model.ApplicationUser;
import com.church.serviceimpl.SecurityServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppUserDetails implements UserDetailsService {

	@Autowired
	private SecurityServiceImpl securityServiceImpl;

	@Override
	public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
		if (null == emailAddress) {
			log.error("Invalid email address provided");
			return null;
		}
		ApplicationUser userObj = securityServiceImpl.getUserByEmail(emailAddress);
		if (null == userObj) {
			log.info("No user details found for [Email]:" + emailAddress);
			return null;
		}
		return userObj.getUserDetails();
	}
}
