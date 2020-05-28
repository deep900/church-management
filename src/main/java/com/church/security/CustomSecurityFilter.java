/**
 * 
 */
package com.church.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.church.model.SessionData;
import com.church.service.SecurityService;
import com.church.serviceimpl.SecurityServiceImpl;
import com.church.util.APIConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is to parse the JWT token in the header.
 * 
 * Provides the function of authorization of any request.
 * 
 * @author pradheep
 *
 */
@Slf4j
public class CustomSecurityFilter extends OncePerRequestFilter {

	private List skipAPIList;

	private boolean flag = false;

	@Autowired
	private SecurityServiceImpl securityServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain fc) throws ServletException, IOException {
		boolean skipFilter = canSkipFilter(servletRequest);
		boolean isValidSession = isValidSession(servletRequest);
		if(!isValidSession){
			servletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid session");
			return;
		}
		log.info("Authorizing the request " + skipFilter);
		if (skipFilter) {
			fc.doFilter(servletRequest, servletResponse);
		} else {
			// Authorize request //
		}
	}

	private boolean canSkipFilter(HttpServletRequest request) {
		flag = false;
		skipFilter().forEach(x -> {
			if (request.getRequestURI().contains(x)) {
				flag = true;
			}
		});
		return flag;
	}

	private List<String> skipFilter() {
		if (null == skipAPIList) {
			skipAPIList = Arrays.asList(APIConstants.skipAuthenticationAPIArray);
		}
		return skipAPIList;
	}

	private boolean isValidSession(HttpServletRequest request){
		// Validate only for request other than pre authorization //
		if (request.getRequestURI().contains(APIConstants.PREPARE_LOGIN)){
			log.info("Skip the session id test for :" + APIConstants.PREPARE_LOGIN);
			return true;
		}
		String sessionId = request.getHeader(SecurityService.SESSION_ID);
		if(null == sessionId){
			log.error("Invalid session id");
			return false;
		}
		SessionData sessionData = securityServiceImpl.getSessionDataById(sessionId);
		if(null != sessionData){
			return true;
		}
		log.info("Invalid session id :" + sessionId);
		return false;
	}

}
