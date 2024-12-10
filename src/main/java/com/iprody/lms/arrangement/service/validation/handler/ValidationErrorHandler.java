package com.iprody.lms.arrangement.service.validation.handler;

import com.iprody.lms.arrangement.service.exception.ApiErrorException;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationErrorHandler {

    @ExceptionHandler({WebExchangeBindException.class})
    public Mono<ResponseEntity<Map<String, String>>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return Mono.just(ResponseEntity.badRequest().body(errors));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public Mono<ResponseEntity<String>> handleIllegalArgumentException(RuntimeException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public Mono<ResponseEntity<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
    }

    @ExceptionHandler({ApiErrorException.class})
    public Mono<ResponseEntity<String>> handleClientErrorException(ApiErrorException ex) {
        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(ex.getErrorMessage()));
    }

    @ExceptionHandler({Exception.class})
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()));
    }
}
