package es.biblioteca.userservice.application.port.in;

import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserUseCase {
    List<UserResponseDTO> getUsers();

    Optional<UserResponseDTO> getUser(String username);

}
