package es.biblioteca.userservice.infrastructure.adapter.in.web;


import es.biblioteca.userservice.application.port.in.UserUseCase;
import es.biblioteca.userservice.infrastructure.adapter.out.annotations.OperationGetUser;
import es.biblioteca.userservice.infrastructure.adapter.out.annotations.OperationGetUsers;
import es.biblioteca.userservice.infrastructure.adapter.out.annotations.ParameterGetUser;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;
    private final ResponseEntityFactory apiResponse;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @OperationGetUsers
    public ResponseEntity<Object> getUsers(HttpServletRequest httpServletRequest) {
        return apiResponse.ok(userUseCase.getUsers(), "Datos obtenidos correctamente", httpServletRequest.getRequestURI());
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @OperationGetUser
    public ResponseEntity<Object> getUserByUsername(
            @ParameterGetUser
            @PathVariable(value = "username")  String username, HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI();
        return userUseCase.getUser(username)
                .map(u -> apiResponse.ok(u, "Usuario obtenido correctamente", uri))
                .orElseGet(() ->apiResponse.error(HttpStatus.NOT_FOUND, uri));
    }
}
