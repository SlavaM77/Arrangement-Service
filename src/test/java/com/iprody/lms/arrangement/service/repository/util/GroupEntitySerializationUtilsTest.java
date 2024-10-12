package com.iprody.lms.arrangement.service.repository.util;

import com.iprody.lms.arrangement.service.domain.enums.MeetStatus;
import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.Assessment;
import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.domain.model.GroupData;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.domain.model.Reference;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import io.r2dbc.spi.Readable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GroupEntitySerializationUtilsTest {

    @Test
    public void shouldDeserializeGroupEntity_successfully() {
        Instant nowInstant = Instant.now();
        Instant expectedInstant = nowInstant.truncatedTo(ChronoUnit.SECONDS);
        String groupGuid = UUID.randomUUID().toString();
        String courseGuid = UUID.randomUUID().toString();
        String teacherGuid = UUID.randomUUID().toString();
        String internGuid = UUID.randomUUID().toString();
        GroupData groupData = GroupData.builder()
                .course(Course.builder()
                        .summary("summary")
                        .description("desc")
                        .homework("homework")
                        .guid(courseGuid)
                        .references(List.of(new Reference("ref", "type")))
                        .assessments(List.of(Assessment.builder()
                                .ref("ref")
                                .grade(1)
                                .comment("comment")
                                .author(internGuid)
                                .build()))
                        .build())
                .schedule(new Schedule(List.of(
                        Meet.builder()
                                .conferenceRef("ref")
                                .happenedAt(expectedInstant)
                                .status(MeetStatus.SCHEDULED)
                                .scheduledFor(expectedInstant)
                                .build())))
                .members(List.of(
                        Member.builder()
                                .role(MemberRole.TEACHER)
                                .avatar("avatar")
                                .firstName("firstName")
                                .lastName("lastName")
                                .guid(teacherGuid)
                                .enabled(true)
                                .build(),
                        Member.builder()
                                .role(MemberRole.INTERN)
                                .avatar("avatar")
                                .firstName("firstName")
                                .lastName("lastName")
                                .guid(internGuid)
                                .enabled(false)
                                .build()))
                .build();
        String groupDataJson = GeneralSerializationUtils.convertObjectToJsonString(groupData);

        Readable mockRow = mock(Readable.class);
        when(mockRow.get("id", Long.class)).thenReturn(1L);
        when(mockRow.get("guid", String.class)).thenReturn(groupGuid);
        when(mockRow.get("name", String.class)).thenReturn("Test Group");
        when(mockRow.get("created_at", Instant.class)).thenReturn(nowInstant);
        when(mockRow.get("updated_at", Instant.class)).thenReturn(nowInstant);
        when(mockRow.get("scheduled_for", Instant.class)).thenReturn(nowInstant);
        when(mockRow.get("group_data", String.class)).thenReturn(groupDataJson);

        GroupEntity result = GroupEntitySerializationUtils.convert(mockRow);

        GroupEntity expected = GroupEntity.builder()
                .id(1L)
                .guid(groupGuid)
                .name("Test Group")
                .scheduledFor(expectedInstant)
                .createdAt(expectedInstant)
                .updatedAt(expectedInstant)
                .groupData(groupData)
                .build();

        assertThat(result).isEqualTo(expected);
    }
}
