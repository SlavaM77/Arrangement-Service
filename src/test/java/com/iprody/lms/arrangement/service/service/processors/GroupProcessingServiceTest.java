package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.request.MeetRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupGuidFilter;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
