package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MeetStatus;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.dto.request.MarkMeetCompletedRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateScheduleRequestDto;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ScheduleProcessingServiceTest {

    @InjectMocks
    private ScheduleProcessingService service;

    @Mock
    private ScheduleCalculator scheduleCalculator;

    private Schedule schedule;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        schedule = new Schedule(new ArrayList<>(collectTestSchedule()));
    }

    @Test
    void shouldRecalculateSchedule_withValidData_successfully() {
        Instant canceledTime = Instant.from(OffsetDateTime.of(2024, 10, 18, 11, 0, 0, 0, ZoneOffset.UTC));
        UpdateScheduleRequestDto dto = new UpdateScheduleRequestDto(
                UUID.randomUUID().toString(),
                canceledTime
        );

        int initSize = schedule.getMeets().size();

        Instant newTime = Instant.from(OffsetDateTime.of(2024, 10, 25, 11, 0, 0, 0, ZoneOffset.UTC));
        when(scheduleCalculator.calculateAdditionalMeetDate(schedule.getMeets())).thenReturn(newTime);

        Schedule result = service.recalculateSchedule(schedule, dto);

        assertThat(result).isNotNull();
        assertThat(result.getMeets())
                .hasSize(initSize)
                .extracting(Meet::getScheduledFor)
                .doesNotContain(canceledTime)
                .contains(newTime);
        assertThat(result.getMeets())
                .filteredOn(meet -> meet.getStatus() == MeetStatus.RESCHEDULED)
                .hasSize(3);
    }

    @Test
    void shouldThrow_withoutMeetWithCanceledTime() {
        Instant canceledTime = Instant.from(OffsetDateTime.of(2024, 10, 19, 11, 0, 0, 0, ZoneOffset.UTC));
        UpdateScheduleRequestDto dto = new UpdateScheduleRequestDto(
                UUID.randomUUID().toString(),
                canceledTime
        );

        assertThatThrownBy(() -> service.recalculateSchedule(schedule, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldMarkMeetCompleted_withValidData_successfully() {
        Instant scheduledFor = Instant.from(OffsetDateTime.of(2024, 10, 18, 11, 0, 0, 0, ZoneOffset.UTC));
        Instant happenedAt = scheduledFor.plus(1, ChronoUnit.HOURS);
        MarkMeetCompletedRequestDto dto = new MarkMeetCompletedRequestDto(
                UUID.randomUUID().toString(),
                scheduledFor,
                happenedAt
        );

        int initSize = schedule.getMeets().size();

        Schedule result = service.markMeetCompleted(schedule, dto);

        assertThat(result).isNotNull();
        assertThat(result.getMeets())
                .hasSize(initSize)
                .filteredOn(meet -> meet.getStatus() == MeetStatus.HAPPENED)
                .satisfies(happened -> {
                    assertThat(happened).hasSize(1);
                    assertThat(happened.getFirst().getScheduledFor()).isEqualTo(scheduledFor);
                    assertThat(happened.getFirst().getHappenedAt()).isEqualTo(happenedAt);
                });
    }

    @Test
    void shouldThrow_withoutMeetWithScheduledTime() {
        Instant scheduledFor = Instant.from(OffsetDateTime.of(2024, 10, 19, 11, 0, 0, 0, ZoneOffset.UTC));
        Instant happenedAt = scheduledFor.plus(1, ChronoUnit.HOURS);
        MarkMeetCompletedRequestDto dto = new MarkMeetCompletedRequestDto(
                UUID.randomUUID().toString(),
                scheduledFor,
                happenedAt
        );

        assertThatThrownBy(() -> service.markMeetCompleted(schedule, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDoNothing_withScheduledForTimeBeforeFirstMeet_successfully() {
        Instant scheduledFor = Instant.from(OffsetDateTime.of(2024, 10, 10, 11, 0, 0, 0, ZoneOffset.UTC));
        List<Meet> meets = schedule.getMeets();
        assertThatNoException().isThrownBy(() -> service.validateSchedule(scheduledFor, meets));
    }

    @Test
    void shouldDoNothing_withoutScheduledForTime_successfully() {
        List<Meet> meets = schedule.getMeets();
        assertThatNoException().isThrownBy(() -> service.validateSchedule(null, meets));
    }

    @Test
    void shouldDoNothing_withoutMeets_successfully() {
        Instant scheduledFor = Instant.from(OffsetDateTime.of(2024, 10, 10, 11, 0, 0, 0, ZoneOffset.UTC));
        assertThatNoException().isThrownBy(() -> service.validateSchedule(scheduledFor, null));
    }

    @Test
    void shouldDoNothing_withNullParams_successfully() {
        assertThatNoException().isThrownBy(() -> service.validateSchedule(null, null));
    }

    @Test
    void shouldThrow_withScheduledForTimeAfterFirstMeet() {
        Instant scheduledFor = Instant.from(OffsetDateTime.of(2024, 10, 20, 11, 0, 0, 0, ZoneOffset.UTC));
        List<Meet> meets = schedule.getMeets();
        assertThatThrownBy(() -> service.validateSchedule(scheduledFor, meets))
                .isInstanceOf(IllegalStateException.class);
    }

    private static List<Meet> collectTestSchedule() {
        return List.of(
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 14, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build(),
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 16, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build(),
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 18, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build(),
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 21, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build(),
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 23, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build());
    }
}
