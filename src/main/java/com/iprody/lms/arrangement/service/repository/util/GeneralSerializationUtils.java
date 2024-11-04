package com.iprody.lms.arrangement.service.repository.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.lms.arrangement.service.config.ObjectMapperFactory;
import org.apache.commons.lang3.SerializationException;

import java.text.MessageFormat;

public class GeneralSerializationUtils {

    private static final ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();

    public static <T> String convertObjectToJsonString(T data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException(
                    MessageFormat.format("Failed to serialize data: {0}", data), e);
        }
    }

    public static <T> T convertJsonStringToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new SerializationException(
                    MessageFormat.format("Failed to deserialize json string into class {0}: {1}",
                            clazz.getName(), json), e);
        }
    }
}
