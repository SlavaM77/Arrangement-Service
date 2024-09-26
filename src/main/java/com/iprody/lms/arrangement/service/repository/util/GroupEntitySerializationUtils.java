package com.iprody.lms.arrangement.service.repository.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.lms.arrangement.service.domain.model.GroupData;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import io.r2dbc.spi.Readable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class GroupEntitySerializationUtils {

    private final ObjectMapper objectMapper;

    public GroupEntity convert(Readable row) {
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

    public String convertGroupDataToJsonString(GroupData data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException(
                    MessageFormat.format("Failed to serialize GroupData: {0}", data), e);
        }
    }

    private GroupData convertJsonToGroupData(String json) {
        try {
            GroupData groupData = objectMapper.readValue(json, GroupData.class);
            groupData.getSchedule().getMeets().forEach(meet -> {
                meet.setScheduledFor(truncateToSeconds(meet.getScheduledFor()));
                if (meet.getHappenedAt() != null) {
                    meet.setHappenedAt(truncateToSeconds(meet.getHappenedAt()));
                }
            });
            return groupData;
        } catch (JsonProcessingException e) {
            throw new SerializationException(
                    MessageFormat.format("Failed to deserialize GroupData: {0}", json), e);
        }
    }

    private Instant truncateToSeconds(Instant instant) {
        return instant.truncatedTo(ChronoUnit.SECONDS);
    }
}
