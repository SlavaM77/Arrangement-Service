package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.dto.response.CourseResponseDto;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseMapperTest {

    private final CourseMapper mapper = new CourseMapperImpl();

    @Test
    void shouldMapCourseToDto_fromCorrectObject_successfully() {
        Course course = Instancio.create(Course.class);

        CourseResponseDto result = mapper.toDto(course);

        assertThat(result).isEqualTo(new CourseResponseDto(course.getGuid(), course.getSummary()));
    }

    @Test
    void shouldReturnNull_fromNull_successfully() {
        CourseResponseDto result = mapper.toDto(null);

        assertThat(result).isNull();
    }
}
