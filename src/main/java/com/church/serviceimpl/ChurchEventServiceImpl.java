/**
 * 
 */
package com.church.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.church.data.repository.EventRepository;
import com.church.management.publish.TaskAndEventPublisher;
import com.church.model.Event;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@Service
public class ChurchEventServiceImpl {

	@Autowired
	private TaskAndEventPublisher taskAndEventPublisher;

	@Autowired
	private EventRepository eventRepository;

	public List<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	public String addEvent(Event event) {
		log.info("Saving a new event:" + event.toString());
		try {			
			event = eventRepository.save(event);
			taskAndEventPublisher.publishEvent(event);
			return event.getId();
		} catch (Exception err) {
			log.error("Error while adding the event.", err);
		}
		return null;
	}

	public void updateEvent(Event eventObj) {
		eventRepository.save(eventObj);
	}
}
