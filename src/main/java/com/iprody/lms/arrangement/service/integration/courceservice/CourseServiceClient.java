package com.iprody.lms.arrangement.service.integration.courceservice;

import com.iprody.lms.arrangement.service.integration.dto.CourseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceClient {

    private final WebClient webClient;

    private static final String COURSE_GET_BY_ID_URI = "/courses/{guid}";

    public CourseServiceClient(WebClient.Builder webClientBuilder,
                               @Value("${integrations.course-service.base-url}") String courseServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(courseServiceUrl).build();
    }

    public Mono<CourseDto> getCourse(String courseGuid) {
        return webClient.get()
                .uri(COURSE_GET_BY_ID_URI, courseGuid)
                .retrieve()
                .bodyToMono(CourseDto.class);
    }
}
