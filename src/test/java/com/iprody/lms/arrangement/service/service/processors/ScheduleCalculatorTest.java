package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MeetStatus;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleCalculatorTest {

    private final ScheduleCalculator calculator = new ScheduleCalculator();

    @Test
    void shouldCalculateAdditionalMeetDate_withRegularSchedule_successfully() {
        List<Meet> meets = List.of(
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
                        .build(),
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 25, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build()
        );

        Instant result = calculator.calculateAdditionalMeetDate(meets);

        Instant expected = Instant.from(OffsetDateTime.of(2024, 10, 28, 11, 0, 0, 0, ZoneOffset.UTC));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldCalculateAdditionalMeetDate_withEndInTheMiddleOfWeek_successfully() {
        List<Meet> meets = List.of(
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
                        .build()
        );

        Instant result = calculator.calculateAdditionalMeetDate(meets);

        Instant expected = Instant.from(OffsetDateTime.of(2024, 10, 25, 11, 0, 0, 0, ZoneOffset.UTC));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldCalculateAdditionalMeetDate_withOneMeetPerWeek_successfully() {
        List<Meet> meets = List.of(
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 14, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build(),
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 21, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build()
        );

        Instant result = calculator.calculateAdditionalMeetDate(meets);

        Instant expected = Instant.from(OffsetDateTime.of(2024, 10, 28, 11, 0, 0, 0, ZoneOffset.UTC));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldCalculateAdditionalMeetDate_withRegularScheduleAndMovedHappenedMeet_successfully() {
        List<Meet> meets = List.of(
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 10, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .status(MeetStatus.HAPPENED)
                        .build(),
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
                        .build(),
                Meet.builder()
                        .scheduledFor(Instant.from(OffsetDateTime.of(2024, 10, 25, 11, 0, 0, 0, ZoneOffset.UTC)))
                        .build()
        );

        Instant result = calculator.calculateAdditionalMeetDate(meets);

        Instant expected = Instant.from(OffsetDateTime.of(2024, 10, 28, 11, 0, 0, 0, ZoneOffset.UTC));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrow_withEmptyMeets_successfully() {
        assertThatThrownBy(() -> calculator.calculateAdditionalMeetDate(Collections.emptyList()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No meet available to calculate the additional date");
    }
}
