package es.biblioteca.libraryservice.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Author {
    private Long id;
    private String name;
    private String surname;


    @Builder
    public Author(Long id, String name, String surname) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del autor no puede ser nulo o vacío.");
        }

        this.id = id;
        this.name = name;
        this.surname = surname;
    }
}
