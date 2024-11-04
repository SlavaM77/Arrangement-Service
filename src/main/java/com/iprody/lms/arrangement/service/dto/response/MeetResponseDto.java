package com.iprody.lms.arrangement.service.dto.response;

import java.time.Instant;

public record MeetResponseDto(
        Instant scheduledFor,
        String conferenceRef,
        Instant happenedAt,
        String status
) {
}
