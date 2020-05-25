/**
 * 
 */
package com.church.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.church.management.publish.TaskAndEventPublisher;
import com.church.model.ChurchEvent;
import com.church.model.ResponseModel;
import com.church.serviceimpl.ChurchEventServiceImpl;

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

	@GetMapping("/api/getEvents")
	public @ResponseBody ResponseModel getAllEvents() {
		log.info("Inside getEvents method");
		List<ChurchEvent> events = churchEventService.getAllEvents();
		ResponseModel model = getDefaultResponseModel();
		model.setObject(events);
		return model;
	}

	@PostMapping("/api/addEvent")
	public @ResponseBody ResponseModel addChurchEvent(@RequestBody ChurchEvent churchEvent) {
		log.info("Adding a church event :" + churchEvent.toString());
		String response = churchEventService.addEvent(churchEvent);		
		if(null == response){
			log.error("Error while saving the church event");
			return getFailureResponseModel();
		}
		return  getDefaultResponseModel();
	}
}
