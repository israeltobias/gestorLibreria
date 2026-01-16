package es.biblioteca.userservice.application.service;

import es.biblioteca.userservice.application.exceptions.UserRegisteredException;
import es.biblioteca.userservice.domain.model.Role;
import es.biblioteca.userservice.domain.model.User;
import es.biblioteca.userservice.domain.repository.UserRepositoryPort;
import es.biblioteca.userservice.infrastructure.adapter.out.security.JwtProvider;
import es.biblioteca.userservice.infrastructure.dto.LoginRequest;
import es.biblioteca.userservice.infrastructure.dto.RegisterRequest;
import es.biblioteca.userservice.infrastructure.dto.TokenResponse;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("AuthService test")
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    UserRepositoryPort userRepositoryPort;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtProvider jwtProvider;

    @InjectMocks
    AuthServiceImpl authService;

    // =========================================================================
    // TESTS PARA register(..)
    // =========================================================================

    @Test
    @DisplayName("Debería crear un usuario correctamente")
    void register_shouldReturnUserResponseDTO_WhenValidReegisterRequest() {

        UserResponseDTO expecteUserResponseDTO = new UserResponseDTO();
        String username = "username";
        String password = "password";
        String encodedPassword = "encodedPassword";
        List<Role> roles = List.of(Role.USER, Role.ADMIN);
        HashSet<Role> hashSetRoles = new HashSet<>(roles);
        expecteUserResponseDTO.setUsername(username);
        expecteUserResponseDTO.setRoles(roles);
        User user = new User(1L, username, encodedPassword, hashSetRoles);
        RegisterRequest request = new RegisterRequest(username, password, roles);


        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepositoryPort.save(any(User.class))).thenReturn(user);

        UserResponseDTO actualResponse = authService.register(request);

        assertThat(actualResponse)
                .isNotNull()
                .isInstanceOf(UserResponseDTO.class)
                .hasFieldOrProperty("username")
                .hasFieldOrProperty("roles");

        assertEquals(expecteUserResponseDTO.getUsername(), actualResponse.getUsername());
        assertThat(actualResponse.getRoles())
                .hasSize(expecteUserResponseDTO.getRoles().size())
                .containsExactlyInAnyOrderElementsOf(expecteUserResponseDTO.getRoles());

        verify(userRepositoryPort, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debería devolver una excepción si existe el usuario")
    void register_shouldReturnException_whenUserExists() {
        String msgException = "User already exists";
        String username = "username";
        String password = "password";
        String encodedPassword = "encodedPassword";
        List<Role> roles = List.of(Role.USER, Role.ADMIN);
        HashSet<Role> hashSetRoles = new HashSet<>(roles);
        User user = new User(1L, username, encodedPassword, hashSetRoles);
        RegisterRequest request = new RegisterRequest(username, password, roles);

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserRegisteredException.class)
                .hasMessage(msgException);

        verify(userRepositoryPort, times(1)).findByUsername(username);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    // =========================================================================
    // TESTS PARA login(..)
    // =========================================================================

    @Test
    @DisplayName("Deberia devolver un token si existe el usuario")
    void login_shouldReturnToken_whenUserExists() {
        String username = "username";
        String password = "password";
        String encodedPassword = "encodedPassword";
        TokenResponse token = new TokenResponse("token");
        List<Role> roles = List.of(Role.USER, Role.ADMIN);
        HashSet<Role> hashSetRoles = new HashSet<>(roles);
        User user = new User(1L, username, encodedPassword, hashSetRoles);
        LoginRequest loginRequest = new LoginRequest(username, password);

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtProvider.createToken(any(UserDetails.class))).thenReturn(token);

        Optional<TokenResponse> actualResponse = authService.login(loginRequest);

        assertThat(actualResponse)
                .isNotNull()
                .isPresent()
                .hasValueSatisfying(tokenResponse -> {
                    assertThat(tokenResponse)
                            .hasFieldOrProperty("token");
                    assertEquals(token.token(), tokenResponse.token());
                });

        verify(userRepositoryPort, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtProvider, times(1)).createToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Debería devolver un token vacío si no existe el usuario")
    void login_shouldEmptyReturnToken_whenUserNoExists() {
        String username = "usuario_inexistente";
        LoginRequest loginRequest = new LoginRequest(username, "password");

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.empty());


        Optional<TokenResponse> actualResponse = authService.login(loginRequest);

        assertThat(actualResponse)
                .isNotNull()
                .isEmpty();

        verify(userRepositoryPort, times(1)).findByUsername(username);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtProvider, never()).createToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Debería devolver un Optional vacío si la contraseña es incorrecta")
    void login_shouldReturnEmptyOptional_whenPasswordIsIncorrect() {
        // --- Arrange ---
        String username = "usuario_existente";
        String wrongPassword = "password_incorrecta";
        String passwordHasheada = "password_correcta_hasheada";
        LoginRequest loginRequest = new LoginRequest(username, wrongPassword);

        User existingUser = User.builder()
                .username(username)
                .password(passwordHasheada)
                .roles(new HashSet<>(List.of(Role.USER)))
                .build();
        
        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(wrongPassword, passwordHasheada)).thenReturn(false);


        Optional<TokenResponse> actualResponse = authService.login(loginRequest);

        assertThat(actualResponse)
                .isNotNull()
                .isEmpty();

        verify(userRepositoryPort, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(wrongPassword, passwordHasheada);
        verify(jwtProvider, never()).createToken(any(UserDetails.class));
    }
}