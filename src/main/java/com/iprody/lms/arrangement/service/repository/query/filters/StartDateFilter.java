package com.iprody.lms.arrangement.service.repository.query.filters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.time.Instant;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULED_FOR_FIELD;

/**
 * {@link Filter} implementation for filtering by group's 'scheduledFor' field with different comparison operators.
 */

@RequiredArgsConstructor
public class StartDateFilter implements Filter {

    private final Instant searchDate;
    private final Expression expression;

    private static final String START_DATE_FILTER_QUERY = "{0} {1} ''{2}''";

    @Override
    public String getFilterQuery() {
        return MessageFormat.format(START_DATE_FILTER_QUERY,
                SCHEDULED_FOR_FIELD,
                expression.symbol,
                searchDate);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Expression {
        FROM(">="), TO("<="), EQUAL("=");

        private final String symbol;
    }
}
