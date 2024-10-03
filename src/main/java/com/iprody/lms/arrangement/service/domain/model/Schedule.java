package com.iprody.lms.arrangement.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @NonNull
    private List<Meet> meets = new ArrayList<>();
}
