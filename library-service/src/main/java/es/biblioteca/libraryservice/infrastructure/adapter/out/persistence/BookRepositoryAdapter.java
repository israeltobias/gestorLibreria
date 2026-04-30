package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Book;
import es.biblioteca.libraryservice.domain.repository.BookRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryAdapter implements BookRepositoryPort {

    private final BookJpaRepository bookJpaRepository;

    @Override
    public Book save(Book book) {

        BookEntity bookEntity = BookMapper.toEntity(book);
        // Si la entidad es nueva (lo que para nosotros significa id=0),
        // la preparamos para Hibernate forzando el ID a null. Hibernate requiere que sea null para nuevos objetos
        if (bookEntity.isNew()) {
            bookEntity.setId(null);
        }
        return BookMapper.toDomain(bookJpaRepository.save(bookEntity));
    }

    @Override
    public Optional<Book> findeByName(String name) {
        return bookJpaRepository.findByName(name).map(BookMapper::toDomain);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {

        return bookJpaRepository.findByIsbn(isbn).map(BookMapper::toDomain);
    }

    @Override
    public List<Book> getAll() {
        return BookMapper.toDomain(bookJpaRepository.findAll());
    }
}
