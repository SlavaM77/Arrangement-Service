package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.domain.model.GroupData;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.request.AddGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupPageResponseDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.repository.query.pagination.Page;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupMapperTest {

    private final GroupMapper mapper = new GroupMapperImpl();

    @Test
    void shouldMapGroupToDto_fromCorrectObject_successfully() {
        GroupEntity group = Instancio.create(GroupEntity.class);

        GroupResponseDto result = mapper.fromEntity(group);

        assertThat(result)
                .extracting(GroupResponseDto::guid, GroupResponseDto::name, GroupResponseDto::scheduledFor)
                .containsExactly(group.getGuid(), group.getName(), group.getScheduledFor());

        assertThat(result.meets()).isNotEmpty();
        assertThat(result.course()).isNotNull();
        assertThat(result.members()).isNotEmpty();
    }

    @Test
    void shouldReturnNullDto_fromNull_successfully() {
        GroupResponseDto result = mapper.fromEntity(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapGroupsToDtos_fromCorrectObjects_successfully() {
        List<GroupEntity> groups = Instancio.ofList(GroupEntity.class).size(2).create();

        List<GroupResponseDto> result = mapper.fromEntities(groups);

        assertThat(result).hasSize(2)
                .doesNotContainNull();
    }

    @Test
    void shouldReturnEmptyDtoList_fromNull_successfully() {
        List<GroupResponseDto> result = mapper.fromEntities(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyDtoList_fromEmptyList_successfully() {
        List<GroupResponseDto> result = mapper.fromEntities(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapPageToDto_fromCorrectObject_successfully() {
        Page<GroupEntity> page = new Page<>(2L, Instancio.ofList(GroupEntity.class).size(2).create());

        GroupPageResponseDto result = mapper.toPageResponse(page);

        assertThat(result)
                .extracting(GroupPageResponseDto::totalCount, GroupPageResponseDto::groups)
                .matches(tuple -> tuple.getFirst().equals(2L) && !((List<?>) tuple.getLast()).isEmpty());
        assertThat(result.groups())
                .hasSize(2)
                .doesNotContainNull();
    }

    @Test
    void shouldReturnNullDto_fromNullPageObject_successfully() {
        GroupPageResponseDto result = mapper.toPageResponse(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapToEntity_withAllParams_successfully() {
        AddGroupRequestDto dto = Instancio.create(AddGroupRequestDto.class);
        Course course = Instancio.create(Course.class);
        List<Member> members = Instancio.ofList(Member.class).size(2).create();

        GroupEntity result = mapper.toEntity(dto, course, members);

        assertThat(result)
                .extracting(GroupEntity::getGuid, GroupEntity::getName, GroupEntity::getCreatedAt,
                        GroupEntity::getUpdatedAt, GroupEntity::getScheduledFor, GroupEntity::getGroupData)
                .matches(tuple ->
                        tuple.get(0) != null &&
                        tuple.get(1).equals(dto.name()) &&
                        tuple.get(2) != null &&
                        tuple.get(3) != null &&
                        tuple.get(4).equals(dto.scheduledFor()) &&
                        tuple.get(5) != null);
        assertThat(result.getGroupData())
                .extracting(GroupData::getCourse, GroupData::getMembers,
                        groupData -> groupData.getSchedule().getMeets().size())
                .containsExactly(course, members, dto.meets().size());

    }
}
