package es.biblioteca.userservice.application.exceptions;

/**
 * Excepción lanzada cuando un usuario ya existe en el sistema.
 * Usada en {@link es.biblioteca.userservice.infrastructure.adapter.in.web.advice.GlobalExceptionHandler} para devolver 409 Conflict.
 */

public class UserRegisteredException extends RuntimeException {

    public UserRegisteredException(String msg) {
        super(msg);
    }

}
