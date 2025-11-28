package es.biblioteca.userservice.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(String code, String message, Instant timestamp, String path) {
    // Constructor adicional para casos simples
    public ErrorResponse(String code, String message) {
        this(code, message, Instant.now(), null);
    }
}