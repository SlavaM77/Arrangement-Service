package com.iprody.lms.arrangement.service.service;

import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.request.AddMemberRequestDto;
import com.iprody.lms.arrangement.service.dto.request.DeactivateMemberRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.integration.userprofile.UserProfileService;
import com.iprody.lms.arrangement.service.repository.GroupRepository;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupGuidFilter;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.repository.query.set.ExpressionType;
import com.iprody.lms.arrangement.service.repository.query.set.SetExpression;
import com.iprody.lms.arrangement.service.service.mappers.GroupMapper;
import com.iprody.lms.arrangement.service.service.mappers.GroupMapperImpl;
import com.iprody.lms.arrangement.service.service.processors.MemberProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;

@Service
@RequiredArgsConstructor
public class MembersManagementService {

    private final UserProfileService userProfileService;
    private final MemberProcessingService memberProcessingService;
    private final GroupRepository repository;
    private final GroupMapper mapper = new GroupMapperImpl();

    public Mono<GroupResponseDto> addMember(AddMemberRequestDto dto) {
        String groupGuid = dto.groupGuid();
        return Mono.zip(userProfileService.getMemberByGuid(dto.memberGuid()),
                        repository.findByGuid(groupGuid))
                .flatMap(tuple -> {
                    Member member = tuple.getT1();
                    GroupEntity groupEntity = tuple.getT2();

                    List<Member> members = memberProcessingService.addNewMember(
                            groupEntity.getGroupData().getMembers(),
                            member);
                    UpdateQueryParams params = new UpdateQueryParams(
                            List.of(new SetExpression<>(GROUP_DATA_FIELD, ExpressionType.MEMBERS_TYPE, members)),
                            List.of(new GroupGuidFilter(groupGuid))
                    );

                    return repository.update(params, groupGuid);
                })
                .map(mapper::fromEntity);
    }

    public Mono<GroupResponseDto> deactivateMember(DeactivateMemberRequestDto dto) {
        String groupGuid = dto.groupGuid();
        return repository.findByGuid(groupGuid)
                .flatMap(groupEntity -> {
                    List<Member> members = memberProcessingService.disableMember(
                            groupEntity.getGroupData().getMembers(),
                            dto.memberGuid(),
                            groupGuid);
                    UpdateQueryParams params = new UpdateQueryParams(
                            List.of(new SetExpression<>(GROUP_DATA_FIELD, ExpressionType.MEMBERS_TYPE, members)),
                            List.of(new GroupGuidFilter(groupGuid))
                    );

                    return repository.update(params, groupGuid);
                })
                .map(mapper::fromEntity);
    }
}
