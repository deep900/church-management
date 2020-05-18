package com.church.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Reminder {

    public Timestamp reminderTime;

    public abstract void remind();

}
