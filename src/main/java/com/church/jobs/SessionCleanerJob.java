/**
 * 
 */
package com.church.jobs;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.church.serviceimpl.SecurityServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * This job is meant for cleaning the session ids in the table.
 * 
 * @author pradheep
 *
 */
@Slf4j
@Component
public class SessionCleanerJob implements Runnable, InitializingBean {
	
	private String fixedDelayForTaskMonitor = "60000";

	@Autowired
	private Environment environment;

	@Autowired
	private SecurityServiceImpl securityServiceImpl;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Override
	public void run() {
		log.info("Cleaning unused sessions ");
		securityServiceImpl.clearUnusedSession();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Session cleaner job is being scheduled.");
		if (environment.containsProperty("session.cleaner.timer.task.delay")) {
			fixedDelayForTaskMonitor = environment.getProperty("session.cleaner.timer.task.delay");
		}
		threadPoolTaskScheduler.scheduleAtFixedRate(this, new Date(), Long.parseLong(fixedDelayForTaskMonitor));
	}

}
