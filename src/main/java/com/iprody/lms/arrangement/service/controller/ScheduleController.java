package com.iprody.lms.arrangement.service.controller;

import com.iprody.lms.arrangement.service.dto.request.AddScheduleRequestDto;
import com.iprody.lms.arrangement.service.dto.request.MarkMeetCompletedRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateScheduleRequestDto;
import com.iprody.lms.arrangement.service.dto.response.ScheduleResponseDto;
import com.iprody.lms.arrangement.service.service.ScheduleManagementService;
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
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleManagementService service;

    @Operation(summary = "Add new schedule to a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule successfully added",
                    content = @Content(schema = @Schema(implementation = ScheduleResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Requested resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PostMapping()
    public Mono<ScheduleResponseDto> addSchedule(@Valid @RequestBody AddScheduleRequestDto request) {
        return service.addSchedule(request);
    }

    @Operation(summary = "Reschedule an existing schedule for a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule successfully updated",
                    content = @Content(schema = @Schema(implementation = ScheduleResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Requested resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PutMapping()
    public Mono<ScheduleResponseDto> rescheduleSchedule(@Valid @RequestBody UpdateScheduleRequestDto request) {
        return service.rescheduleSchedule(request);
    }

    @Operation(summary = "Mark meet as completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meet successfully marked as completed",
                    content = @Content(schema = @Schema(implementation = ScheduleResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Requested resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PutMapping("/meet/complete")
    public Mono<ScheduleResponseDto> markMeetCompleted(@Valid @RequestBody MarkMeetCompletedRequestDto request) {
        return service.markMeetCompleted(request);
    }
}
