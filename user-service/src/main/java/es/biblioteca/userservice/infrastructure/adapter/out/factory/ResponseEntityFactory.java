package es.biblioteca.userservice.infrastructure.adapter.out.factory;

import es.biblioteca.userservice.infrastructure.config.ConfigErrorCodesProperties;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorMessagesProperties;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import es.biblioteca.userservice.infrastructure.dto.PaginationInfo;
import es.biblioteca.userservice.infrastructure.dto.SuccessApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class ResponseEntityFactory {

    private final ConfigErrorCodesProperties configErrorCodesProperties;
    private final ConfigErrorMessagesProperties configErrorMessagesProperties;

    // ========================================
    // MÉTODOS PÚBLICOS ESTÁTICOS
    // ========================================

    public <T> ResponseEntity<Object> ok(T data, String message, String path) {
        SuccessApiResponse<T> body = new SuccessApiResponse<>(
                HttpStatus.OK.value(), message, path, Instant.now(), data, null);
        return ResponseEntity.ok(body);
    }

    public <T> ResponseEntity<Object> genericResponse(HttpStatus status, T data, String message, String path) {
        SuccessApiResponse<T> body = new SuccessApiResponse<>(
                status.value(), message, path, Instant.now(), data, null);
        return ResponseEntity.status(status).body(body);
    }


    public <T> ResponseEntity<Object> ok(Page<T> page, String path) {
        PaginationInfo pagination = new PaginationInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        SuccessApiResponse<List<T>> body = new SuccessApiResponse<>(
                HttpStatus.OK.value(),
                "Datos obtenidos correctamente",
                path,
                Instant.now(),
                page.getContent(), // El 'data' son los contenidos de la página
                pagination       // La información de paginación va en su campo
        );
        return ResponseEntity.ok(body);
    }

    /**
     * Respuesta de error general
     */
    public ResponseEntity<Object> error(HttpStatus status, String path) {
        ErrorResponse errorResponse = new ErrorResponse(
                configErrorCodesProperties.getCode(status),
                configErrorMessagesProperties.getMessage(status),
                Instant.now(),
                path
        );
        return ResponseEntity.status(status).body(errorResponse);
    }


}
