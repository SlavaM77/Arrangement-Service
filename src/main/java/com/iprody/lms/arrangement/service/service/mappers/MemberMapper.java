package com.iprody.lms.arrangement.service.service.mappers;

import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.dto.response.MemberResponseDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper
public interface MemberMapper {

    @Mapping(target = "role", expression = "java(member.getRole() != null ? member.getRole().name() : null)")
    MemberResponseDto fromModel(Member member);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<MemberResponseDto> fromModels(List<Member> members);
}
