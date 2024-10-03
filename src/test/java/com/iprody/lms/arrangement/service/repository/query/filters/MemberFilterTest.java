package com.iprody.lms.arrangement.service.repository.query.filters;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GUID_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.ROLE_FIELD;
import static org.assertj.core.api.Assertions.assertThat;

class MemberFilterTest {

    @Test
    void shouldReturnFilteringQueryClause_successfully() {
        List<String> guids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        MemberFilter filter = new MemberFilter(guids, MemberRole.INTERN);

        String guidsStr = guids.stream()
                .map(guid -> String.format("'%s'", guid))
                .collect(Collectors.joining(", "));
        String expected = """
                EXISTS (SELECT * FROM jsonb_array_elements(%s->'%s') AS member
                WHERE member->>'%s' = ANY(ARRAY[%s]) AND member->>'%s' = '%s')
                """
                .formatted(GROUP_DATA_FIELD, MEMBERS_FIELD, GUID_FIELD, guidsStr, ROLE_FIELD, MemberRole.INTERN)
                .replaceAll("\\s+", " ")
                .trim();

        String result = filter.getFilterQuery();

        assertThat(result).isEqualTo(expected);
    }
}
