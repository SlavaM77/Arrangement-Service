package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.model.Meet;
import com.iprody.lms.arrangement.service.dto.request.MeetRequestDto;
import com.iprody.lms.arrangement.service.dto.response.MeetResponseDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper
public interface MeetMapper {

    Meet toNewModel(MeetRequestDto dto);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<Meet> toModels(List<MeetRequestDto> dtos);

    @Mapping(target = "status", expression = "java(meet.getStatus().name())")
    MeetResponseDto fromModel(Meet meet);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<MeetResponseDto> fromModels(List<Meet> meets);
}
