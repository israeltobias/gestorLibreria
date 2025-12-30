package es.biblioteca.userservice.application.service;

import es.biblioteca.userservice.domain.model.Role;
import es.biblioteca.userservice.domain.model.User;
import es.biblioteca.userservice.domain.repository.UserRepositoryPort;
import es.biblioteca.userservice.infrastructure.dto.UserResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("UserService test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepositoryPort userRepositoryPort;

    @InjectMocks
    UserServiceImpl userService;

    // =========================================================================
    // TESTS PARA getUsers(..)
    // =========================================================================

    @Test
    @DisplayName("Debería devolver una lista de usuarios si existen en BD")
    void getUsers_ShouldReturnList_whenThereAreUsers() {
        String username = "username";
        List<Role> roles = List.of(Role.USER, Role.ADMIN);
        UserResponseDTO user1 = new UserResponseDTO();
        user1.setUsername(username);
        user1.setRoles(roles);
        UserResponseDTO user2 = new UserResponseDTO();
        user2.setUsername(username);
        user2.setRoles(roles);
        List<UserResponseDTO> userResponseDTOList = List.of(user1, user2);

        when(userRepositoryPort.getUsers()).thenReturn(userResponseDTOList);

        List<UserResponseDTO> actualResponse = userService.getUsers();

        assertThat(actualResponse)
                .isNotNull()
                .isEqualTo(userResponseDTOList)
                .hasSize(userResponseDTOList.size())
                .usingRecursiveComparison().isEqualTo(userResponseDTOList);

        verify(userRepositoryPort, times(1)).getUsers();
    }

    @Test
    @DisplayName("Debería devolver una lista vacía de usuarios si no existen en BD")
    void getUsers_ShouldReturnList_whenThereAreNotUsers() {
        when(userRepositoryPort.getUsers()).thenReturn(List.of());

        List<UserResponseDTO> actualResponse = userService.getUsers();

        assertThat(actualResponse)
                .isNotNull()
                .isEqualTo(List.of())
                .isEmpty();

        verify(userRepositoryPort, times(1)).getUsers();
    }

    // =========================================================================
    // TESTS PARA getUser(..)
    // =========================================================================

    @Test
    @DisplayName("Debería devolver un usuario si este existe en la BD")
    void getUser_ShouldReturnUser_WhenExists() {
        String username = "username";
        String password = "password";
        List<Role> roles = List.of(Role.USER, Role.ADMIN);
        Long id = 1L;
        User userRequest = new User(id, username, password, new HashSet<>(roles));
        UserResponseDTO expectedUserResponseDTO = new UserResponseDTO(username, roles);

        when((userRepositoryPort.findByUsername(username))).thenReturn(Optional.of(userRequest));

        Optional<UserResponseDTO> actualResponse = userService.getUser(username);

        assertThat(actualResponse)
                .isNotNull()
                .isPresent()
                .hasValueSatisfying(userResponseDTO -> {
                    assertThat(userResponseDTO)
                            .hasFieldOrProperty("username")
                            .hasFieldOrProperty("roles");
                    assertThat(userResponseDTO.getUsername()).isEqualTo(expectedUserResponseDTO.getUsername());
                    assertThat(userResponseDTO.getRoles()).containsExactlyInAnyOrderElementsOf(expectedUserResponseDTO.getRoles());
                });

        verify(userRepositoryPort, times(1)).findByUsername(anyString());
    }

    @Test
    @DisplayName("getUser debería devolver un Optional vacío cuando el usuario no existe")
    void getUser_shouldReturnEmptyOptional_whenUserDoesNotExist() {
        String username = "username";

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> actualResponse = userService.getUser(username);

        assertThat(actualResponse)
                .isNotNull()
                .isEmpty();

        verify(userRepositoryPort, times(1)).findByUsername(username);
    }
}