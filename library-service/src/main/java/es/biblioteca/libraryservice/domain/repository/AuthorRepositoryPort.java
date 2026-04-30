package es.biblioteca.libraryservice.domain.repository;

import es.biblioteca.libraryservice.domain.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryPort {

    Author save(Author author);

    Optional<Author> findByNameAndSurname(String name, String surname);

    List<Author> getAll();
}
