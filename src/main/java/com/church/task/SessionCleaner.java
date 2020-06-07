/**
 * 
 */
package com.church.task;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.church.serviceimpl.SecurityServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@Component
public class SessionCleaner implements Runnable,InitializingBean {
	
	@Value("${session.cleaner.timer.task.delay}")
	private long fixedDelayForTaskMonitor;
	
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
		threadPoolTaskScheduler.scheduleAtFixedRate(this, new Date(), fixedDelayForTaskMonitor);
	}

	
}
