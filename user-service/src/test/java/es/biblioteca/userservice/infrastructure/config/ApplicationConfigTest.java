package es.biblioteca.userservice.infrastructure.config;

import es.biblioteca.userservice.domain.model.Role;
import es.biblioteca.userservice.domain.model.User;
import es.biblioteca.userservice.domain.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("ApplicationConfig Test")
@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    // No usamos @InjectMocks, crearemos la instancia manualmente
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        // Aseguramos que la instancia de ApplicationConfig usa nuestro mock
        applicationConfig = new ApplicationConfig(userRepositoryPort);
    }

    // =========================================================================
    // TESTS PARA userDetailsService(...)
    // =========================================================================

    @Test
    @DisplayName("userDetailsService debería devolver UserDetails cuando el usuario existe")
    void userDetailsService_shouldReturnUserDetails_whenUserExists() {

        String username = "testuser";
        User domainUser = User.builder()
                .username(username)
                .password("hashed_password")
                .roles(new HashSet<>(List.of(Role.USER)))
                .build();

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(domainUser));

        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);

        verify(userRepositoryPort, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("userDetailsService debería lanzar UsernameNotFoundException cuando el usuario no existe")
    void userDetailsService_shouldThrowException_whenUserDoesNotExist() {

        String username = "nonexistent_user";

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with username " + username + " not found");

        verify(userRepositoryPort, times(1)).findByUsername(username);
    }

    // =========================================================================
    // TESTS PARA passwordEncoder()
    // =========================================================================

    @Test
    @DisplayName("passwordEncoder debería devolver una instancia de BCryptPasswordEncoder")
    void passwordEncoder_shouldReturnBCryptPasswordEncoderInstance() {

        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        assertThat(passwordEncoder)
                .isNotNull()
                .isInstanceOf(BCryptPasswordEncoder.class);
    }

    // =========================================================================
    // TESTS PARA authenticationManager()
    // =========================================================================

    @Test
    @DisplayName("authenticationManager debería devolver el AuthenticationManager de la configuración")
    void authenticationManager_shouldReturnAuthenticationManagerFromConfig() throws Exception {

        AuthenticationConfiguration mockConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);

        when(mockConfig.getAuthenticationManager()).thenReturn(mockAuthManager);

        AuthenticationManager actualAuthManager = applicationConfig.authenticationManager(mockConfig);

        assertThat(actualAuthManager)
                .isNotNull()
                .isSameAs(mockAuthManager); // Verificamos que es la misma instancia

        verify(mockConfig, times(1)).getAuthenticationManager();
    }
}