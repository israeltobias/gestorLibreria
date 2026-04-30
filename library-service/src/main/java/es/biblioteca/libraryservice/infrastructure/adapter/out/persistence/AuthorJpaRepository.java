package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorJpaRepository extends JpaRepository<AuthorEntity, Long> {
    @Query("SELECT authorEntity FROM AuthorEntity authorEntity WHERE authorEntity.name = ?1 and authorEntity.surname = ?2")
    Optional<Author> findByNameAndSurname(String name, String surname);
}
