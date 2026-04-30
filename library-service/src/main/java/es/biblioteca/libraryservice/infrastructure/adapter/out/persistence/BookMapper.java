package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Book;
import es.biblioteca.libraryservice.infrastructure.dto.BookRefDto;
import es.biblioteca.libraryservice.infrastructure.dto.BookResponseDto;

import java.util.List;

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

    public static List<Book> toDomain(List<BookEntity> bookEntityList) {
        return bookEntityList.stream().map(BookMapper::toDomain).toList();
    }

    /// DTOs
    public static BookRefDto domainToBookRefDto(Book book) {
        return new BookRefDto(book.getName(), book.getIsbn(), book.getGenre());
    }

    public static Book bookRefDtoToDomain(BookRefDto bookRefDto) {
        return Book.builder()
                .name(bookRefDto.name())
                .isbn(bookRefDto.isbn())
                .genre(bookRefDto.genre())
                .build();
    }

    public static BookResponseDto domainToBookResponseDTO(Book book) {
        return new BookResponseDto(book.getName(), book.getIsbn(), book.getGenre(), book.getNumPages(),
                AuthorMapper.domainToAuthorRefDto(AuthorMapper.toDomain(book.getAuthor())));
    }

    public static Book bookResponseDTOToDomain(BookResponseDto bookResponseDto) {
        return Book.builder()
                .name(bookResponseDto.name())
                .isbn(bookResponseDto.isbn())
                .genre(bookResponseDto.genre())
                .numPages(bookResponseDto.numPages())
                .author(AuthorMapper.toEntity(AuthorMapper.authorRefDtoToDomain(bookResponseDto.author())))
                .build();
    }
}
