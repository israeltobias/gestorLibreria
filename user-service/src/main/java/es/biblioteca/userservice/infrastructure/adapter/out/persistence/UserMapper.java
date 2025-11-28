package es.biblioteca.userservice.infrastructure.adapter.out.persistence;

import es.biblioteca.userservice.domain.model.User;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;

import java.util.List;

public class UserMapper {
    private UserMapper() {
        super();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.builder().id(userEntity.getId()).username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles()).build();
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRoles(user.getRoles());
        return entity;
    }

    // En tu clase de mapeo
    public static UserResponseDTO toDTO(UserEntity user) {
        return new UserResponseDTO(
                user.getUsername(),
                user.getRoles() // Asumiendo que getRoles() devuelve List<String>
        );
    }

    public static List<UserResponseDTO> toDTO(List<UserEntity> userList) {
        return userList.stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}
