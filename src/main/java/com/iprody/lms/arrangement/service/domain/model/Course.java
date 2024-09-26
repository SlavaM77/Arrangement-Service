package com.iprody.lms.arrangement.service.domain.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @NonNull
    private String guid;

    @NonNull
    private String summary;

    @NonNull
    private String description;

    private String homework;

    @NonNull
    private List<Reference> references = new ArrayList<>();

    @NonNull
    private List<Assessment> assessments = new ArrayList<>();
}
