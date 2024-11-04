package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.validation.annotation.ValidUpdate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Validator that ensures at least one field is provided for updating a group in a {@link UpdateGroupRequestDto}.
 * This validator is intended to be used with a custom annotation {@link ValidUpdate},
 * which applies the validation logic to instances of {@link UpdateGroupRequestDto}.
 */

public class UpdateGroupValidator implements ConstraintValidator<ValidUpdate, UpdateGroupRequestDto> {

    @Override
    public boolean isValid(UpdateGroupRequestDto dto, ConstraintValidatorContext context) {
        boolean isNameEmpty = dto.name() == null || dto.name().isBlank();
        boolean isScheduledForEmpty = dto.scheduledFor() == null;
        boolean isMemberGuidsEmpty = CollectionUtils.isEmpty(dto.memberGuids());
        boolean isMeetsEmpty = CollectionUtils.isEmpty(dto.meets());

        return !isNameEmpty || !isScheduledForEmpty || !isMemberGuidsEmpty || !isMeetsEmpty;
    }
}
