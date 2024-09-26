package com.iprody.lms.arrangement.service.repository.query.filters;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULED_FOR_FIELD;
import static org.assertj.core.api.Assertions.assertThat;

class StartDateFilterTest {

    @Test
    void shouldReturnFilteringQueryClause_forLessOrEqualExpression_successfully() {
        Instant instant = Instant.now();
        StartDateFilter filter = new StartDateFilter(instant, StartDateFilter.Expression.TO);

        String expected = "%s <= '%s'".formatted(SCHEDULED_FOR_FIELD, instant);

        String result = filter.getFilterQuery();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnFilteringQueryClause_forMoreOrEqualExpression_successfully() {
        Instant instant = Instant.now();
        StartDateFilter filter = new StartDateFilter(instant, StartDateFilter.Expression.FROM);

        String expected = "%s >= '%s'".formatted(SCHEDULED_FOR_FIELD, instant);

        String result = filter.getFilterQuery();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnFilteringQueryClause_forEqualExpression_successfully() {
        Instant instant = Instant.now();
        StartDateFilter filter = new StartDateFilter(instant, StartDateFilter.Expression.EQUAL);

        String expected = "%s = '%s'".formatted(SCHEDULED_FOR_FIELD, instant);

        String result = filter.getFilterQuery();

        assertThat(result).isEqualTo(expected);
    }
}
