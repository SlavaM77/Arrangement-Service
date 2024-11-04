package com.iprody.lms.arrangement.service.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeConverterTest {

    @Test
    void shouldConvertToInstant_fromValidDate_successfully() {
        Instant result = TimeConverter.convertStringDateToInstant("2024-01-01");

        Instant expected = Instant.from(Instant.from(OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrow_withInvalidDate() {
        assertThatThrownBy(() -> TimeConverter.convertStringDateToInstant("aaa"))
                .isInstanceOf(DateTimeParseException.class);
    }
}
