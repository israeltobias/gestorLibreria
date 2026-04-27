package es.biblioteca.libraryservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
public class AuthorEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column
    private String surname;
    // Relación inversa: un autor puede tener muchos libros
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "author", orphanRemoval = true)
    private Set<BookEntity> books = new HashSet<>();

    AuthorEntity(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void addBook(BookEntity book) {
        this.books.add(book);
        book.setAuthor(this); // <-- ¡LA CLAVE! Sincroniza el otro lado.
    }

    public void removeBook(BookEntity book) {
        this.books.remove(book);
        book.setAuthor(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorEntity author = (AuthorEntity) o;
        return Objects.equals(getName(), author.getName()) && Objects.equals(getSurname(), author.getSurname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname());
    }

    @Override
    public boolean isNew() {
        // Le dices explícitamente a Hibernate/Spring Data:
        // "Si mi 'id' es null, soy una entidad NUEVA. Haz un INSERT."
        // "Si mi 'id' NO es null, soy una entidad EXISTENTE. Haz un UPDATE/MERGE."
        return this.id == null || this.id == 0;
    }
}
