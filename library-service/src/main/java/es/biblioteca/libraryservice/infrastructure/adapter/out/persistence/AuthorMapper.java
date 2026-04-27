package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Author;

public class AuthorMapper {

    private AuthorMapper() {
        super();
    }

    public static Author toDomain(AuthorEntity authorEntity) {
        return new Author(authorEntity.getId(), authorEntity.getName(), authorEntity.getSurname());
    }

    public static AuthorEntity toEntitiy(Author author) {
        return new AuthorEntity(author.getName(), author.getSurname());
    }


    ///DTOs
}
