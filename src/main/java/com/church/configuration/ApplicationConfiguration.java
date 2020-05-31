package com.church.configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.CustomConversions.StoreConversions;
import org.springframework.data.convert.Jsr310Converters.LocalDateTimeToDateConverter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.church.task.TaskManager;
import com.church.task.TaskMonitorJob;
import com.church.task.TaskReminderRunner;

@Configuration
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
	public TaskManager getTaskManager(){
		return new TaskManager();
	}
	
}
