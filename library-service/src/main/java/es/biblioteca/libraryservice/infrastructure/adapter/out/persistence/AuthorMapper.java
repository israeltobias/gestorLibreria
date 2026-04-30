package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Author;
import es.biblioteca.libraryservice.infrastructure.dto.AuthorRefDto;
import es.biblioteca.libraryservice.infrastructure.dto.AuthorResponseDto;

import java.util.List;

public class AuthorMapper {

    private AuthorMapper() {
        super();
    }

    public static Author toDomain(AuthorEntity authorEntity) {
        return new Author(authorEntity.getId(), authorEntity.getName(), authorEntity.getSurname());
    }

    public static AuthorEntity toEntity(Author author) {
        return new AuthorEntity(author.getName(), author.getSurname());
    }

    public static List<Author> toDomain(List<AuthorEntity> authorEntities) {
        return authorEntities.stream().map(AuthorMapper::toDomain).toList();
    }

    /// DTOs

    public static AuthorRefDto domainToAuthorRefDto(Author author) {
        return new AuthorRefDto(author.getName(), author.getSurname());
    }

    public static Author authorRefDtoToDomain(AuthorRefDto authorRefDto) {
        return Author.builder().name(authorRefDto.name()).surname(authorRefDto.surname()).build();
    }

    public static AuthorResponseDto domainToAuthorResponseDTO(Author author) {
        //ToDo: obtener su lista de libros
        return new AuthorResponseDto(author.getName(), author.getSurname(), List.of());
    }

    public static Author authorResponseDTOToDomain(AuthorResponseDto authorResponseDto) {
        return Author.builder()
                .name(authorResponseDto.name())
                .surname((authorResponseDto.surname()))
                .build();
    }
}
