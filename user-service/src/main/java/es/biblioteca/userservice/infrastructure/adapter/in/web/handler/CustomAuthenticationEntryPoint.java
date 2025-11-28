package es.biblioteca.userservice.infrastructure.adapter.in.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorCodesProperties;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorMessagesProperties;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final ConfigErrorCodesProperties configErrorCodesProperties;
    private final ConfigErrorMessagesProperties configErrorMessagesProperties;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String path = Optional.ofNullable(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI))
                .map(Object::toString).orElse(request.getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse(
                configErrorCodesProperties.getCode(HttpStatus.UNAUTHORIZED),
                configErrorMessagesProperties.getMessage(HttpStatus.UNAUTHORIZED),
                Instant.now(),
                path
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
