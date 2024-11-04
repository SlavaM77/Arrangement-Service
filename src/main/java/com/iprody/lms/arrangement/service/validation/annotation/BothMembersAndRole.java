package com.iprody.lms.arrangement.service.validation.annotation;

import com.iprody.lms.arrangement.service.validation.validator.BothMembersAndRoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BothMembersAndRoleValidator.class)
public @interface BothMembersAndRole {

    String message() default "'memberIds' and 'role' must either be present or absent together";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
