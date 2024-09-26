package com.iprody.lms.arrangement.service.domain.model;

import com.iprody.lms.arrangement.service.domain.enums.MeetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meet {

    @NonNull
    private Instant scheduledFor;

    private String conferenceRef;

    private Instant happenedAt;

    @NonNull
    @Builder.Default
    private MeetStatus status = MeetStatus.SCHEDULED;
}
