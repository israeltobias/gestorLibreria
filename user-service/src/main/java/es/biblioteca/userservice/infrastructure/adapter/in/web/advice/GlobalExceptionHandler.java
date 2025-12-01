package es.biblioteca.userservice.infrastructure.adapter.in.web.advice;

import es.biblioteca.userservice.application.exceptions.UserRegisteredException;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ResponseEntityFactory apiResponse;

    @ExceptionHandler(value = UserRegisteredException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleUserFound(UserRegisteredException userRegisteredException, HttpServletRequest request) {
        return apiResponse.error(HttpStatus.CONFLICT, request.getRequestURI());
    }


}
