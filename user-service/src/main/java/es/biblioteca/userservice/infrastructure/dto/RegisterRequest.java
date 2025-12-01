package es.biblioteca.userservice.infrastructure.dto;

import es.biblioteca.userservice.domain.annotations.LogMasked;
import es.biblioteca.userservice.domain.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RegisterRequest(@NotBlank(message = "El nombre de usuario no puede estar vacío") String username
        , @NotBlank(message = "La contraseña no puede estar vacía") @LogMasked String password
        , @NotEmpty(message = "La lista de roles no puede estar vacía.") List<Role> roles) {
}
