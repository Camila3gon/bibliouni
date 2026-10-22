package com.bibliouni.servicio;

import com.bibliouni.excepcion.RecursoNoEncontradoException;
import com.bibliouni.excepcion.ReglaNegocioException;
import com.bibliouni.modelo.Material;
import com.bibliouni.modelo.Prestamo;
import com.bibliouni.modelo.Usuario;
import com.bibliouni.repositorio.MaterialRepository;
import com.bibliouni.repositorio.PrestamoRepository;
import com.bibliouni.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class PrestamoService {

    /** Máximo de préstamos activos por usuario. */
    public static final int MAX_PRESTAMOS_ACTIVOS = 3;

    /** Días de duración de un préstamo. */
    public static final int DIAS_PRESTAMO = 7;

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           UsuarioRepository usuarioRepository,
                           MaterialRepository materialRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.materialRepository = materialRepository;
    }

    @Transactional
    public Prestamo prestar(Long usuarioId, Long materialId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + usuarioId));
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Material no encontrado: " + materialId));

        if (prestamoRepository.countByUsuarioIdAndActivoTrue(usuarioId) >= MAX_PRESTAMOS_ACTIVOS) {
            throw new ReglaNegocioException(
                    "El usuario ya tiene " + MAX_PRESTAMOS_ACTIVOS + " préstamos activos");
        }
        if (!material.isDisponible()) {
            throw new ReglaNegocioException("El material no está disponible: " + materialId);
        }

        LocalDate hoy = LocalDate.now();
        Prestamo prestamo = new Prestamo(usuario, material, hoy, hoy.plusDays(DIAS_PRESTAMO));
        material.setDisponible(false);
        materialRepository.save(material);
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo devolver(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Préstamo no encontrado: " + prestamoId));

        if (!prestamo.isActivo()) {
            throw new ReglaNegocioException("El préstamo ya fue devuelto: " + prestamoId);
        }

        prestamo.setActivo(false);
        prestamo.setFechaDevolucionReal(LocalDate.now());

        Material material = prestamo.getMaterial();
        material.setDisponible(true);
        materialRepository.save(material);
        return prestamoRepository.save(prestamo);
    }
}
