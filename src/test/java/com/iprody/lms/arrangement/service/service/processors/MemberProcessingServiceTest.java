package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberProcessingServiceTest {

    private final MemberProcessingService service = new MemberProcessingService();

    @Test
    void shouldAddMember_withOnlyInterns_successfully() {
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        int initialSize = existingMembers.size();
        Member member = Instancio.create(Member.class);

        List<Member> result = service.addNewMember(existingMembers, member);

        assertThat(result)
                .hasSize(initialSize + 1)
                .contains(member);
    }

    @Test
    void shouldThrow_withTeacherInExistingAndNewTeacher() {
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .size(1)
                .set(Select.field(Member::getRole), MemberRole.TEACHER)
                .create();
        Member member = Instancio.of(Member.class)
                .set(Select.field(Member::getRole), MemberRole.TEACHER)
                .create();

        assertThatThrownBy(() -> service.addNewMember(existingMembers, member))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDisableMember_withExistingMember_successfully() {
        String guid = UUID.randomUUID().toString();
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::isEnabled), true)
                .create();
        existingMembers.getFirst().setGuid(guid);
        int initialSize = existingMembers.size();

        List<Member> result = service.disableMember(existingMembers, guid, UUID.randomUUID().toString());

        assertThat(result).hasSize(initialSize)
                .filteredOn(member -> member.getGuid().equals(guid))
                .singleElement()
                .satisfies(member -> assertThat(member.isEnabled()).isFalse());
    }

    @Test
    void shouldThrow_withoutExistingMember_successfully() {
        String guid = UUID.randomUUID().toString();
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::isEnabled), true)
                .create();

        assertThatThrownBy(() -> service.disableMember(existingMembers, guid, UUID.randomUUID().toString()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDoNothing_withoutTeachers_successfully() {
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        List<Member> newMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();

        assertThatNoException().isThrownBy(() -> service.checkTeacherDuplication(existingMembers, newMembers));
    }

    @Test
    void shouldDoNothing_withExistingTeacherAndWithoutNewTeacher_successfully() {
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        existingMembers.getFirst().setRole(MemberRole.TEACHER);
        List<Member> newMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();

        assertThatNoException().isThrownBy(() -> service.checkTeacherDuplication(existingMembers, newMembers));
    }

    @Test
    void shouldDoNothing_withoutExistingTeachersAndWithOneNewTeacher_successfully() {
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        List<Member> newMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        newMembers.getFirst().setRole(MemberRole.TEACHER);

        assertThatNoException().isThrownBy(() -> service.checkTeacherDuplication(existingMembers, newMembers));
    }

    @Test
    void shouldThrow_withWithManyNewTeachers() {
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        List<Member> newMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.TEACHER)
                .create();

        assertThatThrownBy(() -> service.checkTeacherDuplication(existingMembers, newMembers))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("A group can contain a maximum of one member with 'TEACHER' role");
    }

    @Test
    void shouldThrow_withOneExistingTeacherAndWithOneNewTeacher() {
        List<Member> existingMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        existingMembers.getFirst().setRole(MemberRole.TEACHER);
        List<Member> newMembers = Instancio.ofList(Member.class)
                .set(Select.field(Member::getRole), MemberRole.INTERN)
                .create();
        newMembers.getFirst().setRole(MemberRole.TEACHER);

        assertThatThrownBy(() -> service.checkTeacherDuplication(existingMembers, newMembers))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("A member with 'TEACHER' role already exists. Cannot add another.");
    }
}
