package com.iprody.lms.arrangement.service.validation.annotation;

import com.iprody.lms.arrangement.service.validation.validator.ScheduleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ScheduleValidator.class)
public @interface ValidSchedule {

    String message() default "Group 'scheduledFor' time must be before any meet's 'scheduledFor' time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
