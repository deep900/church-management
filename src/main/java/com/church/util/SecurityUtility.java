/**
 * 
 */
package com.church.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.church.model.SessionData;
import com.church.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@Component
public class SecurityUtility {

	@Value("{jwt.token.time.to.line}")
	private final String timeToLive = "30000";

	//@Value("{application.signing.key}")
	private final String key = "7yhfkrs-9kjmsi-uyrbcv2-plmzxcd";

	//@Value("{jwt.token.issuer}")
	private final String issuer = "church-management";

	@Autowired
	private EncryptUtility encryptUtility;

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

	public String getPasswordToCompare(byte[] salt, String password) throws IllegalArgumentException {
		if (salt == null || password == null) {
			throw new IllegalArgumentException("Invalid password or salt");
		}
		//ArrayUtils.reverse(salt);
		String decrypted = encryptUtility.decrypt(password, new String(salt));
		return decrypted;
	}

	public String generateSecurityToken(String userId, List<GrantedAuthority> grantedAuthoriryList) {
		Date currentTime = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(currentTime);
		int timeToLiveInMills = Integer.parseInt(timeToLive);
		calendar.add(GregorianCalendar.MILLISECOND, timeToLiveInMills);

		StringBuffer buffer = new StringBuffer();
		Iterator<GrantedAuthority> iter = grantedAuthoriryList.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(",");
			}
		}
		String jws = Jwts.builder().setIssuer(issuer).claim(SecurityConstants.CLAIM_ROLES, buffer.toString())
				.setIssuedAt(Date.from(Instant.ofEpochSecond(currentTime.getTime())))
				.setExpiration(Date.from(Instant.ofEpochSecond(calendar.getTimeInMillis())))
				.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(key)).setSubject(userId).compact();
		return jws;
	}

	public Claims decodeJWT(String jwt) throws Exception {		
		Claims claims = Jwts.parser().setSigningKey(TextCodec.BASE64.decode(key)).parseClaimsJws(jwt).getBody();
		return claims;
	}

	public static void main(String args[]) {
		SecurityUtility utility = new SecurityUtility();
		System.out.println(utility.generateUUID());
		String userId = "abc123";
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
		SimpleGrantedAuthority grantedAuthority1 = new SimpleGrantedAuthority("ROLE_USER");
		grantedAuthorityList.add(grantedAuthority);
		grantedAuthorityList.add(grantedAuthority1);
		String jwt = utility.generateSecurityToken(userId, grantedAuthorityList);
		System.out.println(jwt);
		Claims claims;
		try {
			claims = utility.decodeJWT(jwt);
			System.out.println(claims.get("Roles"));
			System.out.println(claims.getSubject());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
