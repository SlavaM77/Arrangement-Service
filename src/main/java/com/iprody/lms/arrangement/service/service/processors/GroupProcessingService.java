package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupGuidFilter;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.repository.query.set.ExpressionType;
import com.iprody.lms.arrangement.service.repository.query.set.SetExpression;
import com.iprody.lms.arrangement.service.service.mappers.MeetMapper;
import com.iprody.lms.arrangement.service.service.mappers.MeetMapperImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_NAME_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULED_FOR_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULE_FIELD;

@Service
@RequiredArgsConstructor
public class GroupProcessingService {

    private final MemberProcessingService memberProcessingService;
    private final ScheduleProcessingService scheduleProcessingService;
    private final MeetMapper mapper = new MeetMapperImpl();

    public UpdateQueryParams collectParamsForUpdate(@NonNull GroupEntity group,
                                                    @NonNull UpdateGroupRequestDto dto,
                                                    @NonNull List<Member> newMembers) {
        memberProcessingService.checkTeacherDuplication(group.getGroupData().getMembers(), newMembers);

        List<SetExpression<?>> expressions = new ArrayList<>();

        if (StringUtils.isNotEmpty(dto.name())) {
            expressions.add(new SetExpression<>(GROUP_NAME_FIELD, ExpressionType.GROUP_NAME_TYPE, dto.name())
            );
        }

        expressions.addAll(processSchedule(group, dto));

        if (!newMembers.isEmpty()) {
            expressions.add(new SetExpression<>(GROUP_DATA_FIELD, ExpressionType.MEMBERS_TYPE, newMembers)
            );
        }

        return new UpdateQueryParams(
                expressions,
                List.of(new GroupGuidFilter(dto.groupGuid()))
        );
    }

    private List<SetExpression<?>> processSchedule(@NonNull GroupEntity group,
                                                   @NonNull UpdateGroupRequestDto dto) {
        List<SetExpression<?>> scheduleExpressions = new ArrayList<>();

        if (dto.scheduledFor() != null) {
            if (CollectionUtils.isEmpty(dto.meets())) {
                scheduleProcessingService.validateSchedule(
                        dto.scheduledFor(),
                        group.getGroupData().getSchedule().getMeets());
            } else {
                List<Meet> newMeets = mapper.toModels(dto.meets());
                scheduleExpressions.add(new SetExpression<>(
                        SCHEDULE_FIELD,
                        ExpressionType.SCHEDULE_TYPE,
                        new Schedule(newMeets))
                );
            }
            scheduleExpressions.add(new SetExpression<>(
                    SCHEDULED_FOR_FIELD,
                    ExpressionType.SCHEDULED_FOR_TYPE,
                    dto.scheduledFor())
            );
        } else {
            if (CollectionUtils.isNotEmpty(dto.meets())) {
                List<Meet> newMeets = mapper.toModels(dto.meets());
                scheduleProcessingService.validateSchedule(
                        group.getScheduledFor(),
                        newMeets);

                scheduleExpressions.add(new SetExpression<>(
                        SCHEDULE_FIELD,
                        ExpressionType.SCHEDULE_TYPE,
                        new Schedule(newMeets))
                );
            }
        }

        return scheduleExpressions;
    }
}
