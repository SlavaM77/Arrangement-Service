package com.iprody.lms.arrangement.service.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class DateValidatorTest {

    private DateValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new DateValidator();
    }

    @Test
    void shouldReturnTrue_withValidDate_successfully() {
        String date = "2024-01-01";

        boolean isValid = validator.isValid(date, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withNull_successfully() {
        boolean isValid = validator.isValid(null, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalse_withInvalidDateFormat_successfully() {
        String date = "2024:01:01";

        boolean isValid = validator.isValid(date, context);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalse_withInvalidDate_successfully() {
        String date = "2024-13-01";

        boolean isValid = validator.isValid(date, context);

        assertThat(isValid).isFalse();
    }
}
