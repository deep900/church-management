package com.church.management;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.church.configuration.ApplicationConfiguration;
import com.church.model.ChurchEvent;
import com.church.model.ReOccurance;
import com.church.model.Reminder;
import com.church.model.EmailTaskReminder;
import com.church.util.EventTypeEnum;
import com.church.util.FrequencyTypeEnum;
import com.church.util.StatusEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages={"com.church"})
@Import(ApplicationConfiguration.class)
public class ChurchManagementApplication {
	
	@Autowired
	private MongoTemplate template;
	
	@Autowired
	private MongoOperations mongoOps;

	@PostConstruct
	public void init() {
		mongoOps.dropCollection("churchEvent");
		mongoOps.createCollection("churchEvent");
		ChurchEvent event = new ChurchEvent();		
		event.setEventName("birthday-pradheep-utc");
		event.setEventStatus(StatusEnum.scheduled);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		calendar.setTime(new Date());
		calendar.set(1979, 0, 7,0,0,0);
		event.setStartTime(new Timestamp(calendar.getTime().getTime()));
		calendar.set(1979,0,7,23,59);
		event.setEndTime(new Timestamp(calendar.getTime().getTime()));
		event.setEventType(EventTypeEnum.birthday_event);
		ReOccurance reOccurance = new ReOccurance();
		reOccurance.setActive(true);
		reOccurance.setReOccuranceFrequency(FrequencyTypeEnum.yearly);
		event.setReOccurance(reOccurance);
		List<Reminder> reminderList = new ArrayList<Reminder>();
		EmailTaskReminder reminder1 = new EmailTaskReminder();
		reminder1.setReminderCnt(1);
		calendar.add(GregorianCalendar.DAY_OF_MONTH,-7);
		reminder1.setReminderTime(new Timestamp(calendar.getTime().getTime()));
		
		EmailTaskReminder reminder2 = new EmailTaskReminder();
		reminder2.setReminderCnt(2);
		calendar.add(GregorianCalendar.DAY_OF_MONTH,3);
		reminder2.setReminderTime(new Timestamp(calendar.getTime().getTime()));
		reminderList.add(reminder1);
		reminderList.add(reminder2);
		event.setReminders(reminderList);
		template.save(event);
		log.info("Record saved.");
	}

	public static void main(String[] args) {
		SpringApplication.run(ChurchManagementApplication.class, args);
		log.info("Starting the application ..");
	}

}
