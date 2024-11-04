package com.iprody.lms.arrangement.service.repository.query.set;

import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.domain.model.Schedule;
import com.iprody.lms.arrangement.service.repository.util.GeneralSerializationUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_DATA_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.MEMBERS_QUERY;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULE_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SCHEDULE_QUERY;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.STRING_PARAM;
import static org.assertj.core.api.Assertions.assertThat;

class ExpressionTypeTest {

    @Test
    void shouldReturnCorrectExpression_groupNameType_successfully() {
        String groupName = "Test group";
        Function<String, String> groupNameFunction = ExpressionType.GROUP_NAME_TYPE.getFunction();
        String result = groupNameFunction.apply(groupName);

        assertThat(result).isEqualTo(String.format(STRING_PARAM, groupName));
    }

    @Test
    void shouldReturnCorrectExpression_scheduledForType_successfully() {
        Instant now = Instant.now();
        Function<Instant, String> scheduledForFunction = ExpressionType.SCHEDULED_FOR_TYPE.getFunction();
        String result = scheduledForFunction.apply(now);

        assertThat(result).isEqualTo(String.format(STRING_PARAM, now));
    }

    @Test
    void shouldReturnCorrectExpression_membersType_successfully() {
        List<Member> members = Instancio.createList(Member.class);
        String expected = MessageFormat.format(
                MEMBERS_QUERY,
                GROUP_DATA_FIELD,
                MEMBERS_FIELD,
                GeneralSerializationUtils.convertObjectToJsonString(members));
        Function<List<Member>, String> membersFunction = ExpressionType.MEMBERS_TYPE.getFunction();

        String result = membersFunction.apply(members);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnCorrectExpression_scheduleType_successfully() {
        Schedule schedule = Instancio.create(Schedule.class);
        String expected = MessageFormat.format(
                SCHEDULE_QUERY,
                GROUP_DATA_FIELD,
                SCHEDULE_FIELD,
                GeneralSerializationUtils.convertObjectToJsonString(schedule));
        Function<Schedule, String> scheduleFunction = ExpressionType.SCHEDULE_TYPE.getFunction();

        String result = scheduleFunction.apply(schedule);

        assertThat(result).isEqualTo(expected);
    }
}
