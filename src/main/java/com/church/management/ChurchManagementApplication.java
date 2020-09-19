package com.church.management;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.church.configuration.ApplicationConfiguration;
import com.church.data.repository.EventRepository;
import com.church.data.repository.TaskRepository;
import com.church.model.ApplicationUser;
import com.church.model.EmailTaskReminder;
import com.church.model.Event;
import com.church.model.ReOccurance;
import com.church.model.Reminder;
import com.church.security.SecurityConstants;
import com.church.security.SecurityUserDetails;
import com.church.task.TaskAllotmentManager;
import com.church.task.TaskGenerator;
import com.church.util.ApplicationConstants;
import com.church.util.EncryptUtility;
import com.church.util.EventTypeEnum;
import com.church.util.FrequencyTypeEnum;
import com.church.util.SecurityUtility;
import com.church.util.StatusEnum;
import com.church.util.UserTypeEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = { "com.church", "com.church.task" })
@Import(ApplicationConfiguration.class)
@EnableMongoRepositories(basePackageClasses = { TaskRepository.class, EventRepository.class })
public class ChurchManagementApplication {

	@Autowired
	private MongoTemplate template;

	@Autowired
	private MongoOperations mongoOps;

	@Autowired
	private SecurityUtility securityUtil;

	@Autowired
	private EncryptUtility encryptUtil;

	@Autowired
	private TaskGenerator taskGenerator;

	@Autowired
	private TaskAllotmentManager taskAllotmentManager;
	
	@Autowired
	private JavaMailSender mailSender;

	@PostConstruct
	public void init() {
		try {
			//testEmail();
			//addEvents();
			taskAllotmentManager.init();
			Thread.currentThread().sleep(5000);
			taskAllotmentManager.loadTasksForAssignment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Record saved.");
	}
	
	public void testEmail(){
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("admin@localhost");
		simpleMailMessage.setTo("deep_90@yahoo.com");
		simpleMailMessage.setSentDate(new Date());
		simpleMailMessage.setSubject("Test email : Email Reminder");
		simpleMailMessage.setText("Content here ..");
		mailSender.send(simpleMailMessage);
	}

	private void addEvents() {
		/*mongoOps.dropCollection("churchEvent");
		mongoOps.createCollection("churchEvent");
		Event event = new Event();
		event.setId(UUID.randomUUID().toString());
		event.setEventName(ApplicationConstants.BIRTHDAY_GREETING_EVENT);
		event.setRemarks("Birthday for pradheep");
		event.setEventStatus(StatusEnum.scheduled);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		calendar.setTime(new Date());
		calendar.set(1979, 07, 22, 0, 0, 0);
		event.setStartTime(new Timestamp(calendar.getTime().getTime()));
		calendar.set(1979, 07, 22, 23, 59);
		event.setEndTime(new Timestamp(calendar.getTime().getTime()));
		event.setEventType(EventTypeEnum.BirthdayEvent);
		ReOccurance reOccurance = new ReOccurance();
		reOccurance.setActive(true);
		reOccurance.setReOccuranceFrequency(FrequencyTypeEnum.yearly);
		event.setReOccurance(reOccurance);
		List<Reminder> reminderList = new ArrayList<Reminder>();
		EmailTaskReminder reminder1 = new EmailTaskReminder();
		reminder1.setActive(true);
		reminder1.setReminderCnt(1);
		calendar.add(GregorianCalendar.DAY_OF_MONTH, -7);
		reminder1.setReminderTime(new Timestamp(calendar.getTime().getTime()));

		EmailTaskReminder reminder2 = new EmailTaskReminder();
		reminder2.setActive(true);
		reminder2.setReminderCnt(2);
		calendar.add(GregorianCalendar.DAY_OF_MONTH, 3);
		reminder2.setReminderTime(new Timestamp(calendar.getTime().getTime()));
		reminderList.add(reminder1);
		reminderList.add(reminder2);
		event.setReminders(reminderList);	*/	

		mongoOps.dropCollection("applicationUser");
		mongoOps.createCollection("applicationUser");
		List<GrantedAuthority> engineerAuthorities = new ArrayList<GrantedAuthority>();
		engineerAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_TASK_WORKER));
		engineerAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_USER));
		createApplicationUser("Pradheep", "deep90@gmail.com", engineerAuthorities, UserTypeEnum.engineer);

		List<GrantedAuthority> adminAuthorities = new ArrayList<GrantedAuthority>();
		adminAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_ADMIN_USER));
		adminAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_USER));
		createApplicationUser("Sushil", "deep_90@yahoo.com", adminAuthorities, UserTypeEnum.administrator);

		List<GrantedAuthority> reviewAuthorities = new ArrayList<GrantedAuthority>();
		reviewAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_TASK_REVIEWER));
		reviewAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_USER));
		createApplicationUser("John Britto", "deep_90@yahoo.com", reviewAuthorities, UserTypeEnum.reviewer);
		//template.save(event);

		taskGenerator.prepareAllEvents();
	}

	private void createApplicationUser(String name, String email, List<GrantedAuthority> grantedAuthorities,
			UserTypeEnum userType) {
		ApplicationUser user = new ApplicationUser();
		ArrayList userTypeList = new ArrayList();
		userTypeList.add(userType);
		user.setUserTypeList(userTypeList);
		user.setContactNumber("87990011");
		user.setCountryCode("+65");
		user.setEmailAddress(email);
		user.setName(name);
		String salt = securityUtil.generateUUID();
		String encrypted = encryptUtil.encrypt("Welcome@1", salt);
		byte[] saltBytes = salt.getBytes();
		log.info(salt);
		// ArrayUtils.reverse(saltBytes);
		// log.info("Rev" + new String(saltBytes));
		user.setSalt(saltBytes);

		SecurityUserDetails userDetails = new SecurityUserDetails();
		userDetails.setEnabled(true);
		userDetails.setPassword(encrypted);
		userDetails.setGrantedAuthorityList(grantedAuthorities);
		user.setUserDetails(userDetails);
		user.setPriority(1);
		template.save(user);
	}

	public static void main(String[] args) {
		SpringApplication.run(ChurchManagementApplication.class, args);
		log.info("Starting the application ..");
	}

}
