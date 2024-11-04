package com.iprody.lms.arrangement.service.exception;

import java.text.MessageFormat;

public class UpdateFailedException extends RuntimeException {

    public UpdateFailedException(String entity) {
        super(MessageFormat.format("{0} update failed, no row has been updated.", entity));
    }
}
