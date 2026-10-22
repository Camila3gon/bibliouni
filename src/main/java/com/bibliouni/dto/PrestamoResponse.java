package com.bibliouni.dto;

import com.bibliouni.modelo.Prestamo;

import java.time.LocalDate;

public record PrestamoResponse(
        Long id,
        Long usuarioId,
        Long materialId,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucionEsperada,
        LocalDate fechaDevolucionReal,
        boolean activo) {

    public static PrestamoResponse desde(Prestamo p) {
        return new PrestamoResponse(
                p.getId(),
                p.getUsuario().getId(),
                p.getMaterial().getId(),
                p.getFechaPrestamo(),
                p.getFechaDevolucionEsperada(),
                p.getFechaDevolucionReal(),
                p.isActivo());
    }
}
