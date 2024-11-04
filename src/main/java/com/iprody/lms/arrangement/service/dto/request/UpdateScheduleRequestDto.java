package com.iprody.lms.arrangement.service.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

import static com.iprody.lms.arrangement.service.validation.constants.ValidatorConstants.UUID_REGEX;

public record UpdateScheduleRequestDto(

        @NotBlank(message = "Group GUID must not be blank")
        @Pattern(regexp = UUID_REGEX, message = "Group GUID must be a valid UUID")
        String groupGuid,

        @NotNull(message = "Canceled time for meet must not be null")
        @FutureOrPresent(message = "Canceled time must not be in the past")
        Instant canceledTime
) {
}
