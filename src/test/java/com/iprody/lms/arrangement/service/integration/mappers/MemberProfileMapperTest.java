package com.iprody.lms.arrangement.service.integration.mappers;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.integration.dto.MemberDto;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberProfileMapperTest {

    private final MemberProfileMapper mapper = new MemberProfileMapperImpl();

    @Test
    void shouldMapDtoToCourse_fromCorrectObject_successfully() {
        MemberDto dto = Instancio.of(MemberDto.class)
                .set(Select.field(MemberDto::role), MemberRole.INTERN.name())
                .create();

        Member result = mapper.toModel(dto);

        assertThat(result)
                .extracting(Member::getGuid, Member::getFirstName, Member::getLastName, Member::getAvatar,
                        Member::getRole, Member::isEnabled)
                .containsExactly(dto.guid(), dto.firstName(), dto.lastName(), dto.avatar(),
                        MemberRole.INTERN, dto.enabled());

    }

    @Test
    void shouldReturnNull_fromNull_successfully() {
        Member result = mapper.toModel(null);

        assertThat(result).isNull();
    }
}
