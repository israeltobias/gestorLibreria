package es.biblioteca.userservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "api")
@Data
public class ConfigErrorMessagesProperties {
    private final Map<HttpStatus, String> errorMessages;

    public String getMessage(HttpStatus status) {
        return errorMessages.getOrDefault(status, errorMessages.get(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
