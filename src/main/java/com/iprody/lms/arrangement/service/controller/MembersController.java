package com.iprody.lms.arrangement.service.controller;

import com.iprody.lms.arrangement.service.dto.request.AddMemberRequestDto;
import com.iprody.lms.arrangement.service.dto.request.DeactivateMemberRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.dto.response.MemberResponseDto;
import com.iprody.lms.arrangement.service.service.MembersManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MembersController {

    private final MembersManagementService service;

    @Operation(summary = "Add new member to a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member successfully added",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PostMapping()
    public Mono<GroupResponseDto> addMember(@Valid @RequestBody AddMemberRequestDto request) {
        return service.addMember(request);
    }

    @Operation(summary = "Deactivate a member of the group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member successfully deactivated",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PutMapping()
    public Mono<GroupResponseDto> deactivateMember(@Valid @RequestBody DeactivateMemberRequestDto request) {
        return service.deactivateMember(request);
    }
}
