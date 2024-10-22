package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import com.iprody.lms.arrangement.service.validation.annotation.BothMembersAndRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Validator that checks if both 'memberIds' and 'role' params are either present or absent in {@link GroupRequestMeta}.
 * This validator is intended to be used with a custom annotation {@link BothMembersAndRole},
 * which applies the validation logic to instances of {@link GroupRequestMeta}.
 */

public class BothMembersAndRoleValidator implements ConstraintValidator<BothMembersAndRole, GroupRequestMeta> {

    @Override
    public boolean isValid(GroupRequestMeta groupRequestMeta, ConstraintValidatorContext context) {
        boolean hasMemberIds = CollectionUtils.isNotEmpty(groupRequestMeta.memberIds());
        boolean hasRole = StringUtils.isNotEmpty(groupRequestMeta.role());

        return hasMemberIds == hasRole;
    }
}
