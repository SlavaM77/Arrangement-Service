package com.iprody.lms.arrangement.service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

import static com.iprody.lms.arrangement.service.validation.constants.ValidatorConstants.UUID_REGEX;

public record AddScheduleRequestDto(

        @NotBlank(message = "Group GUID must not be blank")
        @Pattern(regexp = UUID_REGEX, message = "Group GUID must be a valid UUID")
        String groupGuid,

        @Valid
        @NotNull(message = "There should be at least one meet")
        @NotEmpty(message = "There should be at least one meet")
        List<MeetRequestDto> meets
) {
}
