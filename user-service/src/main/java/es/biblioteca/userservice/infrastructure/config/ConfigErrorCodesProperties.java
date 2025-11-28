package es.biblioteca.userservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "api")
@Data
public class ConfigErrorCodesProperties {
    // Spring convertirá automáticamente las claves del YAML (404, 401)
    // a las claves del Map (HttpStatus.NOT_FOUND, etc.)
    private Map<HttpStatus, String> errorCodes;

    // Método de utilidad para facilitar el acceso
    public String getCode(HttpStatus status) {
        return errorCodes.getOrDefault(status, errorCodes.get(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
