package es.biblioteca.libraryservice.infrastructure.dto;

import java.util.List;

public record AuthorResponseDto(String name, String surname, List<BookRefDto> books) {
}
