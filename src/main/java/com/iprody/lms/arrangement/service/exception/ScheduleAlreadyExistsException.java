package com.iprody.lms.arrangement.service.exception;

import java.text.MessageFormat;

public class ScheduleAlreadyExistsException extends RuntimeException {

    public ScheduleAlreadyExistsException(String groupGuid) {
        super(MessageFormat.format("Schedule already exists for group with groupGuid ''{0}''", groupGuid));
    }
}
