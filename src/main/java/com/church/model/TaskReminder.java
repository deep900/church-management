package com.church.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;
@Slf4j
public class TaskReminder extends Reminder{

    @Override
    public void remind() {
        log.info("Task Reminder..");
    }
}
