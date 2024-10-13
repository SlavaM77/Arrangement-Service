package com.iprody.lms.arrangement.service.service;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.request.AddGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupPageResponseDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.integration.courceservice.CourseService;
import com.iprody.lms.arrangement.service.integration.userprofile.UserProfileService;
import com.iprody.lms.arrangement.service.repository.GroupRepository;
import com.iprody.lms.arrangement.service.repository.query.params.SelectQueryParams;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.service.mappers.GroupMapper;
import com.iprody.lms.arrangement.service.service.mappers.GroupMapperImpl;
import com.iprody.lms.arrangement.service.service.processors.GroupProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupManagementService {

    private final CourseService courseService;
    private final UserProfileService userProfileService;
    private final GroupProcessingService groupProcessingService;
    private final GroupRepository repository;
    private final GroupMapper mapper = new GroupMapperImpl();

    public Mono<GroupResponseDto> saveGroup(AddGroupRequestDto dto) {
        return Mono.zip(courseService.getCourseByGuid(dto.courseGuid()),
                        userProfileService.getMembersByGuids(dto.memberGuids()).collectList())
                .flatMap(tuple -> {
                    Course course = tuple.getT1();
                    List<Member> members = tuple.getT2();
                    GroupEntity entity = mapper.toEntity(dto, course, members);

                    return repository.save(entity);
                })
                .map(mapper::fromEntity);
    }

    public Mono<GroupPageResponseDto> getGroupsByParams() {
        // todo - add realization according discussed meta language
        SelectQueryParams params = new SelectQueryParams(
                null,
                null,
                null);
        return repository.findByParams(params)
                .map(mapper::toPageResponse);
    }

    public Mono<GroupResponseDto> updateGroup(UpdateGroupRequestDto dto) {
        return Mono.zip(repository.findByGuid(dto.groupGuid()),
                        userProfileService.getMembersByGuids(dto.memberGuids()).collectList())
                .flatMap(tuple -> {
                    GroupEntity groupEntity = tuple.getT1();
                    List<Member> members = tuple.getT2();

                    UpdateQueryParams params = groupProcessingService.collectParamsForUpdate(groupEntity, dto, members);

                    return repository.update(params, dto.groupGuid());
                })
                .map(mapper::fromEntity);
    }
}
