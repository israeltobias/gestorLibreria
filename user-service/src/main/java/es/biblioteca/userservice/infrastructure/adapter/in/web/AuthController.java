package es.biblioteca.userservice.infrastructure.adapter.in.web;

import es.biblioteca.userservice.application.exceptions.UserRegisteredException;
import es.biblioteca.userservice.application.port.in.AuthUseCase;
import es.biblioteca.userservice.infrastructure.adapter.out.annotations.OperationLogin;
import es.biblioteca.userservice.infrastructure.adapter.out.annotations.OperationRegister;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import es.biblioteca.userservice.infrastructure.dto.LoginRequest;
import es.biblioteca.userservice.infrastructure.dto.RegisterRequest;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Módulo de gestión de autenticación de usuarios")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final ResponseEntityFactory apiResponse;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @OperationRegister
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest httpServletRequest) {
        try {
            UserResponseDTO userDTO = authUseCase.register(registerRequest);
            return apiResponse.genericResponse(HttpStatus.CREATED, userDTO, "Usuario creado correctamente.", httpServletRequest.getRequestURI());
        } catch (UserRegisteredException _){
            return apiResponse.error(HttpStatus.CONFLICT,httpServletRequest.getRequestURI());
        }
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @OperationLogin
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        return authUseCase.login(loginRequest)
                .map(token -> apiResponse.ok(token,"Token generado correctamente", httpServletRequest.getRequestURI()))
                .orElseGet(() -> apiResponse.error(HttpStatus.NOT_FOUND, httpServletRequest.getRequestURI()));
    }

}
