package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.dto.request.MeetRequestDto;
import com.iprody.lms.arrangement.service.dto.request.ScheduleDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ScheduleValidatorTest {

    private ScheduleValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ScheduleDto scheduleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ScheduleValidator();
    }

    @Test
    void shouldReturnTrue_withNullScheduledFor_successfully() {
        List<MeetRequestDto> meets = List.of(
                new MeetRequestDto(Instant.now(), "")
        );
        when(scheduleDto.scheduledFor()).thenReturn(null);
        when(scheduleDto.meets()).thenReturn(meets);

        boolean isValid = validator.isValid(scheduleDto, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withEmptyMeets_successfully() {
        when(scheduleDto.scheduledFor()).thenReturn(Instant.now());
        when(scheduleDto.meets()).thenReturn(List.of());

        boolean isValid = validator.isValid(scheduleDto, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalse_withScheduledForAfterMeet_successfully() {
        Instant scheduledFor = Instant.now();
        List<MeetRequestDto> meets = List.of(
                new MeetRequestDto(scheduledFor.minusSeconds(1), "")
        );

        when(scheduleDto.scheduledFor()).thenReturn(scheduledFor);
        when(scheduleDto.meets()).thenReturn(meets);

        boolean isValid = validator.isValid(scheduleDto, context);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnTrue_withAllMeetsAfterScheduledFor_successfully() {
        Instant scheduledFor = Instant.now();
        List<MeetRequestDto> meets = List.of(
                new MeetRequestDto(scheduledFor.plusSeconds(1), "")
        );

        when(scheduleDto.scheduledFor()).thenReturn(scheduledFor);
        when(scheduleDto.meets()).thenReturn(meets);

        boolean isValid = validator.isValid(scheduleDto, context);

        assertThat(isValid).isTrue();
    }
}
