/**
 * 
 */
package com.church.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.church.management.publish.TaskAndEventPublisher;
import com.church.model.ChurchEvent;
import com.church.model.Event;
import com.church.service.EventService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@Service
public class ChurchEventServiceImpl implements EventService {

	@Autowired
	private MongoOperations mongoOps;
	
	@Autowired
	private TaskAndEventPublisher taskAndEventPublisher;

	@Override
	public List<ChurchEvent> getAllEvents() {
		List<ChurchEvent> results = mongoOps.findAll(ChurchEvent.class);
		log.info("Found :" + results.size() + " records");
		return results;
	}

	@Override
	public Event getEventById(String id) {		
		return null;
	}

	@Override
	public String addEvent(Event event) {
		if(event instanceof ChurchEvent){
		ChurchEvent churchEvent = (ChurchEvent) event;
		churchEvent = mongoOps.save(churchEvent);
		taskAndEventPublisher.publishEvent(churchEvent);
		return churchEvent.getId();
		}
		return null;
	}

	@Override
	public String deleteEvent(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateEvent(Event event) {
		// TODO Auto-generated method stub
		return null;
	}

}
