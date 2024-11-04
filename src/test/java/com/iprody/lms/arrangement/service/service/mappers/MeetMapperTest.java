package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.enums.MeetStatus;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.dto.request.MeetRequestDto;
import com.iprody.lms.arrangement.service.dto.response.MeetResponseDto;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MeetMapperTest {

    private final MeetMapper mapper = new MeetMapperImpl();

    @Test
    void shouldMapDtoToMeet_fromCorrectObject_successfully() {
        MeetRequestDto dto = Instancio.create(MeetRequestDto.class);

        Meet result = mapper.toNewModel(dto);

        assertThat(result)
                .extracting(Meet::getScheduledFor, Meet::getConferenceRef, Meet::getHappenedAt, Meet::getStatus)
                .containsExactly(dto.scheduledFor(), dto.conferenceRef(), null, MeetStatus.SCHEDULED);

    }

    @Test
    void shouldReturnNullModel_fromNull_successfully() {
        Meet result = mapper.toNewModel(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapDtosToMeets_fromCorrectObjects_successfully() {
        List<MeetRequestDto> dtos = Instancio.ofList(MeetRequestDto.class).size(2).create();

        List<Meet> result = mapper.toModels(dtos);

        assertThat(result).hasSize(2)
                .doesNotContainNull();
    }

    @Test
    void shouldReturnEmptyMeetList_fromNull_successfully() {
        List<Meet> result = mapper.toModels(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyMeetList_fromEmptyList_successfully() {
        List<Meet> result = mapper.toModels(Collections.emptyList());

        assertThat(result).isEmpty();
    }


    @Test
    void shouldMapMeetToDto_fromCorrectObject_successfully() {
        Meet meet = Instancio.create(Meet.class);

        MeetResponseDto result = mapper.fromModel(meet);

        assertThat(result)
                .extracting(MeetResponseDto::scheduledFor, MeetResponseDto::conferenceRef,
                        MeetResponseDto::happenedAt, MeetResponseDto::status)
                .matches(tuple ->
                        tuple.get(0).equals(meet.getScheduledFor()) &&
                        tuple.get(1).equals(meet.getConferenceRef()) &&
                        tuple.get(2).equals(meet.getHappenedAt()) &&
                        tuple.get(3).equals(meet.getStatus().name()));
    }

    @Test
    void shouldReturnNullDto_fromNull_successfully() {
        MeetResponseDto result = mapper.fromModel(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapMeetsToDtos_fromCorrectObjects_successfully() {
        List<Meet> meets = Instancio.ofList(Meet.class).size(2).create();

        List<MeetResponseDto> result = mapper.fromModels(meets);

        assertThat(result).hasSize(2)
                .doesNotContainNull();
    }

    @Test
    void shouldReturnEmptyDtoList_fromNull_successfully() {
        List<MeetResponseDto> result = mapper.fromModels(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyDtoList_fromEmptyList_successfully() {
        List<MeetResponseDto> result = mapper.fromModels(Collections.emptyList());

        assertThat(result).isEmpty();
    }
}
