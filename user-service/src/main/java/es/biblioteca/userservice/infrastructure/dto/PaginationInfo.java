package es.biblioteca.userservice.infrastructure.dto;

public record PaginationInfo(int page, int size, long totalElements, int totalPages) {
}
