package com.iprody.lms.arrangement.service.validation.annotation;

import com.iprody.lms.arrangement.service.validation.validator.UpdateGroupValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdateGroupValidator.class)
public @interface ValidUpdate {

    String message() default "There is no one parameter for group update";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
