package es.biblioteca.userservice.infrastructure.adapter.in.web;

import es.biblioteca.userservice.application.exceptions.UserRegisteredException;
import es.biblioteca.userservice.application.port.in.AuthUseCase;
import es.biblioteca.userservice.domain.model.Role;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import es.biblioteca.userservice.infrastructure.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test para la clase AuthController")
class AuthControllerTest {

    @Mock
    HttpServletRequest httpServletRequest;
    @InjectMocks
    AuthController authController;
    @Mock
    private AuthUseCase authUseCase;
    @Mock
    private ResponseEntityFactory apiResponse;

    // =========================================================================
    // TESTS PARA register(..)
    // =========================================================================

    @Test
    @DisplayName("Debería devolver 201 con datos correctos")
    void registerUser_shouldReturn201_withData() {
        String path = "/register";
        String username = "username";
        String password = "password";
        List<Role> roleList = List.of(Role.USER);
        String msgSuccess = "Usuario creado correctamente.";

        RegisterRequest registerRequest = new RegisterRequest(username, password, roleList);
        UserResponseDTO userResponseDTO = new UserResponseDTO(username, roleList);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessApiResponse<>(HttpStatus.CREATED.value(), msgSuccess, path, Instant.now(), userResponseDTO, null)
        );


        when(httpServletRequest.getRequestURI()).thenReturn(path);
        when(authUseCase.register(registerRequest)).thenReturn(userResponseDTO);
        when(apiResponse.genericResponse(HttpStatus.CREATED, userResponseDTO, msgSuccess, path)).thenReturn(expectedResponse);


        ResponseEntity<Object> actualResponse = authController.registerUser(registerRequest, httpServletRequest);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertInstanceOf(SuccessApiResponse.class, actualResponse.getBody());

        SuccessApiResponse<?> responseBody = (SuccessApiResponse<?>) actualResponse.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.path()).isEqualTo(path);
        assertEquals(HttpStatus.CREATED.value(), responseBody.status());
        assertThat(responseBody.data())
                .isInstanceOf(UserResponseDTO.class)
                .isNotNull()
                .isEqualTo(userResponseDTO)
                .hasFieldOrProperty("username")
                .hasFieldOrProperty("roles");

        verify(apiResponse, times(1)).genericResponse(any(), any(), anyString(), anyString());
        verify(authUseCase, times(1)).register(any());
        verify(httpServletRequest, times(1)).getRequestURI();

        verify(apiResponse, never()).error(any(), anyString());
    }

    @Test
    @DisplayName("Debería devolver 409 cuando el usuario existe")
    void registerUser_shouldReturn409_whenUserExists() {
        String path = "/register";
        String username = "username";
        String password = "password";
        List<Role> roleList = List.of(Role.USER);
        String msgError = "Los datos ya existen.";
        String msgCode = "DATA_FOUND";
        Instant now = Instant.now();

        RegisterRequest registerRequest = new RegisterRequest(username, password, roleList);
        ErrorResponse errorResponse = new ErrorResponse(msgCode, msgError, now, path);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);


        when(httpServletRequest.getRequestURI()).thenReturn(path);
        when(authUseCase.register(registerRequest)).thenThrow(new UserRegisteredException(msgError));
        when(apiResponse.error(HttpStatus.CONFLICT, path)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = authController.registerUser(registerRequest, httpServletRequest);

        assertThat(actualResponse)
                .isNotNull()
                .isInstanceOf(ResponseEntity.class)
                .isEqualTo(expectedResponse)
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("body");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        assertInstanceOf(ErrorResponse.class, actualResponse.getBody());


        ErrorResponse responseBody = (ErrorResponse) actualResponse.getBody();
        assertThat(responseBody)
                .isNotNull()
                .isEqualTo(errorResponse)
                .hasFieldOrProperty("path")
                .hasFieldOrProperty("code")
                .hasFieldOrProperty("message")
                .hasFieldOrProperty("timestamp");
        assertEquals(path, responseBody.path());
        assertEquals(msgCode, responseBody.code());
        assertEquals(msgError, responseBody.message());
        assertEquals(now, responseBody.timestamp());

        verify(authUseCase, times(1)).register(any());
        verify(apiResponse, times(1)).error(HttpStatus.CONFLICT, path);

        verify(apiResponse, never()).genericResponse(any(), any(), anyString(), anyString());
    }

    // =========================================================================
    // TESTS PARA login(..)
    // =========================================================================


    @Test
    @DisplayName("Debe devolver un token si existe el usuario")
    void login_shouldReturnToken_whenUserExists() {

        String path = "/login";
        String username = "username";
        String password = "password";
        String msgSuccess = "Token generado correctamente";
        String token = "token";
        TokenResponse tokenResponse = new TokenResponse(token);
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.OK).body(
                new SuccessApiResponse<>(HttpStatus.OK.value(), msgSuccess, path, Instant.now(), tokenResponse, null)
        );

        when(httpServletRequest.getRequestURI()).thenReturn(path);
        when(apiResponse.ok(tokenResponse, msgSuccess, path)).thenReturn(expectedResponse);
        when(authUseCase.login(loginRequest)).thenReturn(Optional.of(tokenResponse));

        ResponseEntity<Object> actualResponse = authController.loginUser(loginRequest, httpServletRequest);

        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse);
        assertInstanceOf(SuccessApiResponse.class, actualResponse.getBody());

        SuccessApiResponse<?> bodyResponse = (SuccessApiResponse<?>) actualResponse.getBody();

        assertNotNull(bodyResponse);
        assertEquals(HttpStatus.OK.value(), bodyResponse.status());
        assertEquals(msgSuccess, bodyResponse.message());
        assertEquals(path, bodyResponse.path());
        assertThat(bodyResponse.data())
                .isNotNull()
                .isInstanceOf(TokenResponse.class)
                .isEqualTo(tokenResponse).hasFieldOrProperty("token");

        TokenResponse dataTokenResponse = (TokenResponse) bodyResponse.data();
        assertNotNull(dataTokenResponse.token());
        assertEquals(token, dataTokenResponse.token());

        verify(httpServletRequest, times(1)).getRequestURI();
        verify(authUseCase, times(1)).login(any());
        verify(apiResponse, times(1)).ok(tokenResponse, msgSuccess, path);

        verify(apiResponse, never()).error(any(), anyString());
    }


    @Test
    @DisplayName("Debería devolver un error si no existe el usuario")
    void login_shouldReturnErrorResponse_whenUserNoExists() {
        String path = "/login";
        String username = "username";
        String password = "password";
        String msgError = "No se han encontrado datos";
        String msgCode = "DATA_NOT_FOUND";
        Instant now = Instant.now();
        LoginRequest loginRequest = new LoginRequest(username, password);
        ErrorResponse errorResponse = new ErrorResponse(msgCode, msgError, now, path);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);


        when(httpServletRequest.getRequestURI()).thenReturn(path);
        when(authUseCase.login(loginRequest)).thenReturn(Optional.empty());
        when(apiResponse.error(HttpStatus.NOT_FOUND, path)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = authController.loginUser(loginRequest, httpServletRequest);

        assertThat(actualResponse)
                .isNotNull()
                .isInstanceOf(ResponseEntity.class)
                .isEqualTo(expectedResponse)
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("body");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        assertInstanceOf(ErrorResponse.class, actualResponse.getBody());

        ErrorResponse responseBody = (ErrorResponse) actualResponse.getBody();

        assertThat(responseBody)
                .isNotNull()
                .isEqualTo(errorResponse)
                .hasFieldOrProperty("path")
                .hasFieldOrProperty("code")
                .hasFieldOrProperty("message")
                .hasFieldOrProperty("timestamp");
        assertEquals(path, responseBody.path());
        assertEquals(msgCode, responseBody.code());
        assertEquals(msgError, responseBody.message());
        assertEquals(now, responseBody.timestamp());

        verify(httpServletRequest, times(1)).getRequestURI();
        verify(authUseCase, times(1)).login(any());
        verify(apiResponse, times(1)).error(HttpStatus.NOT_FOUND, path);

        verify(apiResponse, never()).ok(any(TokenResponse.class), anyString(), anyString());

    }
}