package com.iprody.lms.arrangement.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.iprody.lms.arrangement.service.validation.constants.ValidatorConstants.UUID_REGEX;

public record AddMemberRequestDto(

        @NotBlank(message = "Group GUID must not be blank")
        @Pattern(regexp = UUID_REGEX, message = "Group GUID must be a valid UUID")
        String groupGuid,

        @NotBlank(message = "Member GUID must not be blank")
        @Pattern(regexp = UUID_REGEX, message = "Member GUID must be a valid UUID")
        String memberGuid
) {
}
