package com.iprody.lms.arrangement.service.integration.mappers;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.integration.dto.CourseDto;
import org.mapstruct.Mapper;

@Mapper
public interface CourseTemplateMapper {

    Course toModel(CourseDto dto);
}
