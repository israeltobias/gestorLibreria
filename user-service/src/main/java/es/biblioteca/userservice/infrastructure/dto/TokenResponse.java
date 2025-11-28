package es.biblioteca.userservice.infrastructure.dto;

import es.biblioteca.userservice.domain.annotations.LogMasked;

public record TokenResponse(@LogMasked String token) {
}
