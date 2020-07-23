/**
 * 
 */
package com.church.task;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.church.model.Reminder;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Setter
@Getter
@Slf4j
public class TaskReminderRunner implements Runnable {

	private List<Reminder> reminders;

	@Override
	public void run() {
		log.info("Running the reminder tasks");
		if (null == getReminders() || getReminders().isEmpty()) {
			log.info("No runnable reminders");
			return;
		}
		/*List<Reminder> executableReminders = reminders.stream().filter(x -> x.getReminderTime().after(new Date()))
				.collect(Collectors.toList());*/
		log.info("Running :" + reminders.size() + " reminders.");
		reminders.forEach(x -> x.remind());
	}

}
