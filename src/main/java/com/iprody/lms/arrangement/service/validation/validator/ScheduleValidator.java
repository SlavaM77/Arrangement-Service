package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.dto.request.MeetRequestDto;
import com.iprody.lms.arrangement.service.dto.request.ScheduleDto;
import com.iprody.lms.arrangement.service.validation.annotation.ValidSchedule;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.util.List;

/**
 * Validator that ensures the consistency of a schedule in a {@link ScheduleDto}.
 * Validates that no meet within the schedule is set before the group's scheduled time.
 * This validator is intended to be used with a custom annotation {@link ValidSchedule},
 * which applies the validation logic to instances of {@link ScheduleDto}.
 */

public class ScheduleValidator implements ConstraintValidator<ValidSchedule, ScheduleDto> {

    @Override
    public boolean isValid(ScheduleDto dto, ConstraintValidatorContext context) {
        Instant groupScheduledFor = dto.scheduledFor();
        List<MeetRequestDto> meets = dto.meets();

        if (groupScheduledFor == null || CollectionUtils.isEmpty(meets)) {
            return true;
        }

        return meets.stream()
                .filter(meet -> meet.scheduledFor() != null)
                .noneMatch(meet -> groupScheduledFor.isAfter(meet.scheduledFor()));
    }
}
