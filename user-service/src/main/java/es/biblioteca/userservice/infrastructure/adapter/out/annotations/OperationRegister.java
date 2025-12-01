package es.biblioteca.userservice.infrastructure.adapter.out.annotations;

import es.biblioteca.userservice.infrastructure.config.OpenApiExamples;
import es.biblioteca.userservice.infrastructure.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Operation(
        summary = "Registra un nuevo usuario.",
        description = "Crea un nuevo usuario en el sistema. Este endpoint está restringido a administradores.",
        // La información del RequestBody ahora vive aquí
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del usuario para el registro.",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = RegisterRequest.class),
                        examples = @ExampleObject(
                                name = "Ejemplo válido de registro",
                                value = OpenApiExamples.VALID_OBJECT_REGISTER
                        )
                )
        ),
        // También es buena práctica documentar las posibles respuestas
        responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Usuario creado exitosamente",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = @ExampleObject(
                                        name = "Respuesta de usuario creado",
                                        summary = "Ejemplo de usuario creado",
                                        value = OpenApiExamples.RESPONSE_201_USER_CREATED
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Datos de entrada inválidos",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = @ExampleObject(
                                        name = "Respuesta de error de datos",
                                        summary = "Ejemplo de error de datos de entrada",
                                        value = OpenApiExamples.RESPONSE_400_BAD_DATA
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Acceso denegado (no es administrador)",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = @ExampleObject(
                                        name = "Respuesta de error de autenticación",
                                        summary = "Ejemplo de error de autenticación",
                                        value = OpenApiExamples.RESPONSE_403_NOT_ADMINISTRATOR
                                )
                        )
                )
        }
)
public @interface OperationRegister {
}
