package com.bibliouni.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MaterialRequest(
        @NotBlank(message = "El título es obligatorio") String titulo,
        @NotBlank(message = "El autor es obligatorio") String autor,
        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(regexp = "\\d{10}|\\d{13}", message = "El ISBN debe tener 10 o 13 dígitos") String isbn,
        String categoria) {
}
