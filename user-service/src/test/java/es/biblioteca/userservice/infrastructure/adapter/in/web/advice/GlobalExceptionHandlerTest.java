package es.biblioteca.userservice.infrastructure.adapter.in.web.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import es.biblioteca.userservice.application.exceptions.UserRegisteredException;
import es.biblioteca.userservice.infrastructure.adapter.out.factory.ResponseEntityFactory;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler Tests")
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    ResponseEntityFactory apiResponse;
    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private WebRequest mockWebRequest;

    // =========================================================================
    // TESTS PARA handleUserFound(...)
    // =========================================================================

    @Test
    @DisplayName("Debería delegar la creación de la respuesta de error a la fábrica con los parámetros correctos")
    void handleUserFound_shouldDelegateToResponseEntityFactory_withCorrectParameters() {
        // --- Arrange (Preparar) ---
        String expectedPath = "/auth/register";
        UserRegisteredException exception = new UserRegisteredException("El usuario ya existe.");


        when(mockRequest.getRequestURI()).thenReturn(expectedPath);
        ResponseEntity<Object> mockResponseEntity = ResponseEntity.status(HttpStatus.CONFLICT).build();
        when(apiResponse.error(HttpStatus.CONFLICT, expectedPath)).thenReturn(mockResponseEntity);

        ResponseEntity<Object> actualResponse = globalExceptionHandler.handleUserFound(exception, mockRequest);


        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        verify(apiResponse, times(1)).error(any(), anyString());
        verify(mockRequest, times(1)).getRequestURI();
    }

// =========================================================================
    // TESTS PARA handleHttpMessageNotReadable(...)
    // =========================================================================

    @Test
    @DisplayName("handleHttpMessageNotReadable debería devolver un mensaje genérico para errores de JSON simples")
    void handleHttpMessageNotReadable_shouldReturnGenericMessage_forGenericJsonError() {

        String expectedPath = "/some/path";
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("JSON parse error", mock(HttpInputMessage.class));

        when(mockWebRequest.getAttribute(anyString(), anyInt())).thenReturn(expectedPath);

        ResponseEntity<Object> response = globalExceptionHandler.handleHttpMessageNotReadable(
                exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, mockWebRequest
        );

        Assertions.assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        ErrorResponse body = (ErrorResponse) response.getBody();
        Assertions.assertNotNull(body);
        assertThat(body.code()).isEqualTo("INVALID_JSON_FORMAT");
        assertThat(body.message()).isEqualTo("La petición JSON está mal formada.");
        assertThat(body.path()).isEqualTo(expectedPath);
    }

    @Test
    @DisplayName("handleHttpMessageNotReadable debería devolver un mensaje específico para errores de Enum inválidos")
    void handleHttpMessageNotReadable_shouldReturnSpecificMessage_forInvalidEnumError() {
        // --- Arrange ---
        String expectedPath = "/auth/register";

        // 1. Simulamos la excepción InvalidFormatException que Jackson lanzaría
        String jacksonErrorMessage = "Cannot deserialize value of type `...Role` from String \"GUEST\": not one of the values accepted for Enum class: [ADMIN, USER]";
        InvalidFormatException cause = mock(InvalidFormatException.class);
        when(cause.getMessage()).thenReturn(jacksonErrorMessage);

        // 2. Envolvemos la causa en la excepción principal
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("JSON parse error", cause, mock(HttpInputMessage.class));

        // Configuramos los mocks
        when(mockWebRequest.getAttribute(anyString(), anyInt())).thenReturn(expectedPath);
        // when(mockRequest.getRequestURI()).thenReturn(expectedPath);

        // --- Act ---
        ResponseEntity<Object> response = globalExceptionHandler.handleHttpMessageNotReadable(
                exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, mockWebRequest
        );

        // --- Assert ---
        Assertions.assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        ErrorResponse body = (ErrorResponse) response.getBody();
        Assertions.assertNotNull(body);
        assertThat(body.code()).isEqualTo("INVALID_JSON_FORMAT");
        // ¡LA ASERCIÓN CLAVE!
        assertThat(body.message()).isEqualTo("Valor de rol inválido. Los valores permitidos son: ADMIN,  USER.");
        assertThat(body.path()).isEqualTo(expectedPath);
    }


    @Test
    @DisplayName("getPathFromWebRequest debería devolver la URI del forward si existe")
    void getPathFromWebRequest_shouldReturnForwardUri_whenAttributeExists() {

        String expectedPath = "/original/path";
        when(mockWebRequest.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI, RequestAttributes.SCOPE_REQUEST))
                .thenReturn(expectedPath);

        String actualPath = globalExceptionHandler.getPathFromWebRequest(mockWebRequest);


        assertThat(actualPath).isEqualTo(expectedPath);

        verify(mockWebRequest, times(1)).getAttribute(anyString(), anyInt());
    }

    @Test
    @DisplayName("getPathFromWebRequest debería devolver la URI del request nativo si no hay forward")
    void getPathFromWebRequest_shouldReturnNativeRequestUri_whenNoForwardAttribute() {

        String expectedPath = "/some/other/path";

        when(mockWebRequest.getAttribute(any(), anyInt())).thenReturn(expectedPath);


        String actualPath = globalExceptionHandler.getPathFromWebRequest(mockWebRequest);


        assertThat(actualPath).isEqualTo(expectedPath);
        
        verify(mockWebRequest, times(1)).getAttribute(anyString(), anyInt());
    }

    @Test
    @DisplayName("getPathFromWebRequest debería devolver 'unknown' si no puede obtener la ruta")
    void getPathFromWebRequest_shouldReturnUnknown_whenPathCannotBeDetermined() {

        when(mockWebRequest.getAttribute(anyString(), anyInt())).thenReturn(null);

        String actualPath = globalExceptionHandler.getPathFromWebRequest(mockWebRequest);


        assertThat(actualPath).isEqualTo("unknown");

        verify(mockWebRequest, times(1)).getAttribute(anyString(), anyInt());
    }
}