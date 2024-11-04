package com.iprody.lms.arrangement.service.integration.mappers;

import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.integration.dto.MemberDto;
import org.mapstruct.Mapper;

@Mapper
public interface MemberProfileMapper {

    Member toModel(MemberDto dto);
}
