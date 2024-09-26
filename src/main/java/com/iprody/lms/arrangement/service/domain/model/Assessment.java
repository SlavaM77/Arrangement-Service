package com.iprody.lms.arrangement.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assessment {

    @NonNull
    private String author;

    @NonNull
    private String ref;

    private int grade;

    private String comment;
}
