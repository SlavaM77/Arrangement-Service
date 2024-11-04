package com.iprody.lms.arrangement.service.integration.userprofile;

import com.iprody.lms.arrangement.service.integration.dto.MemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserProfileClient {

    private final WebClient webClient;

    private static final String MEMBER_GET_BY_ID_URI = "/members/{guid}";
    private static final String MEMBERS_GET_BY_IDS_URI = "/members/{guids}";

    public UserProfileClient(WebClient.Builder webClientBuilder,
                             @Value("${integrations.user-profile-service.base-url}") String userProfileServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(userProfileServiceUrl).build();
    }

    public Flux<MemberDto> getMembersByGuids(List<String> guids) {
        return webClient.get()
                .uri(MEMBERS_GET_BY_IDS_URI, guids)
                .retrieve()
                .bodyToFlux(MemberDto.class);
    }

    public Mono<MemberDto> getMemberByGuid(String guid) {
        return webClient.get()
                .uri(MEMBER_GET_BY_ID_URI, guid)
                .retrieve()
                .bodyToMono(MemberDto.class);
    }
}
