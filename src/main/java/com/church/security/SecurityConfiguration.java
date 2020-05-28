package com.church.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.church.util.APIConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean(value = "userDetailsService")
	public AppUserDetails userDetailsService() {
		return new AppUserDetails();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
		auth.authenticationProvider(getAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		Arrays.asList(APIConstants.skipAuthenticationAPIArray).forEach(x -> {
			try {
				http.anonymous().and().antMatcher(x.toString());
			} catch (Exception err) {
				log.error("Error while configuring default skip URL", err);
			}
		});
		http.authorizeRequests().antMatchers("**/admin/**").hasAnyRole(SecurityConstants.ROLE_ADMIN_USER);
		http.authorizeRequests().antMatchers("**/api/**").hasAnyRole(SecurityConstants.ROLE_USER,
				SecurityConstants.ROLE_ADMIN_USER, SecurityConstants.ROLE_TASK_REVIEWER,
				SecurityConstants.ROLE_TASK_WORKER);
		http.addFilterBefore(getCustomSecurityFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public CustomSecurityFilter getCustomSecurityFilter() {
		return new CustomSecurityFilter();
	}

	@Bean(value = "authenticationProvider")
	public ApplicationAuthenticationManager getAuthenticationProvider() {
		return new ApplicationAuthenticationManager();
	}
}
