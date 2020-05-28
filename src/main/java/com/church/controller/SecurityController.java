/**
 * 
 */
package com.church.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.church.model.ResponseModel;
import com.church.model.SessionData;
import com.church.security.AuthenticationRequest;
import com.church.service.SecurityService;
import com.church.serviceimpl.SecurityServiceImpl;
import com.church.util.APIConstants;
import com.church.util.ApplicationConstants;
import com.church.util.SecurityUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@RestController
public class SecurityController extends BaseController {

	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private SecurityUtility securityUtility;
	
	@Autowired
	private SecurityServiceImpl securityService;
	
	@GetMapping(value=APIConstants.PREPARE_LOGIN)
	public @ResponseBody ResponseModel prepareAuthentication(HttpServletRequest httpServletRequest){
		log.info("Preparing the data for authentication");
		ResponseModel responseModel = getDefaultResponseModel();
		SessionData sessionData = securityUtility.createSessionData(httpServletRequest);
		securityService.saveSession(sessionData);
		HashMap<String,String> details = new HashMap<String,String>();
		details.put(SecurityService.SESSION_ID, sessionData.getSessionId());
		responseModel.setObject(details);
		return responseModel;
	}

	@PostMapping(value=APIConstants.AUTHENTICATE)
	public @ResponseBody ResponseModel authenticateRequest(HttpServletRequest httpServletRequest,
			@RequestBody AuthenticationRequest authRequest) {
		ResponseModel responseModel = getDefaultResponseModel();
		if (null != authRequest && null != authRequest.getEmailAddress() && null != authRequest.getPassword()) {
			log.info("Authentication request:" + authRequest.toString());
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					authRequest.getEmailAddress(), authRequest.getPassword());
			Authentication authObj = authenticationProvider.authenticate(token);
			return responseModel;

		} else {
			log.error("Invalid credentials found, please check the request");
			responseModel.setResponseCode(ApplicationConstants.INVALID_CREDENTIALS);
			responseModel.setResponseMessage(ApplicationConstants.INVALID_CREDENTIALS_MSG);
			return responseModel;
		}
	}	
	
}
