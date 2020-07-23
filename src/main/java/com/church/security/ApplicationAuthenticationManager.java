/**
 * 
 */
package com.church.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.church.model.ApplicationUser;
import com.church.serviceimpl.SecurityServiceImpl;
import com.church.util.EncryptUtility;
import com.church.util.SecurityUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * Used email address as the login id for authentication.
 * 
 * @author pradheep
 *
 */
@Slf4j
@Component
public class ApplicationAuthenticationManager implements AuthenticationProvider {

	@Autowired
	private SecurityServiceImpl securityServiceImpl;

	@Autowired
	private SecurityUtility securityUtil;

	@Autowired
	private EncryptUtility encryptUtility;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("Authenticating the request:");
		authentication.setAuthenticated(false);
		if (authentication instanceof AuthenticationRequest) {
			AuthenticationRequest request = (AuthenticationRequest) authentication;
			String emailAddress = request.getEmailAddress();
			ApplicationUser userObj = securityServiceImpl.getUserByEmail(emailAddress);
			if (null == userObj) {
				log.error("No such user found [Email]" + emailAddress);
				authentication.setAuthenticated(false);
				return authentication;
			}
			String encryptedEmail = encryptUtility.encrypt(emailAddress, "");
			byte[] salt = userObj.getSalt();
			log.debug("Salt" + new String(salt));
			String _pass = securityUtil.getPasswordToCompare(salt, userObj.getUserDetails().getPassword());
			log.info("Printing the password: " + _pass);
			String sessionId = request.getSessionId();
			log.info("Printing the sessionId:" + sessionId);
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-512");
			} catch (NoSuchAlgorithmException err) {
				log.error("Error while user authentication , no such algorithm", err);
			}
			//md.update(sessionId.getBytes());
			
			byte[] hashedPassword = md.digest(_pass.getBytes(StandardCharsets.UTF_8));
			String hashHexPassword = bytesToHexV1(hashedPassword);
			log.info("Password :" + hashHexPassword);
			if (request.getPassword().equals(hashHexPassword)) {
				request.setAuthenticated(true);
				List<GrantedAuthority> grantedAuthorityList = (List<GrantedAuthority>) userObj.getUserDetails()
						.getAuthorities();
				String jwt = securityUtil.generateSecurityToken(encryptedEmail, grantedAuthorityList);
				request.setDetails(jwt);
				log.info("Email:" + emailAddress + ", authentication success at:" + new Date());
				return request;
			}
			else{
				log.error("Password do not match " + request.getPassword() + " And " + hashHexPassword);
			}
		}
		return authentication;
	}
	
	public String bytesToHexV1(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	@Override
	public boolean supports(Class<?> className) {
		if (className.equals(Authentication.class) || className.equals(AuthenticationRequest.class)) {
			return true;
		}
		return false;
	}
}
