package es.biblioteca.userservice.application.port.in;

import es.biblioteca.userservice.infrastructure.dto.AuthRequest;
import es.biblioteca.userservice.infrastructure.dto.TokenResponse;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;

import java.util.Optional;

public interface AuthUseCase {
    UserResponseDTO register(AuthRequest authRequest);

    Optional<TokenResponse> login(AuthRequest authRequest);
}
