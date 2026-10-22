package com.bibliouni.controlador;

import com.bibliouni.modelo.Material;
import com.bibliouni.servicio.RecomendacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Gancho innovador: recomendaciones de materiales por usuario. */
@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    public RecomendacionController(RecomendacionService recomendacionService) {
        this.recomendacionService = recomendacionService;
    }

    @GetMapping("/{usuarioId}")
    public List<Material> recomendar(@PathVariable Long usuarioId) {
        return recomendacionService.recomendar(usuarioId);
    }
}
