package es.biblioteca.libraryservice.domain.repository;

import es.biblioteca.libraryservice.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {

    Book save(Book book);

    Optional<Book> findeByName(String name);

    Optional<Book> findByIsbn(String isbn);

    List<Book> getAll();
}
