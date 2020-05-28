/**
 * 
 */
package com.church.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.church.model.SessionData;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@Component
public class SecurityUtility {

	public String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public String generateJWTToken(Authentication authenticationObj) {
		return "";
	}

	public SessionData createSessionData(HttpServletRequest httpServletRequest) {
		SessionData session = new SessionData();
		session.setIpAddress(getIPAddressFromRequest(httpServletRequest));
		String browser = httpServletRequest.getHeader("User-Agent");
		if (null != browser) {
			session.setClient(browser);
		}
		session.setSessionId(generateUUID());
		session.setTimeIssued(new Timestamp(new Date().getTime()));
		log.info("Session data " + session.toString());
		return session;
	}

	private String getIPAddressFromRequest(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}
	
	public static void main(String args[]) {
		SecurityUtility utility = new SecurityUtility();
		System.out.println(utility.generateUUID());
	}
}
