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

import com.church.task.TaskManager;

import lombok.extern.slf4j.Slf4j;

/**
 * This is a scheduled job for reminding the tasks assigned to users.
 * 
 * @author pradheep
 *
 */
@Slf4j
@Component
public class TaskMonitorJob implements Runnable, InitializingBean {

	private String fixedDelayForTaskMonitor = "80000";
	
	@Autowired
	private Environment environment;

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
		if(environment.containsProperty("task.monitor.fixed.delay.mills")){
			fixedDelayForTaskMonitor = environment.getProperty("task.monitor.fixed.delay.mills");
		}
		threadPoolTaskScheduler.scheduleAtFixedRate(this, new Date(), Long.parseLong(fixedDelayForTaskMonitor));
	}

}
