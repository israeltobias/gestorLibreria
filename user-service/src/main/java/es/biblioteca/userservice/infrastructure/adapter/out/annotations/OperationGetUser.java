package es.biblioteca.userservice.infrastructure.adapter.out.annotations;

import es.biblioteca.userservice.infrastructure.config.OpenApiExamples;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
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
        summary = "Obtención de un usuario",
        description = "Devuelve un usuario existente en el sistema",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Obtención de un usuario",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = @ExampleObject(
                                        name = "Obtención de un usuario",
                                        summary = "Ejemplo de obtención de un usuario",
                                        value = OpenApiExamples.RESPONSE_200_USER
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Respuesta de usuario no autorizado",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class),
                                examples = @ExampleObject(
                                        name = "No autorizado",
                                        summary = "Acceso no autorizado al recurso",
                                        value = OpenApiExamples.RESPONSE_401_UNAUTHORIZED
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado o credenciales incorrectas.",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class),
                                examples = @ExampleObject(
                                        name = "Respuesta de error 404",
                                        summary = "Ejemplo de usuario no encontrado",
                                        value = OpenApiExamples.RESPONSE_404_NOT_FOUND
                                )
                        )
                )
        }
)
public @interface OperationGetUser {
}
