package com.iprody.lms.arrangement.service.repository.query.sorting;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.function.Supplier;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.LASTNAME_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MENTOR_ROLE;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MENTOR_SORTING_QUERY;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.ROLE_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULED_FOR_FIELD;

/**
 * This class contains defined functions for 'ORDER' clauses construction.
 * It is a parameter for {@link Sorting}.
 */

@Getter
public enum SortingType {

    MENTOR_TYPE(() -> MessageFormat.format(MENTOR_SORTING_QUERY,
            GROUP_DATA_FIELD,
            MEMBERS_FIELD,
            ROLE_FIELD,
            MENTOR_ROLE,
            LASTNAME_FIELD)),

    START_DAY_SORTING(() -> SCHEDULED_FOR_FIELD);

    private final Supplier<String> supplier;

    SortingType(Supplier<String> supplier) {
        this.supplier = supplier;
    }
}
