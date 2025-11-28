package es.biblioteca.userservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class ConfigCorsProperties {
    private String[] allowedOrigins;
    private String[] allowedMethods;
    private String[] exposedHeaders;
}
