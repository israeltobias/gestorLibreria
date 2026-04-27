package es.biblioteca.libraryservice.infrastructure.dto;

import es.biblioteca.libraryservice.domain.model.Genre;

public record BookRefDto(String name, String isbn, Genre genre) {
}
