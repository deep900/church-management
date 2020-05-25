/**
 * 
 */
package com.church.task;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class TaskMonitorJob implements Runnable, InitializingBean {

	@Value("${task.monitor.fixed.delay.mills}")
	private long fixedDelayForTaskMonitor;

	@Autowired
	private TaskManager taskManager;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Override
	public void run() {
		log.info("Running the schedule job at " + new Date());
		taskManager.remindTasks();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Scheduling a task monitor job at fixed interval ");
		threadPoolTaskScheduler.scheduleWithFixedDelay(this, new Date(), fixedDelayForTaskMonitor);
	}

}
