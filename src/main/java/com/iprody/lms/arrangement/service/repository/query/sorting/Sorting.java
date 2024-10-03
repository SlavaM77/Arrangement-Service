package com.iprody.lms.arrangement.service.repository.query.sorting;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * Abstract class for sorting realizations which return data for 'ORDER BY' clauses.
 */

@RequiredArgsConstructor
public abstract class Sorting {

    private final Sort.Direction direction;

    public abstract String getSortingBy();

    public String getSortDirection() {
        return direction.name();
    }
}
