package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DatesExclusionValidatorTest {

    private DatesExclusionValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private GroupRequestMeta groupRequestMeta;

    private final String testInstant = Instant.now().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new DatesExclusionValidator();
    }

    @Test
    void shouldReturnTrue_withAllNull_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(null);
        when(groupRequestMeta.dateTo()).thenReturn(null);
        when(groupRequestMeta.date()).thenReturn(null);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withOnlyDateFrom_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(testInstant);
        when(groupRequestMeta.dateTo()).thenReturn(null);
        when(groupRequestMeta.date()).thenReturn(null);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withOnlyDateTo_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(null);
        when(groupRequestMeta.dateTo()).thenReturn(testInstant);
        when(groupRequestMeta.date()).thenReturn(null);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withOnlyDate_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(null);
        when(groupRequestMeta.dateTo()).thenReturn(null);
        when(groupRequestMeta.date()).thenReturn(testInstant);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withoutDate_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(testInstant);
        when(groupRequestMeta.dateTo()).thenReturn(testInstant);
        when(groupRequestMeta.date()).thenReturn(null);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalse_withDateAndDateFrom_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(testInstant);
        when(groupRequestMeta.dateTo()).thenReturn(null);
        when(groupRequestMeta.date()).thenReturn(testInstant);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalse_withDateAndDateTo_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(null);
        when(groupRequestMeta.dateTo()).thenReturn(testInstant);
        when(groupRequestMeta.date()).thenReturn(testInstant);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalse_withAllDates_successfully() {
        when(groupRequestMeta.dateFrom()).thenReturn(testInstant);
        when(groupRequestMeta.dateTo()).thenReturn(testInstant);
        when(groupRequestMeta.date()).thenReturn(testInstant);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isFalse();
    }
}
