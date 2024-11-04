package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import com.iprody.lms.arrangement.service.dto.request.MeetRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupGuidFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupNameFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.MemberFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.StartDateFilter;
import com.iprody.lms.arrangement.service.repository.query.params.SelectQueryParams;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.repository.query.sorting.Sorting;
import com.iprody.lms.arrangement.service.util.TimeConverter;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.DEFAULT_PAGE;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.DEFAULT_PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

class GroupProcessingServiceTest {

    @InjectMocks
    private GroupProcessingService service;

    @Mock
    private MemberProcessingService memberProcessingService;
    @Mock
    private ScheduleProcessingService scheduleProcessingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCollectParamsForSelect_withoutData_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(null, null, null, null, null, null, null, null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).isEmpty();
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withNameParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta("name", null, null, null, null, null, null, null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).hasSize(1)
                .first()
                .isInstanceOf(GroupNameFilter.class)
                .extracting("searchExpression")
                .isEqualTo("name");
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withMemberIdsAndTeacherRoleParams_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(
                null, List.of("id"), "teacher", null, null, null, null, null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).hasSize(1)
                .first()
                .isInstanceOf(MemberFilter.class)
                .extracting("guids", "role")
                .matches(tuple -> tuple.getFirst().equals(List.of("id")) && tuple.getLast().equals(MemberRole.TEACHER));
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withMemberIdsAndInternRoleParams_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(
                null, List.of("id"), "intern", null, null, null, null, null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).hasSize(1)
                .first()
                .isInstanceOf(MemberFilter.class)
                .extracting("guids", "role")
                .matches(tuple -> tuple.getFirst().equals(List.of("id")) && tuple.getLast().equals(MemberRole.INTERN));
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withDateFromParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(
                null, null, null, "2024-01-01", null, null, null, null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).hasSize(1)
                .first()
                .isInstanceOf(StartDateFilter.class)
                .extracting("searchDate", "expression")
                .matches(tuple -> tuple.getFirst().equals(TimeConverter.convertStringDateToInstant("2024-01-01"))
                        && tuple.getLast().equals(StartDateFilter.Expression.FROM));
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withDateToParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(
                null, null, null, null, "2024-01-01", null, null, null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).hasSize(1)
                .first()
                .isInstanceOf(StartDateFilter.class)
                .extracting("searchDate", "expression")
                .matches(tuple -> tuple.getFirst()
                        .equals(TimeConverter.convertStringDateToInstant("2024-01-01").plus(1, ChronoUnit.DAYS))
                        && tuple.getLast().equals(StartDateFilter.Expression.TO));
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withDateParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(
                null, null, null, null, null, "2024-01-01", null, null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).hasSize(2)
                .allMatch(filter -> filter instanceof StartDateFilter);
        assertThat(result.filters()).first()
                .extracting("searchDate", "expression")
                .matches(tuple -> tuple.getFirst()
                        .equals(TimeConverter.convertStringDateToInstant("2024-01-01"))
                        && tuple.getLast().equals(StartDateFilter.Expression.FROM));
        assertThat(result.filters()).last()
                .extracting("searchDate", "expression")
                .matches(tuple -> tuple.getFirst()
                        .equals(TimeConverter.convertStringDateToInstant("2024-01-01").plus(1, ChronoUnit.DAYS))
                        && tuple.getLast().equals(StartDateFilter.Expression.TO));
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withOnlySortByParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(null, null, null, null, null, null, "date", null, null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).isEmpty();
        assertThat(result.sorting())
                .extracting(Sorting::getSortingClause, Sorting::getSortDirection)
                .matches(tuple -> tuple.getFirst() != null && tuple.getLast().equals("ASC"));
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withOnlySortParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(null, null, null, null, null, null, null, "asc", null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).isEmpty();
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withBothSortingParams_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(null, null, null, null, null, null, "teacher", "desc", null, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).isEmpty();
        assertThat(result.sorting())
                .extracting(Sorting::getSortingClause, Sorting::getSortDirection)
                .matches(tuple -> tuple.getFirst() != null && tuple.getLast().equals("DESC"));
        assertThat(result.pagination())
                .extracting("page", "size")
                .containsExactly(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withPageParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(null, null, null, null, null, null, null, null, 2, null);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).isEmpty();
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination().page()).isEqualTo(2);
        assertThat(result.pagination().size()).isEqualTo(DEFAULT_PAGE_SIZE);
    }

    @Test
    void shouldCollectParamsForSelect_withSizeParam_successfully() {
        GroupRequestMeta meta = new GroupRequestMeta(null, null, null, null, null, null, null, null, null, 15);

        SelectQueryParams result = service.collectParamsForSelect(meta);

        assertThat(result.filters()).isEmpty();
        assertThat(result.sorting()).isNull();
        assertThat(result.pagination().page()).isEqualTo(DEFAULT_PAGE);
        assertThat(result.pagination().size()).isEqualTo(15);
    }

    @Test
    void shouldCollectParamsForUpdate_withAllValidData_successfully() {
        GroupEntity group = Instancio.create(GroupEntity.class);
        UpdateGroupRequestDto dto = Instancio.create(UpdateGroupRequestDto.class);
        List<Member> members = Instancio.createList(Member.class);

        UpdateQueryParams result = service.collectParamsForUpdate(group, dto, members);

        assertThat(result.expressions()).hasSize(4);
        assertThat(result.filters())
                .hasSize(1)
                .first()
                .isInstanceOf(GroupGuidFilter.class);
    }

    @Test
    void shouldCollectParamsForUpdate_withOnlyGroupNameUpdate_successfully() {
        GroupEntity group = Instancio.create(GroupEntity.class);
        UpdateGroupRequestDto dto = new UpdateGroupRequestDto(
                UUID.randomUUID().toString(),
                "test name",
                null,
                null,
                null);

        UpdateQueryParams result = service.collectParamsForUpdate(group, dto, Collections.emptyList());

        assertThat(result.expressions()).hasSize(1);
        assertThat(result.filters())
                .hasSize(1)
                .first()
                .isInstanceOf(GroupGuidFilter.class);
    }

    @Test
    void shouldCollectParamsForUpdate_withOnlyMembersUpdate_successfully() {
        GroupEntity group = Instancio.create(GroupEntity.class);
        UpdateGroupRequestDto dto = new UpdateGroupRequestDto(
                UUID.randomUUID().toString(),
                null,
                null,
                null,
                List.of());
        List<Member> members = Instancio.createList(Member.class);

        UpdateQueryParams result = service.collectParamsForUpdate(group, dto, members);

        assertThat(result.expressions()).hasSize(1);
        assertThat(result.filters())
                .hasSize(1)
                .first()
                .isInstanceOf(GroupGuidFilter.class);
    }

    @Test
    void shouldCollectParamsForUpdate_withOnlyScheduledForUpdate_successfully() {
        GroupEntity group = Instancio.create(GroupEntity.class);
        UpdateGroupRequestDto dto = new UpdateGroupRequestDto(
                UUID.randomUUID().toString(),
                null,
                Instant.now(),
                null,
                null);

        UpdateQueryParams result = service.collectParamsForUpdate(group, dto, Collections.emptyList());

        assertThat(result.expressions()).hasSize(1);
        assertThat(result.filters())
                .hasSize(1)
                .first()
                .isInstanceOf(GroupGuidFilter.class);
    }

    @Test
    void shouldCollectParamsForUpdate_withOnlyMeetsUpdate_successfully() {
        GroupEntity group = Instancio.create(GroupEntity.class);
        List<MeetRequestDto> meets = Instancio.createList(MeetRequestDto.class);
        UpdateGroupRequestDto dto = new UpdateGroupRequestDto(
                UUID.randomUUID().toString(),
                null,
                null,
                meets,
                null);

        UpdateQueryParams result = service.collectParamsForUpdate(group, dto, Collections.emptyList());

        assertThat(result.expressions()).hasSize(1);
        assertThat(result.filters())
                .hasSize(1)
                .first()
                .isInstanceOf(GroupGuidFilter.class);
    }
}
