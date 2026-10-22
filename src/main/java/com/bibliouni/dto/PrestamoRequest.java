package com.bibliouni.dto;

import jakarta.validation.constraints.NotNull;

public record PrestamoRequest(
        @NotNull(message = "usuarioId es obligatorio") Long usuarioId,
        @NotNull(message = "materialId es obligatorio") Long materialId) {
}
