package com.iprody.lms.arrangement.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupData {

    @NonNull
    @Builder.Default
    private Schedule schedule = new Schedule();

    @NonNull
    private Course course;

    @NonNull
    @Builder.Default
    private List<Member> members = new ArrayList<>();
}
