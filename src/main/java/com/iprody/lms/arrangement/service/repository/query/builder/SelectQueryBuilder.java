package com.iprody.lms.arrangement.service.repository.query.builder;

import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.pagination.Pagination;
import com.iprody.lms.arrangement.service.repository.query.sorting.Sorting;
import lombok.NonNull;

import java.util.List;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.DEFAULT_OFFSET;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.DEFAULT_PAGE_SIZE;

public class SelectQueryBuilder {

    private static final String WHITESPACE = " ";
    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String WHERE = "WHERE";
    private static final String AND = "AND";
    private static final String ORDER_BY = "ORDER BY";
    private static final String LIMIT = "LIMIT";
    private static final String OFFSET = "OFFSET";

    public static SelectClause create() {
        return new Steps();
    }

    private static class Steps implements SelectClause, FromClause, WhereClause, OrderByClause, PaginateClause {

        private final StringBuilder queryBuilder = new StringBuilder();

        private Steps() {
        }

        @Override
        public FromClause select(@NonNull String selectFields) {
            queryBuilder.append(SELECT)
                    .append(WHITESPACE)
                    .append(selectFields)
                    .append(WHITESPACE);
            return this;
        }

        @Override
        public WhereClause from(@NonNull String tableName) {
            queryBuilder.append(FROM)
                    .append(WHITESPACE)
                    .append(tableName)
                    .append(WHITESPACE);
            return this;
        }

        @Override
        public OrderByClause where(List<Filter> filters) {
            if (filters != null && !filters.isEmpty()) {
                queryBuilder.append(WHERE)
                        .append(WHITESPACE)
                        .append(filters.getFirst().getFilterQuery())
                        .append(WHITESPACE);

                for (int i = 1; i < filters.size(); i++) {
                    queryBuilder.append(AND)
                            .append(WHITESPACE)
                            .append(filters.get(i).getFilterQuery())
                            .append(WHITESPACE);
                }
            }
            return this;
        }

        @Override
        public PaginateClause orderBy(Sorting sorting) {
            if (sorting != null) {
                queryBuilder.append(ORDER_BY)
                        .append(WHITESPACE)
                        .append(sorting.getSortingClause())
                        .append(WHITESPACE)
                        .append(sorting.getSortDirection())
                        .append(WHITESPACE);
            }
            return this;
        }

        @Override
        public BuildStep paginate(Pagination pagination) {
            queryBuilder.append(LIMIT)
                    .append(WHITESPACE)
                    .append(pagination != null ? pagination.size() : DEFAULT_PAGE_SIZE)
                    .append(WHITESPACE)
                    .append(OFFSET)
                    .append(WHITESPACE)
                    .append(pagination != null ? (pagination.page() - 1) * pagination.size() : DEFAULT_OFFSET);
            return this;
        }

        @Override
        public String build() {
            return queryBuilder.toString().trim();
        }
    }

    public interface SelectClause {
        FromClause select(@NonNull String selectFields);
    }

    public interface FromClause {
        WhereClause from(@NonNull String tableName);
    }

    public interface WhereClause extends OrderByClause {
        OrderByClause where(List<Filter> filters);
    }

    public interface OrderByClause extends PaginateClause {
        PaginateClause orderBy(Sorting sorting);
    }

    public interface PaginateClause extends BuildStep {
        BuildStep paginate(Pagination pagination);
    }

    public interface BuildStep {
        String build();
    }
}
