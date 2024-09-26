package com.iprody.lms.arrangement.service.repository.query.filters;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GUID_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.ROLE_FIELD;

/**
 * {@link Filter} implementation for filtering by member's 'guid' and 'role' fields.
 */

@RequiredArgsConstructor
public class MemberFilter implements Filter {

    private final List<String> guids;
    private final MemberRole role;

    private static final String MENTOR_FILTER_QUERY = "EXISTS (SELECT * FROM jsonb_array_elements({0}->''{1}'') "
            + "AS member WHERE member->>''{2}'' = ANY(ARRAY[{3}]) AND member->>''{4}'' = ''{5}'')";

    @Override
    public String getFilterQuery() {
        String guidsArray = guids.stream()
                .map(guid -> String.format("'%s'", guid))
                .collect(Collectors.joining(", "));
        return MessageFormat.format(MENTOR_FILTER_QUERY,
                GROUP_DATA_FIELD,
                MEMBERS_FIELD,
                GUID_FIELD,
                guidsArray,
                ROLE_FIELD,
                role);
    }
}
