package es.biblioteca.userservice.domain.repository;

import es.biblioteca.userservice.domain.model.User;
import es.biblioteca.userservice.infrastructure.adapter.out.security.SecurityUser;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByUsername(String username);

    List<UserResponseDTO> getUsers();
}
