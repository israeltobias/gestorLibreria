package es.biblioteca.userservice.infrastructure.adapter.out.annotations;

import es.biblioteca.userservice.infrastructure.config.OpenApiExamples;
import es.biblioteca.userservice.infrastructure.dto.ErrorResponse;
import es.biblioteca.userservice.infrastructure.dto.LoginRequest;
import es.biblioteca.userservice.infrastructure.dto.SuccessApiResponse;
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
@Target({ElementType.METHOD, ElementType.TYPE})
@Operation(
        summary = "Autentica un usuario y devuelve un token.",
        description = "Endpoint público para iniciar sesión. Devuelve un accessToken y un refreshToken.",
        security = {}, // Excluye este endpoint de la seguridad global de JWT
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Credenciales del usuario.",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = LoginRequest.class),
                        examples = @ExampleObject(
                                name = "Ejemplo válido de login",
                                value = OpenApiExamples.VALID_OBJECT_LOGIN
                        )
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Autenticación exitosa. Devuelve un token JWT.",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                // El schema ayuda a documentar la estructura
                                schema = @Schema(implementation = SuccessApiResponse.class),
                                examples = @ExampleObject(
                                        name = "Respuesta de login exitoso",
                                        summary = "Ejemplo de token devuelto",
                                        value = OpenApiExamples.RESPONSE_200_LOGIN_SUCCESS
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
public @interface OperationLogin {
}
