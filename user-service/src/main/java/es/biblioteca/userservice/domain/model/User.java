package es.biblioteca.userservice.domain.model;

import es.biblioteca.userservice.domain.annotations.LogMasked;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class User /*implements UserDetails */{
    private Long id;
    private String username;
    @LogMasked
    private String password;
    private Set<Role> roles;

    private static final Set<Role> ALLOWED_ROLES = Set.of(Role.USER, Role.ADMIN);

    @Builder
    public User(Long id, String username, String password, Set<Role> roles) {
        // --- VALIDACIONES DE GUARDIA EN EL CONSTRUCTOR ---
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Un usuario debe tener al menos un rol.");
        }
        // Comprobamos si todos los roles proporcionados están en la lista de permitidos
        if (!ALLOWED_ROLES.containsAll(roles)) {
            // Para un mejor mensaje de error, encontramos los roles inválidos
            Set<Role> invalidRoles = roles.stream()
                    .filter(role -> !ALLOWED_ROLES.contains(role))
                    .collect(Collectors.toSet());
            throw new IllegalArgumentException("Roles inválidos proporcionados: " + invalidRoles);
        }

        this.id = id;
        this.username = username;
        this.password = password;
        // Creamos una copia inmutable para que el estado no pueda ser modificado desde fuera
        this.roles = Set.copyOf(roles);
    }

    // Métodos de negocio que pertenecen al dominio
    public boolean isAdmin() {
        return this.roles.contains(Role.ADMIN);
    }

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Añadiendo el prefijo
                .toList();
    }*/
}
