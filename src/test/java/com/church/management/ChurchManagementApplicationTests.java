package com.church.management;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import com.church.data.repository.TaskRepository;
import com.church.model.Task;
import com.church.model.task.DefaultTask;
import com.church.serviceimpl.TaskServiceImpl;
import com.church.util.StatusEnum;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
class ChurchManagementApplicationTests {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskServiceImpl taskService;

	private void createATask() {
		Task taskObj = new DefaultTask();
		taskObj.setPresentState(StatusEnum.inprogress);
		taskObj.setEstimatedHours(1.0);
		taskObj.setEventId("1");
		taskObj.setTaskName("Sample task");
		Task obj = taskRepository.save(taskObj);
		System.out.println("Task created" + obj.getId());
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testTask() {
		createATask();
		Assert.notEmpty(taskService.getAllTasks());
	}

	@AfterTestMethod
	public void cleanUp() {
		System.out.println("-- Cleaning up -------");
		taskRepository.deleteAll();
	}

}
