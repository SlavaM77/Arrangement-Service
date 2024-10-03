package com.iprody.lms.arrangement.service.repository;

import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.repository.query.QueryBuilder;
import com.iprody.lms.arrangement.service.repository.query.QueryParams;
import com.iprody.lms.arrangement.service.repository.util.GroupEntitySerializationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_TABLE_NAME;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.ID_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SELECT_ALL_FIELDS;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    private final DatabaseClient databaseClient;
    private final GroupEntitySerializationUtils utils;

    private static final String SAVE_GROUP_QUERY = """
            INSERT INTO groups (guid, name, created_at, updated_at, scheduled_for, group_data)
            VALUES ($1, $2, $3, $4, $5, $6::jsonb)
            """;
    private static final String SAVE_GROUP_BY_GUID_QUERY = "SELECT * FROM groups WHERE guid = $1";

    public Mono<GroupEntity> save(GroupEntity groupEntity) {
        return databaseClient.sql(SAVE_GROUP_QUERY)
                .bind(0, groupEntity.getGuid())
                .bind(1, groupEntity.getName())
                .bind(2, groupEntity.getCreatedAt())
                .bind(3, groupEntity.getUpdatedAt())
                .bind(4, groupEntity.getScheduledFor())
                .bind(5, utils.convertGroupDataToJsonString(groupEntity.getGroupData()))
                .filter(statement -> statement.returnGeneratedValues(ID_FIELD))
                .fetch()
                .first()
                .flatMap(result -> {
                    Long generatedId = (Long) result.get(ID_FIELD);
                    groupEntity.setId(generatedId);
                    return Mono.just(groupEntity);
                });
    }

    public Flux<GroupEntity> findByParams(QueryParams params) {
        String query = QueryBuilder.create()
                .select(SELECT_ALL_FIELDS)
                .from(GROUP_TABLE_NAME)
                .where(params.filters())
                .orderBy(params.sorting())
                .paginate(params.pagination())
                .build();
        return databaseClient.sql(query)
                .map(utils::convert)
                .all();
    }

    public Mono<GroupEntity> findByGuid(String guid) {
        return databaseClient.sql(SAVE_GROUP_BY_GUID_QUERY)
                .bind(0, guid)
                .map(utils::convert)
                .one();
    }
}
