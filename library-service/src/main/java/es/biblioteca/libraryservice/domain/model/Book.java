package es.biblioteca.libraryservice.domain.model;

import es.biblioteca.libraryservice.infrastructure.adapter.out.persistence.AuthorEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class Book {
    private Long id;
    private String name;
    private String isbn;
    private Genre genre;
    private int numPages;
    private AuthorEntity author;


    @Builder
    public Book(Long id, String name, String isbn, Genre genre, int numPages, AuthorEntity author) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del libro no puede ser nulo o vacío.");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El ISBN del libro no puede ser nulo o vacío.");
        }
        if (numPages < 0) {
            throw new IllegalArgumentException("El número de páginas no puede ser negativo.");
        }

        this.author = Objects.requireNonNull(author, "El autor del libro no puede ser nulo.");

        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
        this.numPages = numPages;

    }
}
