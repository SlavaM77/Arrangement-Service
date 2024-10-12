package com.iprody.lms.arrangement.service.dto.response;

import java.time.Instant;
import java.util.List;

public record GroupResponseDto(
        String guid,
        String name,
        Instant scheduledFor,
        List<MeetResponseDto> meets,
        CourseResponseDto course,
        List<MemberResponseDto> members
) {
}
