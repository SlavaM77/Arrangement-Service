package com.iprody.lms.arrangement.service.repository.query.pagination;

import java.util.List;

/**
 * <p>This class contains data for implementing pagination.</p>
 * <ul>
 *   <li>{@link #totalCount} - contains total amount of objects in DB (according filters).</li>
 *   <li>{@link #content} - contains paged data objects.</li>
 * </ul>
 */

public record Page<T>(
        long totalCount,
        List<T> content

) {
}
