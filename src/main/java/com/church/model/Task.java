package com.church.model;

import com.church.util.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Task {

    private String taskName;

    private Timestamp startTime;

    private int estimatedHours;

    private StatusEnum presentState;

    private int taskSequence;

    private List<Reminder> reminders;
}
