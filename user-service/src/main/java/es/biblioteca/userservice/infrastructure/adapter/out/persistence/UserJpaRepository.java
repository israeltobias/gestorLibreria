package es.biblioteca.userservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);
}
