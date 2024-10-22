package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.validation.annotation.ValidDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.iprody.lms.arrangement.service.validation.constants.ValidatorConstants.YYYY_MM_DD_DATE_FORMAT;

/**
 * Validator that checks whether a given date string is valid according to the {@code YYYY-MM-DD} format.
 * This validator is intended to be used with a custom annotation {@link ValidDate},
 * which applies the validation logic to String param.
 */

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private static final String PATTERN = YYYY_MM_DD_DATE_FORMAT;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            LocalDate.parse(value, DateTimeFormatter.ofPattern(PATTERN));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
