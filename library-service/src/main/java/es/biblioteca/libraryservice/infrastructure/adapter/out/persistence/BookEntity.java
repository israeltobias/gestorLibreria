package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import es.biblioteca.libraryservice.domain.model.Genre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class BookEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String isbn;
    @Column
    private Genre genre;
    @Column
    private int numPages;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity author;

    public BookEntity(String name, String isbn, Genre genre, int numPages, AuthorEntity author) {
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
        this.numPages = numPages;
        this.author = author;
    }

    @Override
    @Transient
    public boolean isNew() {
        // Le dices explícitamente a Hibernate/Spring Data:
        // "Si mi 'id' es null, soy una entidad NUEVA. Haz un INSERT."
        // "Si mi 'id' NO es null, soy una entidad EXISTENTE. Haz un UPDATE/MERGE."
        return this.id == null || this.id == 0;
    }


    // No deberíamos tener un setter para el ID, ISBN o el autor, ya que son inmutables.

    // 7. Implementación correcta de equals y hashCode basada en el ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Usa un valor constante si el ID es nulo para mantener la consistencia
        // antes de que la entidad sea persistida.
        return getClass().hashCode();
    }
}
