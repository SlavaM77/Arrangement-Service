package com.iprody.lms.arrangement.service.repository.query.params;

import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.set.SetExpression;

import java.util.List;

/**
 * <p>This class represents possible parameters for 'update' query construction.</p>
 * <ul>
 *   <li>{@link #expressions} - contains list of {@link SetExpression} with data for 'SET' clause.</li>
 *   <li>{@link #filters} - contains list of {@link Filter} with data for 'WHERE' clause.</li>
 * </ul>
 */

public record UpdateQueryParams(
        List<SetExpression<?>> expressions,
        List<Filter> filters
) {
}
