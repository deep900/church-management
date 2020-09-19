/**
 * 
 */
package com.church.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.church.model.SessionData;
import com.church.service.SecurityService;
import com.church.serviceimpl.SecurityServiceImpl;
import com.church.util.APIConstants;
import com.church.util.EncryptUtility;
import com.church.util.SecurityUtility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
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

	@Autowired
	private SecurityUtility securityUtil;

	@Autowired
	private EncryptUtility encryptUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain fc) throws ServletException, IOException {
		boolean skipFilter = canSkipFilter(servletRequest);
		log.info("Can skip filter: " + skipFilter);		
		boolean isValidSession = isValidSession(servletRequest);
		log.info("Skip session: " + isValidSession);
		if (!isValidSession) {
			servletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid session");
			return;
		}
		log.info("Authorizing the request " + skipFilter);
		if (skipFilter) {
			fc.doFilter(servletRequest, servletResponse);
		} else {
			// Authorize request //
			String token = servletRequest.getHeader(SecurityConstants.AUTH_HEADER);
			if (null == token) {
				servletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid Authorization Header");
				return;
			}
			try {
				Claims claims = securityUtil.decodeJWT(token);
				log.info("Sucessfully got claims:" + claims);
				String roles = claims.get(SecurityConstants.CLAIM_ROLES).toString();
				String[] roleArr = roles.split(",");
				List<GrantedAuthority> grantedAuthList = new ArrayList<GrantedAuthority>();
				for (int i = 0; i < roleArr.length; i++) {
					SimpleGrantedAuthority grantedAuth = new SimpleGrantedAuthority(roleArr[i]);
					grantedAuthList.add(grantedAuth);
				}
				String email = claims.getSubject();
				String decryptedEmail = encryptUtil.decrypt(email, "");
				AuthenticationRequest authentication = new AuthenticationRequest();
				authentication.setGrantedAuthorities(grantedAuthList);
				authentication.setEmailAddress(decryptedEmail);
				authentication.setAuthenticated(true);
				SecurityContext sc = SecurityContextHolder.getContext();
				sc.setAuthentication(authentication);
				fc.doFilter(servletRequest, servletResponse);
			} catch (ExpiredJwtException err) {
				log.error("Error while parsing token", err);
				servletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Token expired - Login again");
				return;
			} catch (MalformedJwtException | UnsupportedJwtException | SignatureException err) {
				log.error("Error while parsing token", err);
				servletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Token malformed or invalid - Login again");
				return;
			} catch (Exception err) {
				log.error("Error while parsing token", err);
				servletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Internal server error ");
				return;
			}
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

	private boolean isValidSession(HttpServletRequest request) {
		// Validate only for request other than pre authorization //
		if (request.getRequestURI().contains(APIConstants.PREPARE_LOGIN)) {
			log.info("Skip the session id test for :" + APIConstants.PREPARE_LOGIN);
			return true;
		}
		String sessionId = request.getHeader(SecurityService.SESSION_ID);
		if (null == sessionId) {
			log.error("Invalid session id");
			return false;
		}
		SessionData sessionData = securityServiceImpl.getSessionDataById(sessionId);
		if (null != sessionData) {
			return true;
		}
		log.info("Invalid session id :" + sessionId);
		return false;
	}

}
