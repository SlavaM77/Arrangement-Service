package com.iprody.lms.arrangement.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "groups")
public class GroupEntity {

    @Id
    @Column("id")
    private long id;

    @NonNull
    @Column("guid")
    @Builder.Default
    private String guid = UUID.randomUUID().toString();

    @NonNull
    @Column("name")
    private String name;

    @NonNull
    @Column("created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @NonNull
    @Column("updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @NonNull
    @Column("scheduled_for")
    private Instant scheduledFor;

    @NonNull
    @Column("group_data")
    private GroupData groupData;
}
