package com.iprody.lms.arrangement.service.integration.courceservice;

import com.iprody.lms.arrangement.service.domain.model.Course;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CourseService {

    public Mono<Course> getCourseByGuid(String courseGuid) {
        // todo - complete realization
        return Mono.just(mock());
    }

    private Course mock() {
        return Course.builder()
                .guid(UUID.randomUUID().toString())
                .summary("Summary")
                .description("Description")
                .build();
    }
}
