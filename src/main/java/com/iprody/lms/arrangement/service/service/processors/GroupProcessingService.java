package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupGuidFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.GroupNameFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.MemberFilter;
import com.iprody.lms.arrangement.service.repository.query.filters.StartDateFilter;
import com.iprody.lms.arrangement.service.repository.query.pagination.Pagination;
import com.iprody.lms.arrangement.service.repository.query.params.SelectQueryParams;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.repository.query.set.ExpressionType;
import com.iprody.lms.arrangement.service.repository.query.set.SetExpression;
import com.iprody.lms.arrangement.service.repository.query.sorting.Sorting;
import com.iprody.lms.arrangement.service.repository.query.sorting.SortingType;
import com.iprody.lms.arrangement.service.service.mappers.MeetMapper;
import com.iprody.lms.arrangement.service.service.mappers.MeetMapperImpl;
import com.iprody.lms.arrangement.service.util.TimeConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.DEFAULT_PAGE;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.DEFAULT_PAGE_SIZE;
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

    public SelectQueryParams collectParamsForSelect(@NonNull GroupRequestMeta meta) {
        List<Filter> filters = new ArrayList<>();

        if (StringUtils.isNotEmpty(meta.name())) {
            filters.add(new GroupNameFilter(meta.name()));
        }

        if (CollectionUtils.isNotEmpty(meta.memberIds()) && StringUtils.isNotEmpty(meta.role())) {
            filters.add(new MemberFilter(meta.memberIds(), MemberRole.valueOf(meta.role().toUpperCase())));
        }

        if (StringUtils.isNotEmpty(meta.date())) {
            Instant searchDate = TimeConverter.convertStringDateToInstant(meta.date());
            filters.add(new StartDateFilter(searchDate, StartDateFilter.Expression.FROM));
            filters.add(new StartDateFilter(searchDate.plus(1, ChronoUnit.DAYS), StartDateFilter.Expression.TO));
        }

        if (StringUtils.isNotEmpty(meta.dateFrom())) {
            filters.add(new StartDateFilter(
                    TimeConverter.convertStringDateToInstant(meta.dateFrom()),
                    StartDateFilter.Expression.FROM));
        }

        if (StringUtils.isNotEmpty(meta.dateTo())) {
            filters.add(new StartDateFilter(
                    TimeConverter.convertStringDateToInstant(meta.dateTo()).plus(1, ChronoUnit.DAYS),
                    StartDateFilter.Expression.TO));
        }

        Sorting sorting = null;
        if (StringUtils.isNotEmpty(meta.sortBy())) {
            switch (meta.sortBy()) {
                case "date" -> sorting = new Sorting(SortingType.START_DAY_SORTING, meta.sort());
                case "teacher" -> sorting = new Sorting(SortingType.MENTOR_TYPE, meta.sort());
                default -> {
                }
            }
        }

        Pagination pagination = new Pagination(
                meta.page() != null ? meta.page() : DEFAULT_PAGE,
                meta.size() != null ? meta.size() : DEFAULT_PAGE_SIZE);

        return new SelectQueryParams(
                filters,
                sorting,
                pagination);
    }

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
