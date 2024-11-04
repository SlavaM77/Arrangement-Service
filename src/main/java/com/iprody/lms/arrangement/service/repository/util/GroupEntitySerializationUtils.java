package com.iprody.lms.arrangement.service.repository.util;

import com.iprody.lms.arrangement.service.domain.model.GroupData;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import io.r2dbc.spi.Readable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.requireNonNull;

public class GroupEntitySerializationUtils {

    public static GroupEntity convert(Readable row) {
        return GroupEntity.builder()
                .id(requireNonNull(row.get("id", Long.class)))
                .guid(requireNonNull(row.get("guid", String.class)))
                .name(requireNonNull(row.get("name", String.class)))
                .createdAt(truncateToSeconds(requireNonNull(row.get("created_at", Instant.class))))
                .updatedAt(truncateToSeconds(requireNonNull(row.get("updated_at", Instant.class))))
                .scheduledFor(truncateToSeconds(requireNonNull(row.get("scheduled_for", Instant.class))))
                .groupData(convertJsonToGroupData(row.get("group_data", String.class)))
                .build();
    }

    private static GroupData convertJsonToGroupData(String json) {
        GroupData groupData = GeneralSerializationUtils.convertJsonStringToObject(json, GroupData.class);
        groupData.getSchedule().getMeets().forEach(meet -> {
            meet.setScheduledFor(truncateToSeconds(meet.getScheduledFor()));
            if (meet.getHappenedAt() != null) {
                meet.setHappenedAt(truncateToSeconds(meet.getHappenedAt()));
            }
        });
        return groupData;
    }

    private static Instant truncateToSeconds(Instant instant) {
        return instant.truncatedTo(ChronoUnit.SECONDS);
    }
}
