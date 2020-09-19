/**
 * 
 */
package com.church.jobs;

import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.church.task.TaskGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is to create tasks for the events on its time.
 * 
 * This will run every day 00 Hrs.
 * 
 * @author pradheep
 *
 */
@Component
@Slf4j
public class TaskCreatorJob implements Runnable, InitializingBean {

	private String fixedDelayForTaskCreation = "86400000";
	
	@Autowired
	private Environment environment;

	@Autowired
	private TaskGenerator taskGenerator;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Override
	public void run() {
		log.info("Trying to create tasks for all events");
		taskGenerator.prepareAllEvents();
	}

	private Date getStartDate() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
		calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
	public void afterPropertiesSet() {
		Date startDate = getStartDate();
		log.info("Task creator job is being scheduled at " + startDate);
		if(environment.containsProperty("task.creation.fixed.delay.mills")){
			fixedDelayForTaskCreation = environment.getProperty("task.creation.fixed.delay.mills");
		}
		threadPoolTaskScheduler.scheduleAtFixedRate(this, startDate, Long.parseLong(fixedDelayForTaskCreation));
	}

}
