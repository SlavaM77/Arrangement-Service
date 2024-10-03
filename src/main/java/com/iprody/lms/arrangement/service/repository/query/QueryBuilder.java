package com.iprody.lms.arrangement.service.repository.query;

import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.pagination.Pagination;
import com.iprody.lms.arrangement.service.repository.query.sorting.Sorting;
import lombok.NonNull;

import java.util.List;

public class QueryBuilder {

    private static final String WHITESPACE = " ";
    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String WHERE = "WHERE";
    private static final String AND = "AND";
    private static final String ORDER_BY = "ORDER BY";
    private static final String LIMIT = "LIMIT";
    private static final String OFFSET = "OFFSET";
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public static SelectStep create() {
        return new Steps();
    }

    private static class Steps implements SelectStep, FromStep, WhereStep, OrderByStep, PaginateStep {

        private final StringBuilder queryBuilder = new StringBuilder();

        private Steps() {
        }

        @Override
        public FromStep select(@NonNull String selectFields) {
            queryBuilder.append(SELECT)
                    .append(WHITESPACE)
                    .append(selectFields)
                    .append(WHITESPACE);
            return this;
        }

        @Override
        public WhereStep from(@NonNull String tableName) {
            queryBuilder.append(FROM)
                    .append(WHITESPACE)
                    .append(tableName)
                    .append(WHITESPACE);
            return this;
        }

        @Override
        public OrderByStep where(List<Filter> filters) {
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
        public PaginateStep orderBy(Sorting sorting) {
            if (sorting != null) {
                queryBuilder.append(ORDER_BY)
                        .append(WHITESPACE)
                        .append(sorting.getSortingBy())
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
                    .append(pagination != null ? (pagination.page() - 1) * pagination.size() : DEFAULT_PAGE);
            return this;
        }

        @Override
        public String build() {
            return queryBuilder.toString().trim();
        }
    }

    public interface SelectStep {
        FromStep select(@NonNull String selectFields);
    }

    public interface FromStep {
        WhereStep from(@NonNull String tableName);
    }

    public interface WhereStep extends OrderByStep {
        OrderByStep where(List<Filter> filters);
    }

    public interface OrderByStep extends PaginateStep {
        PaginateStep orderBy(Sorting sorting);
    }

    public interface PaginateStep extends BuildStep {
        BuildStep paginate(Pagination pagination);
    }

    public interface BuildStep {
        String build();
    }
}
