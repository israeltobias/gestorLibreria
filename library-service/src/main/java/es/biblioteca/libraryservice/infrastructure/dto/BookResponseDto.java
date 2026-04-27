package es.biblioteca.libraryservice.infrastructure.dto;

import es.biblioteca.libraryservice.domain.model.Genre;

public record BookResponseDto(String name, String isbn, Genre genre, int numPages, AuthorRefDto author) {
}
