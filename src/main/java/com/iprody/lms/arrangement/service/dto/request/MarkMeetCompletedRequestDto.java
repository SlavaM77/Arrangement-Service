package com.iprody.lms.arrangement.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

import static com.iprody.lms.arrangement.service.validation.constants.ValidatorConstants.UUID_REGEX;

public record MarkMeetCompletedRequestDto(

        @NotBlank(message = "Group GUID must not be blank")
        @Pattern(regexp = UUID_REGEX, message = "Group GUID must be a valid UUID")
        String groupGuid,

        @NotNull(message = "Scheduled time for meet must not be null")
        @Past(message = "Scheduled time must be in the past")
        Instant scheduledFor,

        @NotNull(message = "Happened time for meet must not be null")
        @Past(message = "Happened time must be in the past")
        Instant happenedAt
) {
}
