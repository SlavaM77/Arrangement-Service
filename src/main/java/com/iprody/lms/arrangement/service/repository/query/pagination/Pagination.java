package com.iprody.lms.arrangement.service.repository.query.pagination;

/**
 * This class contains data for 'LIMIT' and 'OFFSET' clauses.
 */

public record Pagination(int page, int size) {
}
