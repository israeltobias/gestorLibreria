package es.biblioteca.userservice.infrastructure.adapter.in.web;

import es.biblioteca.userservice.application.port.in.UserUseCase;
import es.biblioteca.userservice.domain.model.Role;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import es.biblioteca.userservice.infrastructure.dto.SuccessApiResponse;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
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
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test para la clase UseController")
class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private ResponseEntityFactory apiResponse;
    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private UserController userController;

    UserResponseDTO userResponseDTO1;
    List<UserResponseDTO> userResponseDTOList1;


    @BeforeEach
    void setUp() {
        userResponseDTO1 = new UserResponseDTO("username", List.of(Role.USER, Role.ADMIN));
        userResponseDTOList1 = List.of(userResponseDTO1);
    }

    // =========================================================================
    // TESTS PARA getUsers()
    // =========================================================================

    @Test
    @DisplayName("Debería devolver 200 OK con una lista de usuarios cuando hay usuarios")
    void getUsers_ShouldReturn200_WithUserList() {
        String uri = "/user";
        String msgSuccess = "Datos obtenidos correctamente";

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(
                new SuccessApiResponse<>(200, msgSuccess, uri, Instant.now(), userResponseDTOList1, null)
        );

        when(userUseCase.getUsers()).thenReturn(userResponseDTOList1);
        when(mockRequest.getRequestURI()).thenReturn(uri);
        when(apiResponse.ok(userResponseDTOList1, msgSuccess, uri)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userController.getUsers(mockRequest);

        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());


        assertInstanceOf(SuccessApiResponse.class, actualResponse.getBody());

        SuccessApiResponse<?> responseBody = (SuccessApiResponse<?>) actualResponse.getBody();
        assertEquals(uri, responseBody.path());
        assertEquals(msgSuccess,responseBody.message());
        assertEquals(userResponseDTOList1, responseBody.data());
        assertThat(responseBody.data()).isInstanceOf(List.class)
                .asInstanceOf(LIST)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(userResponseDTOList1);
        assertThat((List<?>) responseBody.data()).hasSize(userResponseDTOList1.size()); // 2. Compara su tamaño

        verify(userUseCase, times(1)).getUsers();
        verify(apiResponse, times(1)).ok(eq(userResponseDTOList1), eq(msgSuccess), anyString());
    }

    @Test
    @DisplayName("Debería devolver 200 OK con una lista de usuarios cuando NO hay usuarios")
    void getUsers_ShouldReturn200_WithEmptyList(){
        String uri = "/user";
        String msgSuccess = "Datos obtenidos correctamente";

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(
                new SuccessApiResponse<>(200, msgSuccess, uri, Instant.now(), List.of(), null)
        );

        when(userUseCase.getUsers()).thenReturn(List.of());
        when(mockRequest.getRequestURI()).thenReturn(uri);
        when(apiResponse.ok(List.of(), msgSuccess, uri)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userController.getUsers(mockRequest);

        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertInstanceOf(SuccessApiResponse.class, actualResponse.getBody());
        SuccessApiResponse<?> responseBody = (SuccessApiResponse<?>) actualResponse.getBody();
        assertEquals(uri, responseBody.path());
        assertEquals(msgSuccess, responseBody.message());
        assertEquals(HttpStatus.OK.value(),responseBody.status());
        assertThat(responseBody.data()).asInstanceOf(LIST).isEmpty();

        verify(userUseCase, times(1)).getUsers();
        verify(apiResponse, times(1)).ok(eq(List.of()), eq(msgSuccess), anyString());

    }

    // =========================================================================
    // TESTS PARA getUserByUsername()
    // =========================================================================

    @Test
    @DisplayName("Debería devolver 200 si existe el usuario")
    void getUser_ShouldReturn200_WithEUserExist(){

        String path = "/user/username";
        String msgSuccess = "Usuario obtenido correctamente";
        String username = "username";
        Optional<UserResponseDTO> optionalUserResponseDTO = Optional.of(userResponseDTO1);

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(
                new SuccessApiResponse<>(200, msgSuccess, path, Instant.now(), userResponseDTO1, null)
        );

        when(userUseCase.getUser(anyString())).thenReturn(optionalUserResponseDTO);
        when(mockRequest.getRequestURI()).thenReturn(path);
        when(apiResponse.ok(userResponseDTO1,msgSuccess,path)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userController.getUserByUsername(username, mockRequest);

        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertInstanceOf(SuccessApiResponse.class, actualResponse.getBody());

        SuccessApiResponse<?>  responseBody = (SuccessApiResponse<?>) actualResponse.getBody();
        assertEquals(HttpStatus.OK.value(),responseBody.status());
        assertEquals(msgSuccess,responseBody.message());
        assertEquals(path, responseBody.path());
        assertThat(responseBody.data())
                .isInstanceOf(UserResponseDTO.class)
                .isEqualTo(userResponseDTO1);

        verify(userUseCase, times(1)).getUser(username);
        verify(apiResponse, times(1)).ok(eq(userResponseDTO1), eq(msgSuccess), anyString());
        verify(apiResponse,never()).error(any(),anyString());
    }

    @Test
    @DisplayName("Debería devolver 404 si no existe el usuario")
    void getUser_ShouldReturn404_WithEUserNoExist(){
        String path = "/user/username";
        String msgError = "No se han encontrado datos";
        String username = "username";

        ResponseEntity<Object> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.name(), msgError, Instant.now(), path));

        when(userUseCase.getUser(username)).thenReturn(Optional.empty());
        when(mockRequest.getRequestURI()).thenReturn(path);
        when(apiResponse.error(HttpStatus.NOT_FOUND,path)).thenReturn(errorResponse);

        ResponseEntity<Object> actualResponse = userController.getUserByUsername(username, mockRequest);

        assertThat(actualResponse).isNotNull();
        assertEquals(HttpStatus.NOT_FOUND,actualResponse.getStatusCode());
        assertInstanceOf(ErrorResponse.class, actualResponse.getBody());

        ErrorResponse responseBody = (ErrorResponse) actualResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.NOT_FOUND.name(), responseBody.code());
        assertEquals(path, responseBody.path());

        verify(userUseCase, times(1)).getUser(anyString());
        verify(apiResponse,never()).ok(any(UserResponseDTO.class),anyString(),anyString());
        verify(apiResponse, times(1)).error(any(),anyString());
    }
}