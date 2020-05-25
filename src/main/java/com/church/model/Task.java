package com.church.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.church.util.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public abstract class Task {

	@Id
	private String id;
	
	private String eventId;
	
    private String taskName;

    private Timestamp startTime;

    private int estimatedHours;

    private StatusEnum presentState;

    private int taskSequence;
    
    private List<String> comments;

    private List<Reminder> reminders;
}
