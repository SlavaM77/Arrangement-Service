package com.iprody.lms.arrangement.service.service;

import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.dto.request.AddScheduleRequestDto;
import com.iprody.lms.arrangement.service.dto.request.MarkMeetCompletedRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateScheduleRequestDto;
import com.iprody.lms.arrangement.service.dto.response.ScheduleResponseDto;
import com.iprody.lms.arrangement.service.exception.ScheduleAlreadyExistsException;
import com.iprody.lms.arrangement.service.repository.GroupRepository;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupGuidFilter;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.repository.query.set.ExpressionType;
import com.iprody.lms.arrangement.service.repository.query.set.SetExpression;
import com.iprody.lms.arrangement.service.service.mappers.MeetMapper;
import com.iprody.lms.arrangement.service.service.mappers.MeetMapperImpl;
import com.iprody.lms.arrangement.service.service.processors.ScheduleProcessingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULE_FIELD;

@Service
@RequiredArgsConstructor
public class ScheduleManagementService {

    private final ScheduleProcessingService scheduleProcessingService;
    private final GroupRepository repository;
    private final MeetMapper mapper = new MeetMapperImpl();

    public Mono<ScheduleResponseDto> addSchedule(AddScheduleRequestDto dto) {
        String groupGuid = dto.groupGuid();
        return repository.findByGuid(groupGuid)
                .flatMap(groupEntity -> {
                    List<Meet> existed = groupEntity.getGroupData().getSchedule().getMeets();
                    if (CollectionUtils.isNotEmpty(existed)) {
                        return Mono.error(new ScheduleAlreadyExistsException(groupGuid));
                    }

                    Schedule newSchedule = new Schedule();
                    newSchedule.setMeets(mapper.toModels(dto.meets()));
                    UpdateQueryParams params = new UpdateQueryParams(
                            List.of(new SetExpression<>(SCHEDULE_FIELD, ExpressionType.SCHEDULE_TYPE, newSchedule)),
                            List.of(new GroupGuidFilter(groupGuid))
                    );

                    return repository.update(params, groupGuid);
                })
                .map(group -> new ScheduleResponseDto(
                        groupGuid,
                        mapper.fromModels(group.getGroupData().getSchedule().getMeets())));
    }

    public Mono<ScheduleResponseDto> rescheduleSchedule(UpdateScheduleRequestDto dto) {
        String groupGuid = dto.groupGuid();
        return repository.findByGuid(groupGuid)
                .flatMap(groupEntity -> {
                    Schedule updatedSchedule = scheduleProcessingService.recalculateSchedule(
                            groupEntity.getGroupData().getSchedule(), dto);
                    UpdateQueryParams params = new UpdateQueryParams(
                            List.of(new SetExpression<>(SCHEDULE_FIELD, ExpressionType.SCHEDULE_TYPE, updatedSchedule)),
                            List.of(new GroupGuidFilter(groupGuid))
                    );

                    return repository.update(params, groupGuid);
                })
                .map(group -> new ScheduleResponseDto(
                        groupGuid,
                        mapper.fromModels(group.getGroupData().getSchedule().getMeets())));
    }

    public Mono<ScheduleResponseDto> markMeetCompleted(MarkMeetCompletedRequestDto dto) {
        String groupGuid = dto.groupGuid();
        return repository.findByGuid(groupGuid)
                .flatMap(groupEntity -> {
                    Schedule updatedSchedule =
                            scheduleProcessingService.markMeetCompleted(groupEntity.getGroupData().getSchedule(), dto);
                    UpdateQueryParams params = new UpdateQueryParams(
                            List.of(new SetExpression<>(SCHEDULE_FIELD, ExpressionType.SCHEDULE_TYPE, updatedSchedule)),
                            List.of(new GroupGuidFilter(groupGuid))
                    );

                    return repository.update(params, groupGuid);
                })
                .map(group -> new ScheduleResponseDto(
                        groupGuid,
                        mapper.fromModels(group.getGroupData().getSchedule().getMeets())));
    }
}
