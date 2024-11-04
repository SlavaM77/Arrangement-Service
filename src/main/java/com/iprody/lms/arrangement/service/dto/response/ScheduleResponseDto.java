package com.iprody.lms.arrangement.service.dto.response;

import java.util.List;

public record ScheduleResponseDto(
        String groupGuid,
        List<MeetResponseDto> meets) {
}
