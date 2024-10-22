package com.iprody.lms.arrangement.service.integration.userprofile;

import com.iprody.lms.arrangement.service.domain.model.Member;
import com.iprody.lms.arrangement.service.exception.EntityNotFoundException;
import com.iprody.lms.arrangement.service.integration.mappers.MemberProfileMapper;
import com.iprody.lms.arrangement.service.integration.mappers.MemberProfileMapperImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileClient client;

    private final MemberProfileMapper mapper = new MemberProfileMapperImpl();

    public UserProfileService(UserProfileClient client) {
        this.client = client;
    }

    public Flux<Member> getMembersByGuids(List<String> guids) {
        if (CollectionUtils.isEmpty(guids)) {
            return Flux.just();
        }

        return client.getMembersByGuids(guids)
                .switchIfEmpty(Flux.error(new EntityNotFoundException(
                        MessageFormat.format(
                                "Members with guids ''{0}'' not found in the UserProfile service", guids))))
                .map(mapper::toModel);
    }

    public Mono<Member> getMemberByGuid(String guid) {
        if (StringUtils.isBlank(guid)) {
            return Mono.error(new IllegalArgumentException("Member guid must not be null or empty."));
        }

        return client.getMemberByGuid(guid)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        MessageFormat.format(
                                "Member with guid ''{0}'' not found in the UserProfile service", guid))))
                .map(mapper::toModel);
    }
}
