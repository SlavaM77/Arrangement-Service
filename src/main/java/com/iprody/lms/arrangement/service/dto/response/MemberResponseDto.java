package com.iprody.lms.arrangement.service.dto.response;

public record MemberResponseDto(
        String guid,
        String firstName,
        String lastName,
        String avatar,
        String role,
        boolean enabled
) {
}
