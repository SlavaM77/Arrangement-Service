package com.iprody.lms.arrangement.service.repository.query;

import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.sorting.Sorting;
import com.iprody.lms.arrangement.service.repository.query.pagination.Pagination;

import java.util.List;

/**
 * <p>This class represents possible parameters for 'select' query construction.</p>
 * <ul>
 *   <li>{@link #filters} - contains list of {@link Filter} with data for 'WHERE' clause.</li>
 *   <li>{@link #sorting} - contains {@link Sorting} data for 'ORDER BY' clause.</li>
 *   <li>{@link #pagination} - contains {@link Pagination} data for 'LIMIT' and 'OFFSET' clauses.</li>
 * </ul>
 */

public record QueryParams(
        List<Filter> filters,
        Sorting sorting,
        Pagination pagination
) {
}
