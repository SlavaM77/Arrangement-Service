package com.iprody.lms.arrangement.service.repository.query.set;

import java.util.function.Function;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SET_EXPRESSION;

/**
 * This class processes data by defined enum {@link ExpressionType} to return 'SET' clause.
 */

public class SetExpression<T> {

    private final String field;
    private final Function<T, String> function;
    private final T argument;

    public SetExpression(String field, ExpressionType expressionType, T argument) {
        this.field = field;
        this.function = expressionType.getFunction();
        this.argument = argument;
    }

    public String getSetClause() {
        return String.format(SET_EXPRESSION, field, function.apply(argument));
    }
}
