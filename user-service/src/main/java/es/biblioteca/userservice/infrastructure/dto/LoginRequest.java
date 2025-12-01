package es.biblioteca.userservice.infrastructure.dto;

import es.biblioteca.userservice.domain.annotations.LogMasked;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "El nombre de usuario no puede estar vacío") String username
        , @NotBlank(message = "La contraseña no puede estar vacía") @LogMasked String password) {
}
