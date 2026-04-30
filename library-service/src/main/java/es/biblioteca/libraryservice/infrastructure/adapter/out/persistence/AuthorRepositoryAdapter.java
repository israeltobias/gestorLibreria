package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Author;
import es.biblioteca.libraryservice.domain.repository.AuthorRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryAdapter implements AuthorRepositoryPort {

    private final AuthorJpaRepository authorJpaRepository;


    @Override
    public Author save(Author author) {
        AuthorEntity authorEntity = AuthorMapper.toEntity(author);
        // Si la entidad es nueva (lo que para nosotros significa id=0),
        // la preparamos para Hibernate forzando el ID a null. Hibernate requiere que sea null para nuevos objetos
        if (authorEntity.isNew()) {
            authorEntity.setId(null);
        }
        return AuthorMapper.toDomain(authorJpaRepository.save(authorEntity));
    }

    @Override
    public Optional<Author> findByNameAndSurname(String name, String surname) {
        return authorJpaRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public List<Author> getAll() {
        return AuthorMapper.toDomain(authorJpaRepository.findAll());
    }
}
