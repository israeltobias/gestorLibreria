package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByName(String name);

    Optional<BookEntity> findByIsbn(String isbn);
}
