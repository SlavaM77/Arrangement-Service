package com.iprody.lms.arrangement.service.service;

import com.iprody.lms.arrangement.service.domain.model.GroupData;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.dto.request.AddScheduleRequestDto;
import com.iprody.lms.arrangement.service.dto.request.MarkMeetCompletedRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateScheduleRequestDto;
import com.iprody.lms.arrangement.service.dto.response.ScheduleResponseDto;
import com.iprody.lms.arrangement.service.exception.ScheduleAlreadyExistsException;
import com.iprody.lms.arrangement.service.repository.GroupRepository;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.service.processors.ScheduleProcessingService;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleManagementServiceTest {

    @InjectMocks
    private ScheduleManagementService scheduleManagementService;

    @Mock
    private ScheduleProcessingService scheduleProcessingService;
    @Mock
    private GroupRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCompleteAddScheduleFlow_successfully() {
        AddScheduleRequestDto dto = Instancio.create(AddScheduleRequestDto.class);
        GroupEntity group = Instancio.of(GroupEntity.class)
                .set(Select.field(GroupData::getSchedule), new Schedule())
                .create();

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(repository.update(any(), any())).thenReturn(Mono.just(group));

        Mono<ScheduleResponseDto> result = scheduleManagementService.addSchedule(dto);

        StepVerifier.create(result)
                .consumeNextWith(responseDto -> {
                    assertThat(responseDto.groupGuid()).isEqualTo(dto.groupGuid());
                    assertThat(responseDto.meets())
                            .hasSize(group.getGroupData().getSchedule().getMeets().size())
                            .doesNotContainNull();
                })
                .verifyComplete();

        verify(repository).findByGuid(dto.groupGuid());
        verify(repository).update(any(UpdateQueryParams.class), anyString());
    }

    @Test
    void shouldReturnError_whenScheduleAlreadyExists_successfully() {
        AddScheduleRequestDto dto = Instancio.create(AddScheduleRequestDto.class);
        GroupEntity group = Instancio.create(GroupEntity.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));

        Mono<ScheduleResponseDto> result = scheduleManagementService.addSchedule(dto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ScheduleAlreadyExistsException)
                .verify();

        verify(repository, times(1)).findByGuid(dto.groupGuid());
        verify(repository, never()).update(any(UpdateQueryParams.class), anyString());
    }

    @Test
    void shouldCompleteRescheduleScheduleFlow_successfully() {
        UpdateScheduleRequestDto dto = Instancio.create(UpdateScheduleRequestDto.class);
        GroupEntity group = Instancio.of(GroupEntity.class)
                .set(Select.field(GroupData::getSchedule), new Schedule())
                .create();
        Schedule updatedSchedule = Instancio.create(Schedule.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(scheduleProcessingService.recalculateSchedule(group.getGroupData().getSchedule(), dto))
                .thenReturn(updatedSchedule);
        when(repository.update(any(), any())).thenReturn(Mono.just(group));

        Mono<ScheduleResponseDto> result = scheduleManagementService.rescheduleSchedule(dto);

        StepVerifier.create(result)
                .consumeNextWith(responseDto -> {
                    assertThat(responseDto.groupGuid()).isEqualTo(dto.groupGuid());
                    assertThat(responseDto.meets())
                            .hasSize(group.getGroupData().getSchedule().getMeets().size())
                            .doesNotContainNull();
                })
                .verifyComplete();

        verify(repository).findByGuid(dto.groupGuid());
        verify(scheduleProcessingService).recalculateSchedule(group.getGroupData().getSchedule(), dto);
        verify(repository).update(any(UpdateQueryParams.class), anyString());
    }

    @Test
    void shouldCompleteMarkMeetCompletedFlow_successfully() {
        MarkMeetCompletedRequestDto dto = Instancio.create(MarkMeetCompletedRequestDto.class);
        GroupEntity group = Instancio.of(GroupEntity.class)
                .set(Select.field(GroupData::getSchedule), new Schedule())
                .create();
        Schedule updatedSchedule = Instancio.create(Schedule.class);

        when(repository.findByGuid(dto.groupGuid())).thenReturn(Mono.just(group));
        when(scheduleProcessingService.markMeetCompleted(group.getGroupData().getSchedule(), dto))
                .thenReturn(updatedSchedule);
        when(repository.update(any(), any())).thenReturn(Mono.just(group));

        Mono<ScheduleResponseDto> result = scheduleManagementService.markMeetCompleted(dto);

        StepVerifier.create(result)
                .consumeNextWith(responseDto -> {
                    assertThat(responseDto.groupGuid()).isEqualTo(dto.groupGuid());
                    assertThat(responseDto.meets())
                            .hasSize(group.getGroupData().getSchedule().getMeets().size())
                            .doesNotContainNull();
                })
                .verifyComplete();

        verify(repository).findByGuid(dto.groupGuid());
        verify(scheduleProcessingService).markMeetCompleted(group.getGroupData().getSchedule(), dto);
        verify(repository).update(any(UpdateQueryParams.class), anyString());
    }
}
