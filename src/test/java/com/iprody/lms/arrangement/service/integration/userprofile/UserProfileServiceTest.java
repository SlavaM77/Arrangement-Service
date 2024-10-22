package com.iprody.lms.arrangement.service.integration.userprofile;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import com.iprody.lms.arrangement.service.integration.dto.MemberDto;
import com.iprody.lms.arrangement.service.integration.mappers.MemberProfileMapper;
import com.iprody.lms.arrangement.service.integration.mappers.MemberProfileMapperImpl;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserProfileServiceTest {

    @Mock
    private UserProfileClient client;

    @InjectMocks
    private UserProfileService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final MemberProfileMapper mapper = new MemberProfileMapperImpl();

    @Test
    void shouldRetrieveMembersByGuids_whenMembersExist_successfully() {
        List<String> guids = List.of("guid1", "guid2");
        MemberDto memberDto1 = Instancio.of(MemberDto.class)
                .set(Select.field(MemberDto::role), MemberRole.INTERN.name())
                .create();
        MemberDto memberDto2 = Instancio.of(MemberDto.class)
                .set(Select.field(MemberDto::role), MemberRole.TEACHER.name())
                .create();
        Member member1 = mapper.toModel(memberDto1);
        Member member2 = mapper.toModel(memberDto2);

        when(client.getMembersByGuids(guids)).thenReturn(Flux.just(memberDto1, memberDto2));

        Flux<Member> members = service.getMembersByGuids(guids);
        StepVerifier.create(members)
                .assertNext(member -> assertThat(member).usingRecursiveComparison().isEqualTo(member1))
                .assertNext(member -> assertThat(member).usingRecursiveComparison().isEqualTo(member2))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmpty_whenGuidsListIsEmpty_successfully() {
        List<String> guids = List.of();

        StepVerifier.create(service.getMembersByGuids(guids))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldReturnError_whenNoMembersFound() {
        List<String> guids = List.of("guid1", "guid2");

        when(client.getMembersByGuids(guids)).thenReturn(Flux.empty());

        StepVerifier.create(service.getMembersByGuids(guids))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void shouldRetrieveMemberByGuid_whenMemberExists_successfully() {
        String guid = "guid";
        MemberDto memberDto = Instancio.of(MemberDto.class)
                .set(Select.field(MemberDto::role), MemberRole.INTERN.name())
                .create();
        Member member = mapper.toModel(memberDto);

        when(client.getMemberByGuid(guid)).thenReturn(Mono.just(memberDto));

        StepVerifier.create(service.getMemberByGuid(guid))
                .assertNext(m -> assertThat(m).usingRecursiveComparison().isEqualTo(member))
                .verifyComplete();
    }

    @Test
    void shouldReturnError_whenMemberNotFound() {
        String guid = "guid";
        when(client.getMemberByGuid(guid)).thenReturn(Mono.empty());

        StepVerifier.create(service.getMemberByGuid(guid))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void shouldThrow_withNullGuid() {
        StepVerifier.create(service.getMemberByGuid(null))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Member guid must not be null or empty."))
                .verify();
    }

    @Test
    void shouldThrow_withEmptyGuid() {
        StepVerifier.create(service.getMemberByGuid(""))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Member guid must not be null or empty."))
                .verify();
    }
}
