package es.biblioteca.userservice.infrastructure.adapter.in.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorCodesProperties;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorMessagesProperties;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test para la clase CustomAccessDeniedHandler")
class CustomAccessDeniedHandlerTest {

    @Mock
    private ConfigErrorCodesProperties configErrorCodesProperties;
    @Mock
    private ConfigErrorMessagesProperties configErrorMessagesProperties;
    @Mock
    private HttpServletRequest mockRequest;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @InjectMocks
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Test
    @DisplayName("Debería establecer el estado 403 y escribir un ErrorResponse JSON correcto")
    void handle_shouldSetForbiddenStatusAndWriteCorrectJsonResponse() throws IOException, ServletException {

        String expectedPath = "/protected/resource";
        String expectedErrorCode = "ACCESS_DENIED";
        String expectedErrorMessage = "No tienes permisos.";

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        when(mockRequest.getServletPath()).thenReturn(expectedPath);
        when(configErrorCodesProperties.getCode(HttpStatus.FORBIDDEN)).thenReturn(expectedErrorCode);
        when(configErrorMessagesProperties.getMessage(HttpStatus.FORBIDDEN)).thenReturn(expectedErrorMessage);

        AccessDeniedException exception = new AccessDeniedException("Acceso denegado");

        customAccessDeniedHandler.handle(mockRequest, mockResponse, exception);

        assertThat(mockResponse.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(mockResponse.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        String responseBody = mockResponse.getContentAsString();

        ErrorResponse actualErrorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(actualErrorResponse.code()).isEqualTo(expectedErrorCode);
        assertThat(actualErrorResponse.message()).isEqualTo(expectedErrorMessage);
        assertThat(actualErrorResponse.path()).isEqualTo(expectedPath);
        assertThat(actualErrorResponse.timestamp()).isNotNull();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(objectMapper, times(1)).writeValueAsString(any(ErrorResponse.class));
    }
}