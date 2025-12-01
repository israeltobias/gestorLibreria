package es.biblioteca.userservice.application.service;

import es.biblioteca.userservice.application.exceptions.UserRegisteredException;
import es.biblioteca.userservice.application.port.in.AuthUseCase;
import es.biblioteca.userservice.domain.model.User;
import es.biblioteca.userservice.domain.repository.UserRepositoryPort;
import es.biblioteca.userservice.infrastructure.adapter.out.persistence.UserMapper;
import es.biblioteca.userservice.infrastructure.adapter.out.security.JwtProvider;
import es.biblioteca.userservice.infrastructure.dto.LoginRequest;
import es.biblioteca.userservice.infrastructure.dto.RegisterRequest;
import es.biblioteca.userservice.infrastructure.dto.TokenResponse;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserResponseDTO register(RegisterRequest registerRequest) {
        if (userRepositoryPort.findByUsername(registerRequest.username()).isPresent()) {
            throw new UserRegisteredException("User already exists");
        }
        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles(registerRequest.roles())
                .build();
        return UserMapper.toDTO(UserMapper.toEntity(userRepositoryPort.save(user)));
    }

    @Override
    public Optional<TokenResponse> login(LoginRequest loginRequest) {
        return userRepositoryPort.findByUsername(loginRequest.username())
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .map(jwtProvider::createToken);
    }
}
