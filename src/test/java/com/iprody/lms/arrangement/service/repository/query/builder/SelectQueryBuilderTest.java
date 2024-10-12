package com.iprody.lms.arrangement.service.repository.query.builder;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupNameFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.MemberFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.StartDateFilter;
import com.iprody.lms.arrangement.service.repository.query.pagination.Pagination;
import com.iprody.lms.arrangement.service.repository.query.sorting.Sorting;
import com.iprody.lms.arrangement.service.repository.query.sorting.SortingType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SelectQueryBuilderTest {

    private final static String TABLE_NAME = "test_table";
    private final static String SELECTED_FIELDS = "*";

    @Test
    void shouldBuildSelectSqlQuery_withoutAdditionalParams_successfully() {
        String expected = String.format("SELECT %s FROM %s", SELECTED_FIELDS, TABLE_NAME);

        String result = SelectQueryBuilder.create()
                .select(SELECTED_FIELDS)
                .from(TABLE_NAME)
                .build();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldBuildSelectSqlQuery_withFilters_successfully() {
        String groupNameSearch = "group";
        Instant fromTime = Instant.now();
        Instant toTime = fromTime.plus(1, ChronoUnit.DAYS);
        List<String> teacherGuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<Filter> filters = List.of(
                new GroupNameFilter(groupNameSearch),
                new StartDateFilter(fromTime, StartDateFilter.Expression.FROM),
                new StartDateFilter(toTime, StartDateFilter.Expression.TO),
                new MemberFilter(teacherGuids, MemberRole.TEACHER)
        );

        String teacherGuidsStr = teacherGuids.stream()
                .map(guid -> String.format("'%s'", guid))
                .collect(Collectors.joining(", "));
        String expected = """
                SELECT %s FROM %s
                WHERE name ILIKE '%%%s%%'
                AND scheduled_for >= '%s'
                AND scheduled_for <= '%s'
                AND EXISTS (SELECT * FROM jsonb_array_elements(group_data->'members') AS member
                            WHERE member->>'guid' = ANY(ARRAY[%s])
                            AND member->>'role' = '%s')
                """
                .formatted(SELECTED_FIELDS, TABLE_NAME, groupNameSearch, fromTime, toTime, teacherGuidsStr, MemberRole.TEACHER)
                .replaceAll("\\s+", " ")
                .trim();

        String result = SelectQueryBuilder.create()
                .select(SELECTED_FIELDS)
                .from(TABLE_NAME)
                .where(filters)
                .build();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldBuildSelectSqlQuery_withSorting_successfully() {
        Sorting mentorSorting = new Sorting(SortingType.MENTOR_TYPE, "desc");

        String expected = """
                SELECT %s FROM %s
                ORDER BY
                    (SELECT jsonb_path_query_first(group_data,'$.members[*] ? (@.role == "%s").lastName')::text) DESC
                """
                .formatted(SELECTED_FIELDS, TABLE_NAME, MemberRole.TEACHER)
                .replaceAll("\\s+", " ")
                .trim();

        String result = SelectQueryBuilder.create()
                .select(SELECTED_FIELDS)
                .from(TABLE_NAME)
                .orderBy(mentorSorting)
                .build();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldBuildSelectSqlQuery_withPagination_successfully() {
        Pagination pagination = new Pagination(2, 25);

        String expected = String.format("SELECT %s FROM %s LIMIT 25 OFFSET 25", SELECTED_FIELDS, TABLE_NAME);

        String result = SelectQueryBuilder.create()
                .select(SELECTED_FIELDS)
                .from(TABLE_NAME)
                .paginate(pagination)
                .build();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldBuildSelectSqlQuery_withAllAdditionalParams_successfully() {
        String groupNameSearch = "group";
        Instant searchTime = Instant.now();
        List<String> internGuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<Filter> filters = List.of(
                new GroupNameFilter(groupNameSearch),
                new StartDateFilter(searchTime, StartDateFilter.Expression.FROM),
                new MemberFilter(internGuids, MemberRole.INTERN)
        );
        Sorting startDaySorting = new Sorting(SortingType.START_DAY_SORTING, "desc");
        Pagination pagination = new Pagination(3, 15);

        String internGuidsStr = internGuids.stream()
                .map(guid -> String.format("'%s'", guid))
                .collect(Collectors.joining(", "));
        String expected = """
                SELECT %s FROM %s
                WHERE name ILIKE '%%%s%%'
                AND scheduled_for >= '%s'
                AND EXISTS (SELECT * FROM jsonb_array_elements(group_data->'members') AS member
                            WHERE member->>'guid' = ANY(ARRAY[%s])
                            AND member->>'role' = '%s')
                ORDER BY scheduled_for DESC
                LIMIT 15 OFFSET 30
                """
                .formatted(SELECTED_FIELDS, TABLE_NAME, groupNameSearch, searchTime, internGuidsStr, MemberRole.INTERN)
                .replaceAll("\\s+", " ")
                .trim();

        String result = SelectQueryBuilder.create()
                .select(SELECTED_FIELDS)
                .from(TABLE_NAME)
                .where(filters)
                .orderBy(startDaySorting)
                .paginate(pagination)
                .build();

        assertThat(result).isEqualTo(expected);
    }
}
