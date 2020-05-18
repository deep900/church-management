/**
 * 
 */
package com.church.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.church.model.Event;
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
		List<Event> events = churchEventService.getAllEvents();
		ResponseModel model = getDefaultResponseModel();
		model.setObject(events);
		return model;
	}
}
