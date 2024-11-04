package com.iprody.lms.arrangement.service.service;

import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.request.AddMemberRequestDto;
import com.iprody.lms.arrangement.service.dto.request.DeactivateMemberRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import com.iprody.lms.arrangement.service.integration.userprofile.UserProfileService;
import com.iprody.lms.arrangement.service.repository.GroupRepository;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.service.processors.MemberProcessingService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MembersManagementServiceTest {

    @InjectMocks
    private MembersManagementService membersManagementService;

    @Mock
    private UserProfileService userProfileService;
    @Mock
    private MemberProcessingService memberProcessingService;
    @Mock
    private GroupRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCompleteAddMemberFlow_successfully() {
        AddMemberRequestDto dto = Instancio.create(AddMemberRequestDto.class);
        Member member = Instancio.create(Member.class);
        GroupEntity group = Instancio.create(GroupEntity.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(userProfileService.getMemberByGuid(dto.memberGuid())).thenReturn(Mono.just(member));
        when(memberProcessingService.addNewMember(group.getGroupData().getMembers(), member))
                .thenReturn(List.of());
        when(repository.update(any(), any())).thenReturn(Mono.just(group));

        Mono<GroupResponseDto> result = membersManagementService.addMember(dto);

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
        verify(userProfileService).getMemberByGuid(dto.memberGuid());
        verify(memberProcessingService).addNewMember(group.getGroupData().getMembers(), member);
        verify(repository).update(any(UpdateQueryParams.class), anyString());
    }

    @Test
    void shouldReturnError_whenUserProfileServiceReturnsError() {
        AddMemberRequestDto dto = Instancio.create(AddMemberRequestDto.class);
        GroupEntity group = Instancio.create(GroupEntity.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(userProfileService.getMemberByGuid(dto.memberGuid()))
                .thenReturn(Mono.error(new EntityNotFoundException("message")));

        Mono<GroupResponseDto> result = membersManagementService.addMember(dto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();

        verify(repository, times(1)).findByGuid(dto.groupGuid());
        verify(userProfileService, times(1)).getMemberByGuid(dto.memberGuid());
        verify(memberProcessingService, never()).addNewMember(any(), any());
        verify(repository, never()).update(any(UpdateQueryParams.class), anyString());
    }

    @Test
    void shouldCompleteDeactivateMemberFlow_successfully() {
        DeactivateMemberRequestDto dto = Instancio.create(DeactivateMemberRequestDto.class);
        GroupEntity group = Instancio.create(GroupEntity.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(memberProcessingService.disableMember(
                group.getGroupData().getMembers(),
                dto.memberGuid(),
                dto.groupGuid())
        )
                .thenReturn(List.of());
        when(repository.update(any(), any())).thenReturn(Mono.just(group));

        Mono<GroupResponseDto> result = membersManagementService.deactivateMember(dto);

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
        verify(memberProcessingService).disableMember(
                group.getGroupData().getMembers(),
                dto.memberGuid(),
                dto.groupGuid());
        verify(repository).update(any(UpdateQueryParams.class), anyString());
    }
}
