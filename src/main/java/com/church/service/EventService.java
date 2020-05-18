/**
 * 
 */
package com.church.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.church.model.Event;

/**
 * @author pradheep
 *
 */
@Service
public interface EventService<T extends Event> {
	
	public List<T> getAllEvents();
	
	public Event getEventById(String id);
	
	public String addEvent(T event);
	
	public String deleteEvent(String id);
	
	public String updateEvent(T event);
}
