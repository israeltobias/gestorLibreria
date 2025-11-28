package es.biblioteca.userservice.infrastructure.adapter.in.web;

import es.biblioteca.userservice.application.port.in.AuthUseCase;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import es.biblioteca.userservice.infrastructure.dto.AuthRequest;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
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
public class AuthController {

    private final AuthUseCase authUseCase;
    private final ResponseEntityFactory apiResponse;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@RequestBody AuthRequest authRequest, HttpServletRequest httpServletRequest) {
        UserResponseDTO userDTO = authUseCase.register(authRequest);
        //return apiResponse.ok(HttpStatus.CREATED,"Datos actualizados correctamente",httpServletRequest.getRequestURI());
        return apiResponse.genericResponse(HttpStatus.CREATED, userDTO, "Usuario creado correctamente.", httpServletRequest.getRequestURI());
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loginUser(@RequestBody AuthRequest authRequest, HttpServletRequest httpServletRequest) {
        return authUseCase.login(authRequest)
                .map(token -> apiResponse.ok(token,"Token generado correctamente", httpServletRequest.getRequestURI()))
                .orElseGet(() -> apiResponse.error(HttpStatus.NOT_FOUND, httpServletRequest.getRequestURI()));
    }

}
