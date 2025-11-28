package es.biblioteca.userservice.application.service;

import es.biblioteca.userservice.application.port.in.UserUseCase;
import es.biblioteca.userservice.domain.repository.UserRepositoryPort;
import es.biblioteca.userservice.infrastructure.adapter.out.persistence.UserMapper;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;


    @Override
    public List<UserResponseDTO> getUsers() {
        return userRepositoryPort.getUsers();
    }

    @Override
    public Optional<UserResponseDTO> getUser(String username) {
        return userRepositoryPort.findByUsername(username)
                .map(UserMapper::toEntity)
                .map(UserMapper::toDTO);
    }

}