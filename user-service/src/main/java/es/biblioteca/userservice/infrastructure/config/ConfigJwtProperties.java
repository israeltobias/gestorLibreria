package es.biblioteca.userservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class ConfigJwtProperties {
    private String secret;
    private long expiration;
}
