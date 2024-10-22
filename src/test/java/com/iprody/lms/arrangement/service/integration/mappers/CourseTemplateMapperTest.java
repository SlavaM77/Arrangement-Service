package com.iprody.lms.arrangement.service.integration.mappers;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.integration.dto.CourseDto;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseTemplateMapperTest {

    private final CourseTemplateMapper mapper = new CourseTemplateMapperImpl();

    @Test
    void shouldMapDtoToCourse_fromCorrectObject_successfully() {
        CourseDto dto = Instancio.create(CourseDto.class);

        Course result = mapper.toModel(dto);

        assertThat(result)
                .extracting(Course::getGuid, Course::getSummary, Course::getDescription, Course::getHomework)
                .containsExactly(dto.guid(), dto.summary(), dto.description(), dto.homework());

    }

    @Test
    void shouldReturnNull_fromNull_successfully() {
        Course result = mapper.toModel(null);

        assertThat(result).isNull();
    }
}
