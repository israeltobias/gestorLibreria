package es.biblioteca.userservice.infrastructure.config;


import es.biblioteca.userservice.application.port.in.AuthUseCase;
import es.biblioteca.userservice.domain.model.Role;
import es.biblioteca.userservice.infrastructure.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final AuthUseCase authUseCase;

    @Override
    public void run(String... args) throws Exception {
        authUseCase.register(new RegisterRequest("juan", "juanpass", List.of(Role.USER)));
        authUseCase.register(new RegisterRequest("maria", "mariapass", List.of(Role.USER, Role.ADMIN)));
        authUseCase.register(new RegisterRequest("pepe", "pepepass", List.of(Role.USER)));
    }
}
