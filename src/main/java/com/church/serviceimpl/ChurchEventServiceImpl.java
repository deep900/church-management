/**
 * 
 */
package com.church.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.church.data.repository.EventRepository;
import com.church.management.publish.TaskAndEventPublisher;
import com.church.model.ChurchEvent;
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

	public List<ChurchEvent> getAllEvents() {
		return eventRepository.findAll();
	}

	public String addEvent(Event event) {
		log.info("Saving a new event:" + event.toString());
		if (event instanceof ChurchEvent) {
			ChurchEvent churchEvent = (ChurchEvent) event;
			churchEvent = eventRepository.save(churchEvent);
			taskAndEventPublisher.publishEvent(churchEvent);
			return churchEvent.getId();
		}
		return null;
	}

	public void updateEvent(ChurchEvent eventObj) {
		eventRepository.save(eventObj);
	}	
}
