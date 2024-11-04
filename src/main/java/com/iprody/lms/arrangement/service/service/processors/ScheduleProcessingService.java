package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MeetStatus;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.dto.request.MarkMeetCompletedRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateScheduleRequestDto;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleProcessingService {

    private final ScheduleCalculator calculator;

    public Schedule recalculateSchedule(@NonNull Schedule schedule,
                                        @NonNull UpdateScheduleRequestDto dto) {
        List<Meet> meets = schedule.getMeets();
        Meet canceledMeet = meets.stream()
                .filter(meet -> meet.getScheduledFor().equals(dto.canceledTime()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format(
                                "Meet with 'scheduledFor' time = {0} not found for group with groupGuid ''{1}''",
                                dto.canceledTime(),
                                dto.groupGuid())));

        Instant nextMeetDate = calculator.calculateAdditionalMeetDate(meets);

        Meet newMeet = Meet.builder()
                .scheduledFor(nextMeetDate)
                .conferenceRef(canceledMeet.getConferenceRef())
                .build();

        meets.remove(canceledMeet);
        meets.add(newMeet);

        markRescheduled(meets, dto.canceledTime());

        return schedule;
    }

    public Schedule markMeetCompleted(@NonNull Schedule schedule,
                                      @NonNull MarkMeetCompletedRequestDto dto) {
        List<Meet> meets = schedule.getMeets();

        Meet meetToUpdate = meets.stream()
                .filter(meet -> meet.getScheduledFor().equals(dto.scheduledFor()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format(
                                "Meet with 'scheduledFor' time = {0} not found for group with groupGuid ''{1}''",
                                dto.scheduledFor(),
                                dto.groupGuid())));

        meetToUpdate.setHappenedAt(dto.happenedAt());
        meetToUpdate.setStatus(MeetStatus.HAPPENED);

        return schedule;
    }

    void validateSchedule(Instant scheduledFor, List<Meet> meets) {
        if (scheduledFor != null && CollectionUtils.isNotEmpty(meets)) {
            boolean isAnyBefore = meets.stream()
                    .anyMatch(meet -> meet.getScheduledFor().isBefore(scheduledFor));
            if (isAnyBefore) {
                throw new IllegalStateException("New meets start before 'scheduledFor' time.");
            }
        }
    }

    private void markRescheduled(List<Meet> meets, Instant canceledTime) {
        meets.stream()
                .filter(meet -> meet.getScheduledFor().isAfter(canceledTime))
                .forEach(meet -> meet.setStatus(MeetStatus.RESCHEDULED));
    }
}
