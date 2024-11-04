package com.iprody.lms.arrangement.service.service;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.request.AddGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupPageResponseDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import com.iprody.lms.arrangement.service.integration.courceservice.CourseService;
import com.iprody.lms.arrangement.service.integration.userprofile.UserProfileService;
import com.iprody.lms.arrangement.service.repository.GroupRepository;
import com.iprody.lms.arrangement.service.repository.query.pagination.Page;
import com.iprody.lms.arrangement.service.repository.query.params.SelectQueryParams;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.service.processors.GroupProcessingService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GroupManagementServiceTest {

    @InjectMocks
    private GroupManagementService groupManagementService;

    @Mock
    private CourseService courseService;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private GroupProcessingService groupProcessingService;
    @Mock
    private GroupRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCompleteSaveFlow_successfully() {
        AddGroupRequestDto dto = Instancio.create(AddGroupRequestDto.class);
        Course course = Instancio.create(Course.class);
        List<Member> members = Instancio.createList(Member.class);
        GroupEntity group = Instancio.create(GroupEntity.class);

        when(courseService.getCourseByGuid(dto.courseGuid())).thenReturn(Mono.just(course));
        when(userProfileService.getMembersByGuids(dto.memberGuids())).thenReturn(Flux.fromIterable(members));
        when(repository.save(any())).thenReturn(Mono.just(group));

        Mono<GroupResponseDto> result = groupManagementService.saveGroup(dto);

        StepVerifier.create(result)
                .consumeNextWith(responseDto -> {
                    assertThat(responseDto)
                            .extracting(GroupResponseDto::guid, GroupResponseDto::name, GroupResponseDto::scheduledFor)
                            .containsExactly(group.getGuid(), group.getName(), group.getScheduledFor());
                    assertThat(responseDto.meets()).isNotEmpty();
                    assertThat(responseDto.course()).isNotNull();
                    assertThat(responseDto.members()).isNotEmpty();
                })
                .verifyComplete();

        verify(courseService).getCourseByGuid(dto.courseGuid());
        verify(userProfileService).getMembersByGuids(dto.memberGuids());
        verify(repository).save(any(GroupEntity.class));
    }

    @Test
    void shouldReturnErrorForSaveFlow_whenCourseServiceReturnsError() {
        AddGroupRequestDto dto = Instancio.create(AddGroupRequestDto.class);
        List<Member> members = Instancio.createList(Member.class);

        when(userProfileService.getMembersByGuids(dto.memberGuids())).thenReturn(Flux.fromIterable(members));
        when(courseService.getCourseByGuid(dto.courseGuid()))
                .thenReturn(Mono.error(new EntityNotFoundException("message")));

        Mono<GroupResponseDto> result = groupManagementService.saveGroup(dto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();

        verify(courseService, times(1)).getCourseByGuid(dto.courseGuid());
        verify(userProfileService, times(1)).getMembersByGuids(dto.memberGuids());
        verify(repository, never()).save(any(GroupEntity.class));
    }

    @Test
    void shouldReturnErrorForSaveFlow_whenUserProfileServiceReturnsError() {
        AddGroupRequestDto dto = Instancio.create(AddGroupRequestDto.class);
        Course course = Instancio.create(Course.class);

        when(userProfileService.getMembersByGuids(dto.memberGuids()))
                .thenReturn(Flux.error(new EntityNotFoundException("message")));
        when(courseService.getCourseByGuid(dto.courseGuid())).thenReturn(Mono.just(course));

        Mono<GroupResponseDto> result = groupManagementService.saveGroup(dto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();

        verify(courseService, times(1)).getCourseByGuid(dto.courseGuid());
        verify(userProfileService, times(1)).getMembersByGuids(dto.memberGuids());
        verify(repository, never()).save(any(GroupEntity.class));
    }

    @Test
    void shouldCompleteUpdateFlow_successfully() {
        UpdateGroupRequestDto dto = Instancio.create(UpdateGroupRequestDto.class);
        List<Member> members = Instancio.createList(Member.class);
        GroupEntity group = Instancio.create(GroupEntity.class);
        UpdateQueryParams params = Instancio.create(UpdateQueryParams.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(userProfileService.getMembersByGuids(dto.memberGuids())).thenReturn(Flux.fromIterable(members));
        when(groupProcessingService.collectParamsForUpdate(group, dto, members)).thenReturn(params);
        when(repository.update(params, dto.groupGuid())).thenReturn(Mono.just(group));

        Mono<GroupResponseDto> result = groupManagementService.updateGroup(dto);

        StepVerifier.create(result)
                .consumeNextWith(responseDto -> {
                    assertThat(responseDto)
                            .extracting(GroupResponseDto::guid, GroupResponseDto::name, GroupResponseDto::scheduledFor)
                            .containsExactly(group.getGuid(), group.getName(), group.getScheduledFor());
                    assertThat(responseDto.meets()).isNotEmpty();
                    assertThat(responseDto.course()).isNotNull();
                    assertThat(responseDto.members()).isNotEmpty();
                })
                .verifyComplete();

        verify(repository).findByGuid(dto.groupGuid());
        verify(userProfileService).getMembersByGuids(dto.memberGuids());
        verify(groupProcessingService).collectParamsForUpdate(group, dto, members);
        verify(repository).update(params, dto.groupGuid());
    }

    @Test
    void shouldReturnErrorForUpdateFlow_whenUserProfileServiceReturnsError() {
        UpdateGroupRequestDto dto = Instancio.create(UpdateGroupRequestDto.class);
        GroupEntity group = Instancio.create(GroupEntity.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(userProfileService.getMembersByGuids(dto.memberGuids()))
                .thenReturn(Flux.error(new EntityNotFoundException("message")));

        Mono<GroupResponseDto> result = groupManagementService.updateGroup(dto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();

        verify(repository, times(1)).findByGuid(dto.groupGuid());
        verify(userProfileService, times(1)).getMembersByGuids(dto.memberGuids());
        verify(groupProcessingService, never()).collectParamsForUpdate(any(), any(), any());
        verify(repository, never()).update(any(), any());
    }

    @Test
    void shouldRetrieveGroupPage_successfully() {
        GroupRequestMeta meta = Instancio.create(GroupRequestMeta.class);
        SelectQueryParams params = Instancio.create(SelectQueryParams.class);
        Page<GroupEntity> page = new Page<>(2L, Instancio.ofList(GroupEntity.class).size(2).create());

        when(groupProcessingService.collectParamsForSelect(meta)).thenReturn(params);
        when(repository.findByParams(params)).thenReturn(Mono.just(page));

        Mono<GroupPageResponseDto> result = groupManagementService.getGroupsByParams(meta);

        StepVerifier.create(result)
                .consumeNextWith(responseDto -> {
                    assertThat(responseDto)
                            .extracting(GroupPageResponseDto::totalCount, GroupPageResponseDto::groups)
                            .matches(tuple -> tuple.getFirst().equals(2L) && !((List<?>) tuple.getLast()).isEmpty());
                    assertThat(responseDto.groups())
                            .doesNotContainNull();
                })
                .verifyComplete();

        verify(groupProcessingService).collectParamsForSelect(meta);
        verify(repository).findByParams(params);
    }
}
