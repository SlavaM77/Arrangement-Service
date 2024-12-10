package com.iprody.lms.arrangement.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.text.MessageFormat;

@Getter
public class ApiErrorException extends RuntimeException {

    private final String serviceName;
    private final String requestedResource;
    private final HttpStatusCode statusCode;

    public ApiErrorException(String serviceName,
                             String requestedResource,
                             HttpStatusCode statusCode,
                             String message) {
        super(message);
        this.serviceName = serviceName;
        this.requestedResource = requestedResource;
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return MessageFormat.format("Error while requesting service: ''{0}'' for resource: ''{1}'': {2}",
                serviceName,
                requestedResource,
                this.getMessage());
    }
}
