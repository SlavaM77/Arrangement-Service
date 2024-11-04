package com.iprody.lms.arrangement.service.dto.request;

import com.iprody.lms.arrangement.service.validation.annotation.BothMembersAndRole;
import com.iprody.lms.arrangement.service.validation.annotation.DatesExclusion;
import com.iprody.lms.arrangement.service.validation.annotation.ValidDate;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

import static com.iprody.lms.arrangement.service.validation.constants.ValidatorConstants.UUID_REGEX;

@BothMembersAndRole
@DatesExclusion
public record GroupRequestMeta(

        @Size(max = 255, message = "Name must be shorter than 255 characters")
        String name,

        List<@Pattern(regexp = UUID_REGEX, message = "Each member GUID must be a valid UUID") String> memberIds,

        @Pattern(regexp = "teacher|intern", message = "Role must be either 'teacher' or 'intern'")
        String role,

        @ValidDate
        String dateFrom,

        @ValidDate
        String dateTo,

        @ValidDate
        String date,

        @Pattern(regexp = "date|teacher", message = "SortingBy must be either 'teacher' or 'date'")
        String sortBy,

        @Pattern(regexp = "asc|desc", message = "Sorting must be either 'asc' or 'desc'")
        String sort,

        @Positive(message = "Value must be positive")
        Integer page,

        @Positive(message = "Value must be positive")
        Integer size
) {
}
