package com.church.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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

	@Bean(name = "corsFilter")
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		CorsFilter bean = new CorsFilter(source);
		return bean;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(getCustomSecurityFilter(), BasicAuthenticationFilter.class).csrf().disable();
		http.cors().and().addFilter(corsFilter());
		http.antMatcher("**/authenticateReq**").anonymous();
		http.antMatcher("**/prepareForLogin**").anonymous();
		//http.authorizeRequests().antMatchers("**/admin/**").access(removeRole(SecurityConstants.ROLE_ADMIN_USER));
		//http.authorizeRequests().antMatchers("**/api/**").access(removeRole(SecurityConstants.ROLE_USER));
		//http.securityContext().securityContextRepository(securityContextRepository());
	}

	/*@Bean
	public SecurityContextRepository securityContextRepository() {
		HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
		repo.setSpringSecurityContextKey("CUSTOM");
		return repo;
	}*/

	private String removeRole(String role) {
		return "hasRole('" + role + "')";
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
