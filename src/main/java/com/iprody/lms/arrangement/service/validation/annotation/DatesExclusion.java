package com.iprody.lms.arrangement.service.validation.annotation;

import com.iprody.lms.arrangement.service.validation.validator.DatesExclusionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatesExclusionValidator.class)
public @interface DatesExclusion {

    String message() default "If 'dateFrom' or 'dateTo' is present, 'date' must not exist.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
