package com.iprody.lms.arrangement.service.repository.query.builder;

import com.iprody.lms.arrangement.service.repository.query.filters.Filter;
import com.iprody.lms.arrangement.service.repository.query.set.SetExpression;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateQueryBuilder {

    private static final String WHITESPACE = " ";
    private static final String COMMA = ",";
    private static final String UPDATE = "UPDATE";
    private static final String SET = "SET";
    private static final String WHERE = "WHERE";
    private static final String AND = "AND";

    public static UpdateClause create() {
        return new Steps();
    }

    private static class Steps implements UpdateClause, SetClause, WhereClause, BuildStep {

        private final StringBuilder queryBuilder = new StringBuilder();

        private Steps() {
        }

        @Override
        public SetClause update(@NonNull String tableName) {
            queryBuilder.append(UPDATE)
                    .append(WHITESPACE)
                    .append(tableName)
                    .append(WHITESPACE);
            return this;
        }

        @Override
        public WhereClause set(List<SetExpression<?>> expressions) {
            if (CollectionUtils.isEmpty(expressions)) {
                throw new IllegalArgumentException("There must be at least one expression in the 'Update' query");
            }

            String setClause = expressions.stream()
                    .map(SetExpression::getSetClause)
                    .collect(Collectors.joining(COMMA));

            queryBuilder.append(SET)
                    .append(WHITESPACE)
                    .append(setClause)
                    .append(WHITESPACE);
            return this;
        }

        @Override
        public BuildStep where(List<Filter> filters) {
            if (CollectionUtils.isNotEmpty(filters)) {
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
        public String build() {
            return queryBuilder.toString().trim();
        }
    }

    public interface UpdateClause {
        SetClause update(@NonNull String tableName);
    }

    public interface SetClause {
        WhereClause set(List<SetExpression<?>> expressions);
    }

    public interface WhereClause extends BuildStep {
        BuildStep where(List<Filter> filters);
    }

    public interface BuildStep {
        String build();
    }
}
