package com.iprody.lms.arrangement.service.repository.query.filters;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GUID_FIELD;
import static org.assertj.core.api.Assertions.assertThat;

class GroupGuidFilterTest {

    @Test
    void shouldReturnFilteringQueryClause_successfully() {
        String guid = UUID.randomUUID().toString();
        GroupGuidFilter filter = new GroupGuidFilter(guid);

        String expected = "%s = '%s'".formatted(GUID_FIELD, guid);

        String result = filter.getFilterQuery();

        assertThat(result).isEqualTo(expected);
    }
}
