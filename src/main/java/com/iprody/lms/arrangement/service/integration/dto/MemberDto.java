package com.iprody.lms.arrangement.service.integration.dto;

public record MemberDto(
        String guid,
        String firstName,
        String lastName,
        String avatar,
        String role,
        boolean enabled
) {
}
