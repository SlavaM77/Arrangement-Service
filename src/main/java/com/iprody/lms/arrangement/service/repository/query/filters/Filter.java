package com.iprody.lms.arrangement.service.repository.query.filters;

/**
 * Interface for filter implementations which return conditions for 'WHERE' clauses.
 */

public interface Filter {

    String getFilterQuery();
}
