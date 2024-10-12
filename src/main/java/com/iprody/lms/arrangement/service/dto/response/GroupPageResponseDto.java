package com.iprody.lms.arrangement.service.dto.response;

import java.util.List;

public record GroupPageResponseDto(
        long totalCount,
        List<GroupResponseDto> groups
) {
}
