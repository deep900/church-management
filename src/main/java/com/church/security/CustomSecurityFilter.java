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

import org.springframework.web.filter.OncePerRequestFilter;

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

	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain fc) throws ServletException, IOException {
		boolean skipFilter = canSkipFilter(servletRequest);
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
			skipAPIList = Arrays.asList(APIConstants.skipAPI);
		}
		return skipAPIList;
	}

}
