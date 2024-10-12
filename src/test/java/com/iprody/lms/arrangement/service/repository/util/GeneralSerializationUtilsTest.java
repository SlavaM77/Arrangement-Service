package com.iprody.lms.arrangement.service.repository.util;

import com.iprody.lms.arrangement.service.domain.model.Assessment;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeneralSerializationUtilsTest {

    @Test
    void convertObjectToJsonString_shouldReturnJsonString_successfully() {
        Assessment assessment = new Assessment("uuid", "ref", 1, "comment");
        String expected = "{\"author\":\"uuid\",\"ref\":\"ref\",\"grade\":1,\"comment\":\"comment\"}";

        String result = GeneralSerializationUtils.convertObjectToJsonString(assessment);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void convertJsonStringToObject_shouldReturnObject_successfully() {
        String json = "{\"author\":\"uuid\",\"ref\":\"ref\",\"grade\":1,\"comment\":\"comment\"}";
        Assessment expectedObject = new Assessment("uuid", "ref", 1, "comment");

        Assessment result = GeneralSerializationUtils.convertJsonStringToObject(json, Assessment.class);

        assertThat(result).isEqualTo(expectedObject);
    }
}
