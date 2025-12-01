package es.biblioteca.userservice.infrastructure.adapter.out.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Parameter(
        name = "username",
        in = ParameterIn.PATH,
        description = "Nombre de usuario único del usuario a buscar.",
        required = true,
        example = "juan",
        schema = @Schema(type = "string")
)
public @interface ParameterGetUser {
}
