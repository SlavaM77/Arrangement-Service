package com.iprody.lms.arrangement.service.dto.request;

import com.iprody.lms.arrangement.service.validation.annotation.ValidSchedule;
import com.iprody.lms.arrangement.service.validation.annotation.ValidUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

import static com.iprody.lms.arrangement.service.validation.constants.ValidatorConstants.UUID_REGEX;

@ValidSchedule
@ValidUpdate
public record UpdateGroupRequestDto(
        @NotBlank(message = "Group GUID must not be blank")
        @Pattern(regexp = UUID_REGEX, message = "Group GUID must be a valid UUID")
        String groupGuid,

        @Size(max = 255, message = "Name must be shorter than 255 characters")
        String name,

        @FutureOrPresent(message = "Scheduled time must not be in the past")
        Instant scheduledFor,

        @Valid
        List<MeetRequestDto> meets,

        List<@Pattern(regexp = UUID_REGEX, message = "Each member GUID must be a valid UUID") String> memberGuids
) implements ScheduleDto {
}
