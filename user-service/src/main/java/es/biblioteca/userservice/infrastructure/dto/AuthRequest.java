package es.biblioteca.userservice.infrastructure.dto;

import es.biblioteca.userservice.domain.annotations.LogMasked;
import es.biblioteca.userservice.domain.model.Role;

import java.util.List;

public record AuthRequest(String username, @LogMasked String password, List<Role> roles) {
}
