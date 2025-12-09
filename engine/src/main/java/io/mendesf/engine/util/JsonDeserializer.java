package io.mendesf.engine.util;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mendesf.engine.core.EngineConfigurationException;

public final class JsonDeserializer {

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    private JsonDeserializer() {
    }

    public static <T> T deserialize(String resourcePath, Class<T> type) {
        String normalizedPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream in = classLoader.getResourceAsStream(normalizedPath)) {
            if (in == null) {
                throw new EngineConfigurationException(
                    "Configuration file '" + resourcePath + "' not found on classpath"
                );
            }

            return MAPPER.readValue(in, type);

        } catch (JsonProcessingException e) {
            throw new EngineConfigurationException(
                "Invalid configuration format in '" + resourcePath + "': " + e.getOriginalMessage(), e
            );
        } catch (IOException e) {
            throw new EngineConfigurationException(
                "I/O error while reading configuration '" + resourcePath + "': " + e.getMessage(), e
            );
        }
    }
}
