package com.iprody.lms.arrangement.service.integration.courceservice;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import com.iprody.lms.arrangement.service.integration.dto.CourseDto;
import com.iprody.lms.arrangement.service.integration.mappers.CourseTemplateMapperImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CourseServiceTest {

    @Mock
    private CourseServiceClient client;

    @InjectMocks
    private CourseService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRetrieveMemberByGuid_whenMemberExists_successfully() {
        String guid = "guid";
        CourseDto courseDto = Instancio.create(CourseDto.class);
        Course course = new CourseTemplateMapperImpl().toModel(courseDto);

        when(client.getCourse(guid))
                .thenReturn(Mono.just(courseDto));

        StepVerifier.create(service.getCourseByGuid(guid))
                .assertNext(c -> assertThat(c).usingRecursiveComparison().isEqualTo(course))
                .verifyComplete();
    }

    @Test
    void shouldReturnError_whenCourseNotFound() {
        String guid = "guid";
        when(client.getCourse(guid)).thenReturn(Mono.empty());

        StepVerifier.create(service.getCourseByGuid(guid))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void shouldThrow_withNullGuid() {
        StepVerifier.create(service.getCourseByGuid(null))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Course guid must not be null or empty."))
                .verify();
    }

    @Test
    void shouldThrow_withEmptyGuid() {
        StepVerifier.create(service.getCourseByGuid(""))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Course guid must not be null or empty."))
                .verify();
    }
}
