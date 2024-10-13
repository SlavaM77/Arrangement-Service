package com.iprody.lms.arrangement.service.integration.userprofile;

import com.iprody.lms.arrangement.service.domain.model.Member;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserProfileService {

    public Flux<Member> getMembersByGuids(List<String> guids) {
        if (CollectionUtils.isEmpty(guids)) {
            return Flux.just();
        }
        // todo - complete realization
        return Flux.just();
    }

    public Mono<Member> getMemberByGuid(String guid) {
        // todo - complete realization
        return Mono.just(new Member());
    }
}
