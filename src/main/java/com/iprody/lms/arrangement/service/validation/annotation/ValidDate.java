package com.iprody.lms.arrangement.service.validation.annotation;

import com.iprody.lms.arrangement.service.validation.validator.DateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface ValidDate {

    String message() default "Invalid date or date format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
