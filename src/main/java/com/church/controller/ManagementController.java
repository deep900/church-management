/**
 * 
 */
package com.church.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.church.model.Event;
import com.church.model.ResponseModel;
import com.church.model.Task;
import com.church.serviceimpl.ChurchEventServiceImpl;
import com.church.serviceimpl.TaskServiceImpl;
import com.church.util.ApplicationConstants;
import com.church.util.EventTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@RestController
public class ManagementController extends BaseController {

	@Autowired
	private ChurchEventServiceImpl churchEventService;

	@Autowired
	private TaskServiceImpl taskService;

	@PreAuthorize("hasRole('ROLE_ADMIN_USER')")
	@GetMapping("/api/getEvents")
	public @ResponseBody ResponseModel getAllEvents() {
		log.info("Inside getEvents method");
		List<Event> events = churchEventService.getAllEvents();
		ResponseModel model = getDefaultResponseModel();
		model.setObject(events);
		return model;
	}

	/*
	 * @PostMapping("/api/addEvent") public @ResponseBody ResponseModel
	 * addChurchEvent(@RequestBody Event event) { log.info("Adding a  event :" +
	 * event.toString()); String response = churchEventService.addEvent(event);
	 * if (null == response) { log.error("Error while saving the church event");
	 * return getFailureResponseModel(); } return getDefaultResponseModel(); }
	 */

	@PreAuthorize("hasRole('ROLE_ADMIN_USER')")
	@GetMapping("/api/getEventTypes")
	public @ResponseBody ResponseModel getAllEventsTypes() {
		log.info("Inside getEvent types method");
		List<String> eventTypeList = new ArrayList<String>();
		eventTypeList.add(EventTypeEnum.BirthdayEvent.label);
		eventTypeList.add(EventTypeEnum.WeddingAnniversary.label);
		ResponseModel model = getDefaultResponseModel();
		model.setObject(eventTypeList);
		return model;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN_USER')")
	@PutMapping("/api/admin/addNewEvent")
	public @ResponseBody ResponseModel addNewEvent(@RequestBody Event eventObj) {
		log.info("Trying to add a new event:" + eventObj.toString());
		String id = churchEventService.addEvent(eventObj);
		ResponseModel model = getDefaultResponseModel();
		if (id == null) {
			model.setResponseCode(ApplicationConstants.INTERNAL_SERVER_ERROR);
			model.setResponseMessage(ApplicationConstants.SERVER_ERROR);
			log.error("Error while adding a new event.");
			return model;
		} else {
			HashMap eventIdMap = new HashMap();
			eventIdMap.put("eventId", id);
			model.setObject(eventIdMap);
		}
		return model;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN_USER')")
	@GetMapping("/api/admin/getAllTasks")
	public @ResponseBody ResponseModel getAllTasks() {
		log.info("Trying to fetch all tasks assigned to all users");
		ResponseModel model = getDefaultResponseModel();
		List<Task> taskList = taskService.getAllTasks();
		model.setObject(taskList);
		return model;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/api/getTasksForUser")
	public @ResponseBody ResponseModel getTasksForUser(HttpServletRequest servletRequest) {
		String emailAddress = servletRequest.getParameter("email");
		log.info("Trying to fetch all tasks assigned to :" + emailAddress);
		ResponseModel model = getDefaultResponseModel();
		List<Task> taskList = taskService.getTaskByEmailAddress(emailAddress);
		model.setObject(taskList);
		return model;
	}

}
