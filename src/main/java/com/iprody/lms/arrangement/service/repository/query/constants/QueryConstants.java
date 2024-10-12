package com.iprody.lms.arrangement.service.repository.query.constants;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

public class QueryConstants {

    public static final String GROUP_TABLE_NAME;
    public static final String ID_FIELD;
    public static final String GUID_FIELD;
    public static final String GROUP_NAME_FIELD;
    public static final String SCHEDULED_FOR_FIELD;
    public static final String GROUP_DATA_FIELD;
    public static final String SELECT_ALL_FIELDS = "*";
    public static final String SELECT_COUNT = "count(*)";
    public static final String MEMBERS_FIELD = "members";
    public static final String SCHEDULE_FIELD = "schedule";
    public static final String LASTNAME_FIELD = "lastName";
    public static final String ROLE_FIELD = "role";
    public static final String MENTOR_ROLE = MemberRole.TEACHER.name();

    static {
        try {
            GROUP_TABLE_NAME = GroupEntity.class.getAnnotation(Table.class).name();
            ID_FIELD = GroupEntity.class.getDeclaredField("id")
                    .getAnnotation(Column.class).value();
            GUID_FIELD = GroupEntity.class.getDeclaredField("guid")
                    .getAnnotation(Column.class).value();
            GROUP_NAME_FIELD = GroupEntity.class.getDeclaredField("name")
                    .getAnnotation(Column.class).value();
            GROUP_DATA_FIELD = GroupEntity.class.getDeclaredField("groupData")
                    .getAnnotation(Column.class).value();
            SCHEDULED_FOR_FIELD = GroupEntity.class.getDeclaredField("scheduledFor")
                    .getAnnotation(Column.class).value();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error while initializing QueryConstants", e);
        }
    }
}
