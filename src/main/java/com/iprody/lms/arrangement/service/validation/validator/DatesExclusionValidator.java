package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import com.iprody.lms.arrangement.service.validation.annotation.DatesExclusion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Validator that ensures mutually exclusive date fields in {@link GroupRequestMeta}.
 * Validates that if either 'dateFrom' or 'dateTo' param is present, 'date' param must be absent, and vice versa.
 * This validator is intended to be used with a custom annotation {@link DatesExclusion},
 * which applies the validation logic to instances of {@link GroupRequestMeta}.
 */

public class DatesExclusionValidator implements ConstraintValidator<DatesExclusion, GroupRequestMeta> {

    @Override
    public boolean isValid(GroupRequestMeta groupRequestMeta, ConstraintValidatorContext context) {
        boolean hasDateFrom = StringUtils.isNotEmpty(groupRequestMeta.dateFrom());
        boolean hasDateTo = StringUtils.isNotEmpty(groupRequestMeta.dateTo());
        boolean hasDate = StringUtils.isNotEmpty(groupRequestMeta.date());

        return !(hasDateFrom || hasDateTo) || !hasDate;
    }
}
