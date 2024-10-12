package com.iprody.lms.arrangement.service.repository.query.sorting;

import org.springframework.data.domain.Sort;

import java.text.MessageFormat;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.LASTNAME_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MENTOR_ROLE;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.ROLE_FIELD;

/**
 * {@link Sorting} implementation for sorting by mentor's (teacher's) 'lastName' field.
 */

public class MentorSorting extends Sorting {

    private static final String MENTOR_SORTING_QUERY =
            "(SELECT jsonb_path_query_first({0},''$.{1}[*] ? (@.{2} == \"{3}\").{4}'')::text)";

    public MentorSorting(Sort.Direction direction) {
        super(direction);
    }

    @Override
    public String getSortingBy() {
        return MessageFormat.format(MENTOR_SORTING_QUERY,
                GROUP_DATA_FIELD,
                MEMBERS_FIELD,
                ROLE_FIELD,
                MENTOR_ROLE,
                LASTNAME_FIELD);
    }
}
