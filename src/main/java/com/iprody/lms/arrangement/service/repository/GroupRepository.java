package com.iprody.lms.arrangement.service.repository;

import com.iprody.lms.arrangement.service.domain.model.GroupEntity;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import com.iprody.lms.arrangement.service.exception.UpdateFailedException;
import com.iprody.lms.arrangement.service.repository.query.builder.SelectQueryBuilder;
import com.iprody.lms.arrangement.service.repository.query.builder.UpdateQueryBuilder;
import com.iprody.lms.arrangement.service.repository.query.pagination.Page;
import com.iprody.lms.arrangement.service.repository.query.params.SelectQueryParams;
import com.iprody.lms.arrangement.service.repository.query.params.UpdateQueryParams;
import com.iprody.lms.arrangement.service.repository.util.GeneralSerializationUtils;
import com.iprody.lms.arrangement.service.repository.util.GroupEntitySerializationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.List;

import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GROUP_TABLE_NAME;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.GUID_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.ID_FIELD;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SELECT_ALL_FIELDS;
import static com.iprody.lms.arrangement.service.repository.query.constants.QueryConstants.SELECT_COUNT;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator transactionalOperator;

    private static final String SAVE_GROUP_QUERY = """
            INSERT INTO groups (guid, name, created_at, updated_at, scheduled_for, group_data)
            VALUES ($1, $2, $3, $4, $5, $6::jsonb)
            """;
    private static final String SELECT_GROUP_BY_GUID_QUERY =
            String.format("SELECT * FROM %s WHERE %s = $1", GROUP_TABLE_NAME, GUID_FIELD);

    public Mono<GroupEntity> save(GroupEntity groupEntity) {
        return databaseClient.sql(SAVE_GROUP_QUERY)
                .bind(0, groupEntity.getGuid())
                .bind(1, groupEntity.getName())
                .bind(2, groupEntity.getCreatedAt())
                .bind(3, groupEntity.getUpdatedAt())
                .bind(4, groupEntity.getScheduledFor())
                .bind(5, GeneralSerializationUtils.convertObjectToJsonString(groupEntity.getGroupData()))
                .filter(statement -> statement.returnGeneratedValues(ID_FIELD))
                .fetch()
                .first()
                .flatMap(result -> {
                    Long generatedId = (Long) result.get(ID_FIELD);
                    groupEntity.setId(generatedId);
                    return Mono.just(groupEntity);
                });
    }

    public Mono<Page<GroupEntity>> findByParams(SelectQueryParams params) {
        String searchQuery = SelectQueryBuilder.create()
                .select(SELECT_ALL_FIELDS)
                .from(GROUP_TABLE_NAME)
                .where(params.filters())
                .orderBy(params.sorting())
                .paginate(params.pagination())
                .build();
        String countQuery = SelectQueryBuilder.create()
                .select(SELECT_COUNT)
                .from(GROUP_TABLE_NAME)
                .where(params.filters())
                .build();

        Mono<List<GroupEntity>> groups = databaseClient.sql(searchQuery)
                .map(GroupEntitySerializationUtils::convert)
                .all()
                .collectList();
        Mono<Long> count = databaseClient.sql(countQuery)
                .map(row -> row.get(0, Long.class))
                .one();

        return Mono.zip(count, groups)
                .map(tuple -> new Page<>(tuple.getT1(), tuple.getT2()))
                .as(transactionalOperator::transactional);
    }

    public Mono<GroupEntity> findByGuid(String guid) {
        return databaseClient.sql(SELECT_GROUP_BY_GUID_QUERY)
                .bind(0, guid)
                .map(GroupEntitySerializationUtils::convert)
                .one()
                .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        MessageFormat.format("Group with groupGuid ''{0}'' not found", guid))));
    }

    public Mono<GroupEntity> update(UpdateQueryParams params, String groupGuid) {
        String updateQuery = UpdateQueryBuilder.create()
                .update(GROUP_TABLE_NAME)
                .set(params.expressions())
                .where(params.filters())
                .build();
        return databaseClient.sql(updateQuery)
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated == 0) {
                        return Mono.error(new UpdateFailedException(GroupEntity.class.getSimpleName()));
                    }
                    return findByGuid(groupGuid);
                });
    }
}
