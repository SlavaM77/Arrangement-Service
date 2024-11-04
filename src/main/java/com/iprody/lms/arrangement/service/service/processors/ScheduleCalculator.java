package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MeetStatus;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

/**
 * <p>This class is defined to calculate an additional meet date based on the current schedule (list of meets}).
 * The date of the calculated meet will be the day on which this meet would have taken place
 * if the schedule had continued further.
 * The time of the calculated meet will be the time of the last meet of the received schedule.
 * The calculation is carried out only for meets that have not yet taken place.</p>
 */

@Component
public class ScheduleCalculator {

    private static final int DAYS_AT_WEEK = 7;

    Instant calculateAdditionalMeetDate(@NonNull List<Meet> meets) {
        List<Meet> notHappenedMeets = meets.stream()
                .filter(meet -> meet.getStatus() != MeetStatus.HAPPENED)
                .toList();
        if (notHappenedMeets.isEmpty()) {
            throw new IllegalStateException("No meet available to calculate the additional date");
        }

        Meet lastMeet = notHappenedMeets.stream()
                .max(Comparator.comparing(Meet::getScheduledFor))
                .get();
        Instant lastMeetDate = lastMeet.getScheduledFor();
        DayOfWeek lastMeetDay = lastMeetDate.atZone(ZoneId.systemDefault()).getDayOfWeek();

        List<DayOfWeek> meetDaysOfWeek = notHappenedMeets.stream()
                .filter(meet -> meet.getStatus() != MeetStatus.HAPPENED)
                .map(meet -> meet.getScheduledFor()
                        .atZone(ZoneId.systemDefault())
                        .getDayOfWeek())
                .distinct()
                .sorted()
                .toList();

        DayOfWeek additionalMeetDay = meetDaysOfWeek.stream()
                .filter(day -> day.getValue() > lastMeetDay.getValue())
                .findFirst()
                .orElse(meetDaysOfWeek.getFirst());

        int daysToAdd = (additionalMeetDay.getValue() == lastMeetDay.getValue())
                ? DAYS_AT_WEEK
                : (additionalMeetDay.getValue() - lastMeetDay.getValue() + DAYS_AT_WEEK) % DAYS_AT_WEEK;

        return lastMeetDate.plus(daysToAdd, ChronoUnit.DAYS);
    }
}
