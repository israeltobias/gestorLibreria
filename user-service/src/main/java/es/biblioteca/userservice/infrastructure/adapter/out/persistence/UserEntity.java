package es.biblioteca.userservice.infrastructure.adapter.out.persistence;

import es.biblioteca.userservice.domain.model.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<Role> roles;

    @Override
    @Transient
    public boolean isNew() {
        // Le dices explícitamente a Hibernate/Spring Data:
        // "Si mi 'id' es null, soy una entidad NUEVA. Haz un INSERT."
        // "Si mi 'id' NO es null, soy una entidad EXISTENTE. Haz un UPDATE/MERGE."
        return this.id == null || this.id == 0;
    }
}
