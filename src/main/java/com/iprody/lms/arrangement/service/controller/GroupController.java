package com.iprody.lms.arrangement.service.controller;

import com.iprody.lms.arrangement.service.dto.request.AddGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.dto.response.GroupPageResponseDto;
import com.iprody.lms.arrangement.service.dto.response.GroupResponseDto;
import com.iprody.lms.arrangement.service.service.GroupManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupManagementService service;

    @Operation(summary = "Add new Group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Group was successfully created",
                    content = @Content(schema = @Schema(implementation = GroupResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PostMapping
    public Mono<GroupResponseDto> createGroup(@Valid @RequestBody AddGroupRequestDto request) {
        return service.saveGroup(request);
    }

    @Operation(summary = "Retrieve page of groups by params")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of groups retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GroupPageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @GetMapping
    public Mono<GroupPageResponseDto> getGroupsByParams(@Valid GroupRequestMeta meta) {
        return service.getGroupsByParams(meta);
    }

    @Operation(summary = "Update an existing group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group successfully updated",
                    content = @Content(schema = @Schema(implementation = GroupResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PutMapping
    public Mono<GroupResponseDto> updateGroup(@Valid @RequestBody UpdateGroupRequestDto request) {
        return service.updateGroup(request);
    }
}
