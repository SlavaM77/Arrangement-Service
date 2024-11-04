package com.iprody.lms.arrangement.service.repository.query.sorting;

import com.iprody.lms.arrangement.service.repository.query.set.ExpressionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.function.Supplier;

/**
 * This class processes data by defined enum {@link ExpressionType} to return 'SET' clause.
 * Abstract class for sorting realizations which return data for 'ORDER BY' clauses.
 */

public class Sorting {

    private final Supplier<String> stringSupplier;
    private final Sort.Direction direction;

    public Sorting(SortingType sortingType, String direction) {
        this.stringSupplier = sortingType.getSupplier();
        this.direction = parseDirection(direction);
    }

    private Sort.Direction parseDirection(String direction) {
        if (StringUtils.isEmpty(direction)) {
            return Sort.Direction.ASC;
        }
        try {
            return Sort.Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Sort.Direction.ASC;
        }
    }

    public String getSortingClause() {
        return stringSupplier.get();
    }

    public String getSortDirection() {
        return direction.name();
    }
}
