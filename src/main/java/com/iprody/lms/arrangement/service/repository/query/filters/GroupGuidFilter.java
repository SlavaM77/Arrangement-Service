package com.iprody.lms.arrangement.service.repository.query.filters;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GUID_FIELD;

/**
 * {@link Filter} implementation for filtering by group 'groupGuid' field.
 */

@RequiredArgsConstructor
public class GroupGuidFilter implements Filter {

    private final String searchGuid;

    private static final String GROUP_GUID_FILTER_QUERY = "{0} = ''{1}''";

    @Override
    public String getFilterQuery() {
        return MessageFormat.format(GROUP_GUID_FILTER_QUERY, GUID_FIELD, searchGuid);
    }
}
