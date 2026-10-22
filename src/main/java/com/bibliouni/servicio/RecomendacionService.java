package com.bibliouni.servicio;

import com.bibliouni.excepcion.RecursoNoEncontradoException;
import com.bibliouni.modelo.Material;
import com.bibliouni.modelo.Prestamo;
import com.bibliouni.repositorio.MaterialRepository;
import com.bibliouni.repositorio.PrestamoRepository;
import com.bibliouni.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Gancho innovador: recomienda materiales disponibles según las categorías
 * que el usuario ha prestado con más frecuencia.
 */
@Service
public class RecomendacionService {

    /** Cantidad máxima de materiales recomendados. */
    public static final int MAX_RECOMENDACIONES = 5;

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final MaterialRepository materialRepository;

    public RecomendacionService(UsuarioRepository usuarioRepository,
                                PrestamoRepository prestamoRepository,
                                MaterialRepository materialRepository) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
        this.materialRepository = materialRepository;
    }

    public List<Material> recomendar(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado: " + usuarioId);
        }

        List<Prestamo> historial = prestamoRepository.findByUsuarioId(usuarioId);

        Map<String, Long> frecuenciaPorCategoria = historial.stream()
                .map(p -> p.getMaterial().getCategoria())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Sin historial (o sin categorías conocidas): lista por defecto, nunca error.
        if (frecuenciaPorCategoria.isEmpty()) {
            return materialRepository.findByDisponibleTrue().stream()
                    .limit(MAX_RECOMENDACIONES)
                    .toList();
        }

        Set<Long> yaPrestados = historial.stream()
                .map(p -> p.getMaterial().getId())
                .collect(Collectors.toSet());

        return materialRepository.findByDisponibleTrueAndCategoriaIn(frecuenciaPorCategoria.keySet()).stream()
                .filter(m -> !yaPrestados.contains(m.getId()))
                .sorted(Comparator
                        .comparingLong((Material m) -> frecuenciaPorCategoria.get(m.getCategoria()))
                        .reversed()
                        .thenComparing(Material::getId))
                .limit(MAX_RECOMENDACIONES)
                .toList();
    }
}
