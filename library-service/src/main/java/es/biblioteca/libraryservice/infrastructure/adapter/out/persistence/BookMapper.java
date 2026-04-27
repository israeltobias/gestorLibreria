package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Book;

public class BookMapper {

    private BookMapper() {
        super();
    }

    public static BookEntity toEntity(Book book) {
        return new BookEntity(book.getName(), book.getIsbn(), book.getGenre(), book.getNumPages(), book.getAuthor());
    }

    public static Book toDomain(BookEntity bookEntity) {
        return Book.builder()
                .name(bookEntity.getName())
                .isbn(bookEntity.getIsbn())
                .genre(bookEntity.getGenre())
                .numPages(bookEntity.getNumPages())
                .author(bookEntity.getAuthor())
                .build();
    }

    ///DTOs
}
