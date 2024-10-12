package com.iprody.lms.arrangement.service.repository.query.builder;

import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupNameFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.StartDateFilter;
import com.iprody.lms.arrangement.service.repository.query.set.ExpressionType;
import com.iprody.lms.arrangement.service.repository.query.set.SetExpression;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_NAME_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULED_FOR_FIELD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateQueryBuilderTest {

    private final static String TABLE_NAME = "test_table";

    @Test
    void shouldThrow_withEmptySetExpressions() {
        List<SetExpression<?>> expressions = Collections.emptyList();
        assertThatThrownBy(() ->
                UpdateQueryBuilder.create()
                        .update(TABLE_NAME)
                        .set(expressions)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBuildUpdateSqlQuery_withoutFilters_successfully() {
        String name = "Test Name";
        Instant now = Instant.now();
        List<SetExpression<?>> expressions = List.of(
                new SetExpression<>(GROUP_NAME_FIELD, ExpressionType.GROUP_NAME_TYPE, name),
                new SetExpression<>(SCHEDULED_FOR_FIELD, ExpressionType.SCHEDULED_FOR_TYPE, now)
        );
        String result = UpdateQueryBuilder.create()
                .update(TABLE_NAME)
                .set(expressions)
                .build();

        String expected = String.format("UPDATE %s SET name = '%s',scheduled_for = '%s'", TABLE_NAME, name, now);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldBuildUpdateSqlQuery_withFilters_successfully() {
        String name = "Test Name";
        Instant now = Instant.now();
        List<SetExpression<?>> expressions = List.of(
                new SetExpression<>(GROUP_NAME_FIELD, ExpressionType.GROUP_NAME_TYPE, name),
                new SetExpression<>(SCHEDULED_FOR_FIELD, ExpressionType.SCHEDULED_FOR_TYPE, now)
        );
        List<Filter> filters = List.of(
                new GroupNameFilter(name),
                new StartDateFilter(now, StartDateFilter.Expression.FROM)
        );
        String result = UpdateQueryBuilder.create()
                .update(TABLE_NAME)
                .set(expressions)
                .where(filters)
                .build();

        String expected = """
                UPDATE %s SET name = '%s',scheduled_for = '%s'
                WHERE name ILIKE '%%%s%%' AND scheduled_for >= '%s'
                """
                .formatted(TABLE_NAME, name, now, name, now)
                .replaceAll("\\s+", " ")
                .trim();

        assertThat(result).isEqualTo(expected);
    }
}
