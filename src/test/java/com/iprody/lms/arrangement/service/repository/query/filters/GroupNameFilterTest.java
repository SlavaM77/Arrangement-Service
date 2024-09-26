package com.iprody.lms.arrangement.service.repository.query.filters;

import org.junit.jupiter.api.Test;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_NAME_FIELD;
import static org.assertj.core.api.Assertions.assertThat;

class GroupNameFilterTest {

    @Test
    void shouldReturnFilteringQueryClause_successfully() {
        String groupNameSearch = "group";
        GroupNameFilter filter = new GroupNameFilter(groupNameSearch);

        String expected = "%s ILIKE '%%%s%%'".formatted(GROUP_NAME_FIELD, groupNameSearch);

        String result = filter.getFilterQuery();

        assertThat(result).isEqualTo(expected);
    }
}
