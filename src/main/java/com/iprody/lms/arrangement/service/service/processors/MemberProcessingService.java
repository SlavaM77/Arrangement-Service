package com.iprody.lms.arrangement.service.service.processors;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberProcessingService {

    public List<Member> addNewMember(@NonNull List<Member> existingMembers,
                                     @NonNull Member member) {
        checkTeacherDuplication(existingMembers, member);
        existingMembers.add(member);
        return existingMembers;
    }

    public List<Member> disableMember(@NonNull List<Member> existingMembers,
                                      @NonNull String memberGuid,
                                      @NonNull String groupGuid) {
        Member member = existingMembers.stream()
                .filter(m -> m.getGuid().equals(memberGuid))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format(
                                "Member with memberGuid ''{0}'' not found in group ''{1}''", memberGuid, groupGuid)));
        member.setEnabled(false);
        return existingMembers;
    }

    void checkTeacherDuplication(@NonNull List<Member> existingMembers,
                                 @NonNull List<Member> newMembers) {
        checkTeacherDuplication(newMembers);
        newMembers.forEach(member -> checkTeacherDuplication(existingMembers, member));
    }

    private void checkTeacherDuplication(@NonNull List<Member> newMembers) {
        long teacherCount = newMembers.stream()
                .filter(member -> member.getRole() == MemberRole.TEACHER)
                .count();
        if (teacherCount > 1) {
            throw new IllegalStateException("A group can contain a maximum of one member with 'TEACHER' role");
        }
    }

    private void checkTeacherDuplication(@NonNull List<Member> existingMembers,
                                         @NonNull Member newMember) {
        if (newMember.getRole() == MemberRole.TEACHER) {
            boolean teacherExists = existingMembers.stream()
                    .anyMatch(member -> member.getRole() == MemberRole.TEACHER);
            if (teacherExists) {
                throw new IllegalStateException("A member with 'TEACHER' role already exists. Cannot add another.");
            }
        }
    }
}
