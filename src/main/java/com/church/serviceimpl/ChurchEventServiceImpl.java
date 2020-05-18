/**
 * 
 */
package com.church.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.church.data.EventsRepository;
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
	
	private EventsRepository eventsRepository;

	@Override
	public List getAllEvents() {
		List<ChurchEvent> results = eventsRepository.findAll();
		log.info("Found [" + results.size() + "] events");
		return results;
	}

	@Override
	public Event getEventById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addEvent(Event event) {
		// TODO Auto-generated method stub
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
