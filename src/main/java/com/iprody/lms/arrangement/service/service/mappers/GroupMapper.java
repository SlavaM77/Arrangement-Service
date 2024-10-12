package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.domain.model.GroupData;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.dto.request.AddGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupPageResponseDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.repository.query.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(uses = {CourseMapper.class, MeetMapper.class, MemberMapper.class})
public interface GroupMapper {

    @Mapping(target = "groups", source = "page.content")
    GroupPageResponseDto toPageResponse(Page<GroupEntity> page);

    @Mapping(target = "meets", source = "group.groupData.schedule.meets")
    @Mapping(target = "course", source = "group.groupData.course")
    @Mapping(target = "members", source = "group.groupData.members")
    GroupResponseDto fromEntity(GroupEntity group);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<GroupResponseDto> fromEntities(List<GroupEntity> groups);

    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "scheduledFor", source = "dto.scheduledFor")
    @Mapping(target = "groupData", expression = "java(mapGroupData(dto, course, members))")
    GroupEntity toEntity(AddGroupRequestDto dto, Course course, List<Member> members);

    default GroupData mapGroupData(AddGroupRequestDto dto, Course course, List<Member> members) {
        return GroupData.builder()
                .course(course)
                .members(members)
                .schedule(CollectionUtils.isEmpty(dto.meets())
                        ? new Schedule()
                        : new Schedule(new MeetMapperImpl().toModels(dto.meets())))
                .build();
    }
}
