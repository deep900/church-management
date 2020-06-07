package com.church.model.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@Component
public class EventSchemaLoader extends DefaultHandler implements InitializingBean {

	private List<EventSchema> eventSchemaList = new ArrayList<EventSchema>();
	
	private List<TaskSchema> taskSchemaList = null;

	private void loadEvents(InputStream inputStream) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(inputStream, this);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String elementName, Attributes attributes)
			throws SAXException {
		if (elementName.equalsIgnoreCase("event")) {
			EventSchema eventSchema = new EventSchema();
			taskSchemaList = new ArrayList<TaskSchema>();
			String eventName = attributes.getValue("name");
			eventSchema.setName(eventName);
			eventSchema.setTaskSchemaList(taskSchemaList);
			eventSchemaList.add(eventSchema);			
		}
		if (elementName.equalsIgnoreCase("task")) {
			TaskSchema taskSchema = new TaskSchema();			
			String taskName = attributes.getValue("name");
			String timePlannedInDays = attributes.getValue("timePlannedInDays");
			String assignation = attributes.getValue("assignation");
			String maxAssigned = attributes.getValue("maxAssigned");
			String sequence = attributes.getValue("sequence");
			String isParallel = attributes.getValue("isParallel");
			taskSchema.setAssignation(assignation);
			taskSchema.setMaxAssigned(Integer.parseInt(maxAssigned));
			taskSchema.setName(taskName);
			taskSchema.setParallel(Boolean.valueOf(isParallel));
			taskSchema.setTimePlannedInDays(Double.parseDouble(timePlannedInDays));
			taskSchema.setSequence(Integer.parseInt(sequence));
			taskSchemaList.add(taskSchema);
		}
	}

	public List<EventSchema> getEventSchemaList() {
		log.info("Printing the event schema list: "+ eventSchemaList.toString());
		return eventSchemaList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource res = new ClassPathResource("event-task-mapper.xml");
		loadEvents(res.getInputStream());	
	}
}
