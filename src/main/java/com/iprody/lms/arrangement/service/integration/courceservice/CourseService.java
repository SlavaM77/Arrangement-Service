package com.iprody.lms.arrangement.service.integration.courceservice;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import com.iprody.lms.arrangement.service.integration.mappers.CourseTemplateMapperImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseServiceClient client;

    public Mono<Course> getCourseByGuid(String guid) {
        if (StringUtils.isBlank(guid)) {
            return Mono.error(new IllegalArgumentException("Course guid must not be null or empty."));
        }

        return client.getCourse(guid)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        MessageFormat.format("Course with guid ''{0}'' not found in the Course service", guid))))
                .map(new CourseTemplateMapperImpl()::toModel);
    }
}
