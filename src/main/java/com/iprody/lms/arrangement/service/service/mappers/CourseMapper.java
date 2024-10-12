package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.model.Course;
import com.iprody.lms.arrangement.service.dto.response.CourseResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface CourseMapper {

    CourseResponseDto toDto(Course course);
}
