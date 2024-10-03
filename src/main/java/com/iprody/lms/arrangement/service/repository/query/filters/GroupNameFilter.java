package com.iprody.lms.arrangement.service.repository.query.filters;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_NAME_FIELD;

/**
 * {@link Filter} implementation for filtering by 'groupName' field using the ILIKE condition.
 */

@RequiredArgsConstructor
public class GroupNameFilter implements Filter {

    private final String searchExpression;

    private static final String GROUP_NAME_FILTER_QUERY = "{0} ILIKE ''%{1}%''";

    @Override
    public String getFilterQuery() {
        return MessageFormat.format(GROUP_NAME_FILTER_QUERY, GROUP_NAME_FIELD, searchExpression);
    }
}
