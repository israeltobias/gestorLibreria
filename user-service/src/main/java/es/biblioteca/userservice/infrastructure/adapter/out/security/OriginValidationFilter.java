package es.biblioteca.userservice.infrastructure.adapter.out.security;

import es.biblioteca.userservice.infrastructure.config.ConfigCorsProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component

public class OriginValidationFilter extends OncePerRequestFilter {

    private final ConfigCorsProperties corsProperties;

    public OriginValidationFilter(ConfigCorsProperties corsProperties) {
        super();
        this.corsProperties = corsProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        List<String> allowedOrigins = List.of(corsProperties.getAllowedOrigins());
        String origin = request.getHeader("Origin");

        if (origin == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (allowedOrigins.contains(origin)) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write("Acceso denegado desde un origen no autorizado.");
    }
}
