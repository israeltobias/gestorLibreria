package es.biblioteca.userservice.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Servicio de Usuarios - Gestor de Librería",
                version = "v1.0.0", // Es una convención usar 'v' al principio
                description = "Esta API proporciona los endpoints para la gestión de usuarios, " +
                        "incluyendo la autenticación (login), registro y autorización basada en roles. " +
                        "Forma parte del sistema de microservicios del Gestor de Librería.",
                contact = @Contact(
                        name = "Israel Tobias",
                        email = "tu.email@ejemplo.com",
                        url = "https://github.com/israeltobias/gestorLibreria"
                ),

                license = @License(
                        name = "GNU GPL v3",
                        url = "https://www.gnu.org/licenses/gpl-3.0.html"
                ),
                termsOfService = "http://swagger.io/terms/"
        ),

        servers = {
                @Server(
                        description = "Entorno de Desarrollo Local",
                        url = "http://localhost:8081"
                )
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Token de acceso JWT. Para autorizar, introduce la palabra **'Bearer'** seguida de un espacio y tu token.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
