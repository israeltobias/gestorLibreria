package es.biblioteca.userservice.application.port.in;

import es.biblioteca.userservice.infrastructure.dto.LoginRequest;
import es.biblioteca.userservice.infrastructure.dto.RegisterRequest;
import es.biblioteca.userservice.infrastructure.dto.TokenResponse;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;

import java.util.Optional;

public interface AuthUseCase {
    UserResponseDTO register(RegisterRequest authRequest);

    Optional<TokenResponse> login(LoginRequest authRequest);
}
