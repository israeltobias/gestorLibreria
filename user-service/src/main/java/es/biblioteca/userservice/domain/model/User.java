package es.biblioteca.userservice.domain.model;

import es.biblioteca.userservice.domain.annotations.LogMasked;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
public class User implements UserDetails {
    private Long id;
    private String username;
    @LogMasked
    private String password;
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Añadiendo el prefijo
                .toList();
    }
}
