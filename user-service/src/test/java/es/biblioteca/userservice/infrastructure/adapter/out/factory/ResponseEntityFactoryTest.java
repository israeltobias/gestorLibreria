package es.biblioteca.userservice.infrastructure.adapter.out.factory;

import es.biblioteca.userservice.domain.model.Role;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorCodesProperties;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorMessagesProperties;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import es.biblioteca.userservice.infrastructure.dto.SuccessApiResponse;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ResponseEntityFactory test")
@ExtendWith(MockitoExtension.class)
class ResponseEntityFactoryTest {
    @Mock
    ConfigErrorCodesProperties configErrorCodesProperties;
    @Mock
    ConfigErrorMessagesProperties configErrorMessagesProperties;

    @InjectMocks
    ResponseEntityFactory responseEntityFactory;

    // =========================================================================
    // private methods
    // =========================================================================
    private static Stream<Arguments> ok_provideDifferentDataObjects() {
        String stringData = "Hello, World!";
        Map<String, Integer> mapData = Map.of("key1", 1, "key2", 2);
        List<String> listData = List.of("item1", "item2");
        UserResponseDTO dtoData = new UserResponseDTO("tester", List.of(Role.USER));

        return Stream.of(
                Arguments.of(null, "Dato nulo"),
                Arguments.of(stringData, "Dato de tipo String"),
                Arguments.of(mapData, "Dato de tipo Map"),
                Arguments.of(listData, "Dato de tipo List"),
                Arguments.of(dtoData, "Dato de tipo DTO personalizado")
        );
    }

    private static Stream<Arguments> genericResponse__provideDifferentDataObjects() {
        String stringData = "Hello, World!";
        Map<String, Integer> mapData = Map.of("key1", 1, "key2", 2);
        List<String> listData = List.of("item1", "item2");
        UserResponseDTO dtoData = new UserResponseDTO("tester", List.of(Role.USER));

        return Stream.of(
                Arguments.of(HttpStatus.BAD_GATEWAY, null, "Dato nulo"),
                Arguments.of(HttpStatus.ACCEPTED, stringData, "Dato de tipo String"),
                Arguments.of(HttpStatus.OK, mapData, "Dato de tipo Map"),
                Arguments.of(HttpStatus.NOT_FOUND, listData, "Dato de tipo List"),
                Arguments.of(HttpStatus.BAD_REQUEST, dtoData, "Dato de tipo DTO personalizado")
        );
    }

    private static Stream<Arguments> provideErrorStatusCodes() {
        return Stream.of(
                Arguments.of(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "El usuario no fue encontrado."),
                Arguments.of(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS", "El usuario ya existe."),
                Arguments.of(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Credenciales inválidas."),
                Arguments.of(HttpStatus.INTERNAL_SERVER_ERROR, "GENERIC_ERROR", "Ocurrió un error inesperado.")
        );
    }

    // =========================================================================
    // TESTS PARA ok()
    // =========================================================================

    @ParameterizedTest(name = "Debería construir una respuesta OK correcta con {1}")
    @MethodSource("ok_provideDifferentDataObjects")
    @DisplayName("Debe devolver una respuesta json ok correcta con diferentes tipos de datos")
    void ok_shouldReturnResponseEntityOK_withVariousDataTypes(Object data, @SuppressWarnings("unused") String testCaseName) {
        String msgSuccess = "Valid data";
        String uri = "uri";

        ResponseEntity<Object> actualResponse = responseEntityFactory.ok(data, msgSuccess, uri);

        assertThat(actualResponse).isNotNull().isInstanceOf(ResponseEntity.class)
                .hasFieldOrProperty("body")
                .hasFieldOrProperty("status");

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody())
                .isNotNull()
                .isInstanceOf(SuccessApiResponse.class)
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("message")
                .hasFieldOrProperty("path")
                .hasFieldOrProperty("time")
                .hasFieldOrProperty("data")
                .hasFieldOrProperty("pagination");

        SuccessApiResponse<?> actualBody = (SuccessApiResponse<?>) actualResponse.getBody();

        assertThat(actualBody.status()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualBody.path()).isEqualTo(uri);
        assertThat(actualBody.message()).isEqualTo(msgSuccess);
        assertThat(actualBody.time()).isNotNull().isInstanceOf(Instant.class).isBefore(Instant.now().plusSeconds(1));
        assertThat(actualBody.pagination()).isNull();
        assertThat(actualBody.data()).isEqualTo(data);

        verify(configErrorCodesProperties, never()).getCode(any());
        verify(configErrorCodesProperties, never()).getErrorCodes();
        verify(configErrorMessagesProperties, never()).getMessage(any());
        verify(configErrorMessagesProperties, never()).getErrorMessages();
    }

    // =========================================================================
    // TESTS PARA genericResponse()
    // =========================================================================
    @ParameterizedTest(name = "Debería construir una respuesta OK correcta con {1}")
    @MethodSource("genericResponse__provideDifferentDataObjects")
    @DisplayName("Debe devolver una respuesta json generic correcta con diferentes tipos de datos y estados")
    void genericResponse_shouldReturnResponseEntity_withVariousDataTypesAndStatus(HttpStatus status, Object data, @SuppressWarnings("unused") String testCaseName) {

        String msgSuccess = "Valid data";
        String uri = "uri";

        ResponseEntity<Object> actualResponse = responseEntityFactory.genericResponse(status, data, msgSuccess, uri);

        assertThat(actualResponse).isNotNull().isInstanceOf(ResponseEntity.class)
                .hasFieldOrProperty("body")
                .hasFieldOrProperty("status");
        assertThat(actualResponse.getStatusCode()).isEqualTo(status);

        assertThat(actualResponse.getBody())
                .isNotNull()
                .isInstanceOf(SuccessApiResponse.class)
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("message")
                .hasFieldOrProperty("path")
                .hasFieldOrProperty("time")
                .hasFieldOrProperty("data")
                .hasFieldOrProperty("pagination");

        SuccessApiResponse<?> actualBody = (SuccessApiResponse<?>) actualResponse.getBody();

        assertThat(actualBody.status()).isEqualTo(status.value());
        assertThat(actualBody.path()).isEqualTo(uri);
        assertThat(actualBody.message()).isEqualTo(msgSuccess);
        assertThat(actualBody.time()).isNotNull().isInstanceOf(Instant.class).isBefore(Instant.now().plusSeconds(1));
        assertThat(actualBody.pagination()).isNull();
        assertThat(actualBody.data()).isEqualTo(data);

        verify(configErrorCodesProperties, never()).getCode(any());
        verify(configErrorCodesProperties, never()).getErrorCodes();
        verify(configErrorMessagesProperties, never()).getMessage(any());
        verify(configErrorMessagesProperties, never()).getErrorMessages();

    }

    @ParameterizedTest(name = "Debería generar una respuesta de error correcta para el estado {0}")
    @MethodSource("provideErrorStatusCodes")
    @DisplayName("Debe devolver una respuesta de error correcta para diferentes códigos de estado")
    void error_shouldReturnCorrectErrorResponse_forDifferentStatusCodes(HttpStatus httpStatus, String expectedErrorCode, String expectedErrorMessage
    ) {
        String path = "/test/path";


        when(configErrorCodesProperties.getCode(httpStatus)).thenReturn(expectedErrorCode);
        when(configErrorMessagesProperties.getMessage(httpStatus)).thenReturn(expectedErrorMessage);


        ResponseEntity<Object> actualResponse = responseEntityFactory.error(httpStatus, path);

        assertThat(actualResponse).isNotNull()
                .isInstanceOf(ResponseEntity.class)
                .hasFieldOrProperty("body")
                .hasFieldOrProperty("status");
        assertThat(actualResponse.getStatusCode()).isEqualTo(httpStatus);


        assertThat(actualResponse.getBody())
                .isNotNull()
                .isInstanceOf(ErrorResponse.class)
                .hasFieldOrProperty("code")
                .hasFieldOrProperty("message")
                .hasFieldOrProperty("path")
                .hasFieldOrProperty("timestamp");
        
        ErrorResponse responseBody = (ErrorResponse) actualResponse.getBody();

        assertThat(responseBody.code()).isEqualTo(expectedErrorCode);
        assertThat(responseBody.message()).isEqualTo(expectedErrorMessage);
        assertThat(responseBody.path()).isEqualTo(path);
        assertThat(responseBody.timestamp()).isNotNull();

        verify(configErrorCodesProperties, times(1)).getCode(any());
        verify(configErrorCodesProperties, never()).getErrorCodes();
        verify(configErrorMessagesProperties, times(1)).getMessage(any());
        verify(configErrorMessagesProperties, never()).getErrorMessages();
    }
}