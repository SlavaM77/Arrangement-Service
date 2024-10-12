package com.iprody.lms.arrangement.service.dto.request;

import java.time.Instant;
import java.util.List;

public interface ScheduleDto {

    Instant scheduledFor();

    List<MeetRequestDto> meets();
}
