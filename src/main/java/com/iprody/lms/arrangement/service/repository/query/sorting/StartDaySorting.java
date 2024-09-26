package com.iprody.lms.arrangement.service.repository.query.sorting;

import org.springframework.data.domain.Sort;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULED_FOR_FIELD;

/**
 * {@link Sorting} realizations for sorting by group's 'scheduledFor' field.
 */

public class StartDaySorting extends Sorting {

    public StartDaySorting(Sort.Direction direction) {
        super(direction);
    }

    @Override
    public String getSortingBy() {
        return SCHEDULED_FOR_FIELD;
    }
}
