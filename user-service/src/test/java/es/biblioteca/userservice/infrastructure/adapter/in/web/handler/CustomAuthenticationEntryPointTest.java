package es.biblioteca.userservice.infrastructure.adapter.in.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorCodesProperties;
import es.biblioteca.userservice.infrastructure.config.ConfigErrorMessagesProperties;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test para la clase CustomAuthenticationEntryPoint")
class CustomAuthenticationEntryPointTest {

    @Mock
    private ConfigErrorCodesProperties configErrorCodesProperties;
    @Mock
    private ConfigErrorMessagesProperties configErrorMessagesProperties;
    @Mock
    private HttpServletRequest mockRequest;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @InjectMocks
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    @DisplayName("Debería establecer el estado 401 y escribir un ErrorResponse JSON correcto")
    void commence_shouldSetUnauthorizedStatusAndWriteCorrectJsonResponse() throws IOException, ServletException {

        String expectedPath = "/api/v1/protected-resource";
        String expectedErrorCode = "UNAUTHORIZED";
        String expectedErrorMessage = "Se requiere autenticación.";

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        when(mockRequest.getRequestURI()).thenReturn(expectedPath);
        when(configErrorCodesProperties.getCode(HttpStatus.UNAUTHORIZED)).thenReturn(expectedErrorCode);
        when(configErrorMessagesProperties.getMessage(HttpStatus.UNAUTHORIZED)).thenReturn(expectedErrorMessage);

        AuthenticationException authException = new AuthenticationException("No autenticado") {
        };

        customAuthenticationEntryPoint.commence(mockRequest, mockResponse, authException);

        assertThat(mockResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(mockResponse.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        String responseBody = mockResponse.getContentAsString();
        ErrorResponse actualErrorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(actualErrorResponse.code()).isEqualTo(expectedErrorCode);
        assertThat(actualErrorResponse.message()).isEqualTo(expectedErrorMessage);
        assertThat(actualErrorResponse.path()).isEqualTo(expectedPath);
        assertThat(actualErrorResponse.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Debería usar el path del atributo FORWARD_REQUEST_URI si está presente")
    void commence_shouldUseForwardRequestUriPath_whenAttributeIsPresent() throws IOException, ServletException {

        String originalPath = "/original/path";
        String errorPath = "/error"; // La ruta a la que se hizo el forward

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        when(mockRequest.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI)).thenReturn(originalPath);
        when(mockRequest.getRequestURI()).thenReturn(errorPath); // El requestURI es el del forward

        customAuthenticationEntryPoint.commence(mockRequest, mockResponse, new AuthenticationException("") {
        });

        String responseBody = mockResponse.getContentAsString();
        ErrorResponse actualErrorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(actualErrorResponse.path()).isEqualTo(originalPath);
    }

    @Test
    @DisplayName("Debería usar el getRequestURI si el atributo FORWARD_REQUEST_URI no está presente")
    void commence_shouldUseRequestUri_whenForwardAttributeIsNotPresent() throws IOException, ServletException {

        String directPath = "/direct/path";

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        when(mockRequest.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI)).thenReturn(null); // No hay atributo
        when(mockRequest.getRequestURI()).thenReturn(directPath);

        customAuthenticationEntryPoint.commence(mockRequest, mockResponse, new AuthenticationException("") {
        });

        String responseBody = mockResponse.getContentAsString();
        ErrorResponse actualErrorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
        
        assertThat(actualErrorResponse.path()).isEqualTo(directPath);
    }

}