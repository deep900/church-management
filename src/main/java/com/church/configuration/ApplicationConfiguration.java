package com.church.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.CustomConversions.StoreConversions;
import org.springframework.data.convert.Jsr310Converters.LocalDateTimeToDateConverter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.church.management.publish.TaskAndEventListener;
import com.church.model.EmailTaskReminder;
import com.church.notify.EmailNotifyService;
import com.church.task.TaskAllotmentManager;
import com.church.task.TaskManager;
import com.church.task.TaskMonitorJob;
import com.church.task.TaskReminderRunner;
import com.church.util.DelegateHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApplicationConfiguration {

	@Autowired
	private MongoDbFactory mongoDbFactory;

	@Bean
	public MappingMongoConverter getDefaultMongoConverter() throws Exception {
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory),
				new MongoMappingContext());
		converter.setCustomConversions(customConversions());
		return converter;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, getDefaultMongoConverter());
		return mongoTemplate;
	}

	@Bean
	public CustomConversions customConversions() {
		List collection = new ArrayList();
		collection.add(LocalDateTimeToDateConverter.INSTANCE);
		collection.add(CustomDateTimeConverter.INSTANCE);
		collection.add(CustomBinaryToByteConverter.INSTANCE);
		CustomConversions customConversions = new CustomConversions(StoreConversions.NONE, collection);
		return customConversions;
	}

	enum CustomDateTimeConverter implements Converter<Date, Timestamp> {
		INSTANCE;
		@Override
		public Timestamp convert(Date source) {
			return new Timestamp(source.getTime());
		}
	}

	enum CustomBinaryToByteConverter implements Converter<Binary, byte[]> {
		INSTANCE;
		@Override
		public byte[] convert(Binary source) {
			return source.getData();
		}
	}

	@Bean
	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setQueueCapacity(10000);
		executor.setMaxPoolSize(15);
		executor.setThreadNamePrefix("CM");
		return executor;
	}

	@Bean
	public TaskReminderRunner getTaskReminderRunner() {
		TaskReminderRunner runner = new TaskReminderRunner();
		return runner;
	}

	@Bean
	public ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		return taskScheduler;
	}

	@Bean
	public TaskMonitorJob getTaskMonitorJob() {
		return new TaskMonitorJob();
	}

	@Bean
	public TaskManager getTaskManager() {
		return new TaskManager();
	}

	@Bean
	public TaskAllotmentManager getTaskAllotmentManager() {
		return new TaskAllotmentManager();
	}

	@Bean
	@DependsOn("delegateHandler")
	public TaskAndEventListener getTaskAndEventListener() {
		return new TaskAndEventListener();
	}

	@Bean(value = "delegateHandler")
	public DelegateHandler getDelgateHandler() {
		return new DelegateHandler();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		Properties properties = loadEmailProperties();
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(properties.getProperty("email.host"));
		mailSender.setPort(Integer.parseInt(properties.getProperty("email.sender.port")));
		mailSender.setUsername(properties.getProperty("username"));
		mailSender.setPassword(properties.getProperty("password"));
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", properties.get("mail.transport.protocol"));
		props.put("mail.smtp.auth", properties.get("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", properties.get("mail.smtp.starttls.enable"));
		props.put("mail.debug", properties.get("mail.debug"));
		return mailSender;
	}

	private Properties loadEmailProperties() {
		String path = "/opt/tomcat/conf/email/email.properties";
		File fObj = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fObj);
		} catch (FileNotFoundException e1) {
			log.error("Error while loading email properties", e1);
		}
		Properties prop = new Properties();
		try {
			prop.load(fis);
		} catch (IOException e) {
			log.error("Error while loading the email properties", e);
		}
		return prop;
	}

		
	@Bean
	public EmailNotifyService getEmailNotifyService(){
		return new EmailNotifyService();
	}
}
