package com.church.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.church.util.StatusEnum;

import lombok.Data;

@Data
@Document
public abstract class Task {

	@Id
	public String id;
	
	public String eventId;
	
    public String taskName;

    public Timestamp startTime;

    public double estimatedHours;

    public StatusEnum presentState;

    public int taskSequence;
    
    public List<String> comments;

    public List<Reminder> reminders;
}
