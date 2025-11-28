package es.biblioteca.userservice.infrastructure.config;

import es.biblioteca.userservice.domain.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Bean para el UserDetailsService.
     * Define cómo obtener los detalles de un usuario a partir de su nombre de usuario.
     * Spring Security usará este bean automáticamente para cargar datos de usuario.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Usamos una expresión lambda para una implementación concisa.
        // Se conecta directamente a tu capa de persistencia (puerto del repositorio).
        return username -> userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    /**
     * Bean para el PasswordEncoder.
     * Define el algoritmo de hash para las contraseñas (BCrypt es el estándar).
     * Este bean se inyectará en tu UserServiceImpl para codificar contraseñas al registrarse.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Bean para el AuthenticationManager.
     * Es el "gerente" que orquesta el proceso de autenticación.
     * Spring Boot 3 lo expone a través de AuthenticationConfiguration.
     * Lo definimos como bean para poder inyectarlo en nuestro UserServiceImpl (si fuera necesario para el login).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
