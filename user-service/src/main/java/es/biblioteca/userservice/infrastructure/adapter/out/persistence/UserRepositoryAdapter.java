package es.biblioteca.userservice.infrastructure.adapter.out.persistence;

import es.biblioteca.userservice.domain.model.User;
import es.biblioteca.userservice.domain.repository.UserRepositoryPort;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        // Si la entidad es nueva (lo que para nosotros significa id=0),
        // la preparamos para Hibernate forzando el ID a null. Hibernate requiere que sea null paranuevos objetos
        if (userEntity.isNew()) {
            userEntity.setId(null);
        }
        return UserMapper.toDomain(userJpaRepository.save(userEntity));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public List<UserResponseDTO> getUsers() {
        return UserMapper.toDTO(userJpaRepository.findAll());
    }

}
