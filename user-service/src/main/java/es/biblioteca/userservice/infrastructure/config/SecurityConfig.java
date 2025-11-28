package es.biblioteca.userservice.infrastructure.config;

import es.biblioteca.userservice.infrastructure.adapter.in.web.handler.CustomAccessDeniedHandler;
import es.biblioteca.userservice.infrastructure.adapter.in.web.handler.CustomAuthenticationEntryPoint;
import es.biblioteca.userservice.infrastructure.adapter.out.security.JwtAuthenticationFilter;
import es.biblioteca.userservice.infrastructure.adapter.out.security.OriginValidationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ConfigCorsProperties corsProperties;
    private final OriginValidationFilter originValidationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions ->
                        exceptions.accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/auth/register/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // 1. Configurar la gestión de sesiones como STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 2. Especificamos el filtro de los métodos permitidos a orígenes desconocidos
                .addFilterBefore(originValidationFilter, CorsFilter.class)
                // 3. Especificar el proveedor de autenticación, no es necesario Spring ya carga automáticamente nuestro JWtProvider
                // 4. Añadir nuestro filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 1. Orígenes: Sé específico sobre qué front-ends pueden conectarse
        configuration.setAllowedOrigins(List.of(corsProperties.getAllowedOrigins()));
        // 2. Métodos: Permite todos los métodos comunes, incluyendo el pre-vuelo OPTIONS
        configuration.setAllowedMethods(List.of(corsProperties.getAllowedMethods()));
        // 3. Cabeceras: Permite todas las cabeceras en la petición
        configuration.setAllowedHeaders(List.of("*"));
        // 4. Credenciales: Permite el envío de cookies y cabeceras de autenticación
        configuration.setAllowCredentials(true);
        // 5. Cabeceras Expuestas: Permite que el front-end lea estas cabeceras de la respuesta
        configuration.setExposedHeaders(List.of(corsProperties.getExposedHeaders()));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}