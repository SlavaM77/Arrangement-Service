package com.iprody.lms.arrangement.service.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MeetRequestDto(

        @NotNull(message = "Scheduled time for meet must not be null")
        @FutureOrPresent(message = "Scheduled time must not be in the past")
        Instant scheduledFor,

        String conferenceRef
) {
}
