package es.biblioteca.userservice.infrastructure.dto;

import java.time.Instant;

public record SuccessApiResponse<T>(Integer status, String message, String path, Instant time
                                , T data, PaginationInfo pagination) {}
