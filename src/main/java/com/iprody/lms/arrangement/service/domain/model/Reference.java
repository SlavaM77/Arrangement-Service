package com.iprody.lms.arrangement.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reference {

    @NonNull
    private String ref;

    @NonNull
    private String type;
}
