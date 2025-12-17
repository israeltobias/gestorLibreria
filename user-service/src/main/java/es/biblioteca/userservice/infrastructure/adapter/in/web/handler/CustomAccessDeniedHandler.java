package es.biblioteca.userservice.infrastructure.adapter.in.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorCodesProperties;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorMessagesProperties;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final ConfigErrorCodesProperties configErrorCodesProperties;
    private final ConfigErrorMessagesProperties configErrorMessagesProperties;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ErrorResponse errorResponse = new ErrorResponse(
                configErrorCodesProperties.getCode(HttpStatus.FORBIDDEN),
                configErrorMessagesProperties.getMessage(HttpStatus.FORBIDDEN),
                Instant.now(),
                request.getServletPath()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
