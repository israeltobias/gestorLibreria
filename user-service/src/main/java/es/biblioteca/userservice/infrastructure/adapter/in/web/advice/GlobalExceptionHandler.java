package es.biblioteca.userservice.infrastructure.adapter.in.web.advice;

import es.biblioteca.userservice.application.exceptions.UserRegisteredException;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ResponseEntityFactory apiResponse;


    @ExceptionHandler(value = UserRegisteredException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleUserFound(UserRegisteredException userRegisteredException, HttpServletRequest request) {
        return apiResponse.error(HttpStatus.CONFLICT, request.getRequestURI());
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        String detailedMessage = "La petición JSON está mal formada.";

        // --- Lógica avanzada para un mensaje de error más útil ---
        Throwable cause = ex.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            // Expresión regular para extraer el valor y los valores permitidos del mensaje de error de Jackson
            Pattern pattern = Pattern.compile("accepted for Enum class: \\[(.*?)]");

            Matcher matcher = pattern.matcher(cause.getMessage());
            if (matcher.find()) {
                // El resto de tu lógica ya es correcta
                detailedMessage = String.format("Valor de rol inválido. Los valores permitidos son: %s.", matcher.group(1).replace(",", ", "));
            } else {
                // Es buena práctica tener un fallback por si el mensaje de Jackson cambia en el futuro
                detailedMessage = "Se ha proporcionado un valor inválido para un campo de tipo enumerado (enum).";
            }
        }
        String path = getPathFromWebRequest(request);
        if (path == null) {
            path = "unknown";
        }
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_JSON_FORMAT", // O un código de error que definas
                detailedMessage,
                Instant.now(),
                path
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    protected String getPathFromWebRequest(WebRequest webRequest) {
        try {
            Object forwardUri = webRequest.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI, RequestAttributes.SCOPE_REQUEST);
            
            return Optional.ofNullable(forwardUri)
                    .map(Object::toString)
                    .orElseGet(() -> { // Si no hay forward, ejecutamos el código de respaldo
                        if (webRequest instanceof ServletWebRequest servletWebRequest) {
                            return servletWebRequest.getRequest().getRequestURI();
                        }
                        return null; // Devuelve null si tampoco es un ServletWebRequest
                    });

        } catch (Exception _) {
            // En caso de cualquier error inesperado, devolvemos null
            return null;
        }
    }
}
