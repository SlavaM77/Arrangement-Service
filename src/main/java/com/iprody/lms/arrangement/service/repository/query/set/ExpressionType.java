package com.iprody.lms.arrangement.service.repository.query.set;

import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.repository.util.GeneralSerializationUtils;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_QUERY;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULE_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULE_QUERY;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.STRING_PARAM;

/**
 * This class contains defined functions for 'SET' clauses construction.
 * It is a parameter for {@link SetExpression}.
 */

@RequiredArgsConstructor
public enum ExpressionType {

    GROUP_NAME_TYPE(ExpressionRegistry.getFunction(ExpressionRegistry.GROUP_NAME)),
    SCHEDULED_FOR_TYPE(ExpressionRegistry.getFunction(ExpressionRegistry.SCHEDULED_FOR)),
    MEMBERS_TYPE(ExpressionRegistry.getFunction(ExpressionRegistry.MEMBERS)),
    SCHEDULE_TYPE(ExpressionRegistry.getFunction(ExpressionRegistry.SCHEDULE));

    private final Function<?, String> function;

    public <T> Function<T, String> getFunction() {
        return (Function<T, String>) function;
    }

    private static class ExpressionRegistry {
        private static final Map<String, Function<?, String>> expressionMap = new HashMap<>();

        private static final String GROUP_NAME = "GROUP_NAME";
        private static final String SCHEDULED_FOR = "SCHEDULED_FOR";
        private static final String MEMBERS = "MEMBERS";
        private static final String SCHEDULE = "SCHEDULE";

        static {
            expressionMap.put(GROUP_NAME, (Function<String, String>) groupName ->
                    String.format(STRING_PARAM, groupName));

            expressionMap.put(SCHEDULED_FOR, (Function<Instant, String>) scheduledFor ->
                    String.format(STRING_PARAM, scheduledFor));

            expressionMap.put(MEMBERS, (Function<List<Member>, String>) members -> {
                String membersJson = GeneralSerializationUtils.convertObjectToJsonString(members);
                return MessageFormat.format(
                        MEMBERS_QUERY,
                        GROUP_DATA_FIELD,
                        MEMBERS_FIELD,
                        membersJson
                );
            });

            expressionMap.put(SCHEDULE, (Function<Schedule, String>) schedule -> {
                String scheduleJson = GeneralSerializationUtils.convertObjectToJsonString(schedule);
                return MessageFormat.format(
                        SCHEDULE_QUERY,
                        GROUP_DATA_FIELD,
                        SCHEDULE_FIELD,
                        scheduleJson
                );
            });
        }

        private static Function<?, String> getFunction(String expressionType) {
            return expressionMap.get(expressionType);
        }
    }
}
