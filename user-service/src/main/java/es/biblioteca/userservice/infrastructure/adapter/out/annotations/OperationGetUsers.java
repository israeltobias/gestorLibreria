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
        summary = "Listado de usuarios existentes",
        description = "Devuelve un listado de usuarios existentes en la base de datos",
        // También es buena práctica documentar las posibles respuestas
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Listado de usuarios",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = @ExampleObject(
                                        name = "Listado de usuarios",
                                        summary = "Ejemplo de listado de usuarios",
                                        value = OpenApiExamples.RESPONSE_200_USERS
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
                )
        }
)
public @interface OperationGetUsers {
}
