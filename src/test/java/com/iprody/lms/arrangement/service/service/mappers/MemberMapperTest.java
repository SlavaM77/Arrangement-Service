package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.response.MemberResponseDto;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberMapperTest {

    private final MemberMapper mapper = new MemberMapperImpl();

    @Test
    void shouldMapMeetToDto_fromCorrectObject_successfully() {
        Member member = Instancio.create(Member.class);

        MemberResponseDto result = mapper.fromModel(member);

        assertThat(result)
                .extracting(MemberResponseDto::guid, MemberResponseDto::firstName, MemberResponseDto::lastName,
                        MemberResponseDto::avatar, MemberResponseDto::role, MemberResponseDto::enabled)
                .matches(tuple ->
                        tuple.get(0).equals(member.getGuid()) &&
                        tuple.get(1).equals(member.getFirstName()) &&
                        tuple.get(2).equals(member.getLastName()) &&
                        tuple.get(3).equals(member.getAvatar()) &&
                        tuple.get(4).equals(member.getRole().name()) &&
                        tuple.get(5).equals(member.isEnabled())
                );
    }

    @Test
    void shouldReturnNull_fromNull_successfully() {
        MemberResponseDto result = mapper.fromModel(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapMeetsToDtos_fromCorrectObjects_successfully() {
        List<Member> members = Instancio.ofList(Member.class).size(2).create();

        List<MemberResponseDto> result = mapper.fromModels(members);

        assertThat(result).hasSize(2)
                .doesNotContainNull();
    }

    @Test
    void shouldReturnEmptyDtoList_fromNull_successfully() {
        List<MemberResponseDto> result = mapper.fromModels(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyDtoList_fromEmptyList_successfully() {
        List<MemberResponseDto> result = mapper.fromModels(Collections.emptyList());

        assertThat(result).isEmpty();
    }
}
