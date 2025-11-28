package es.biblioteca.userservice.infrastructure.dto;

import es.biblioteca.userservice.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private List<Role> roles;
}
