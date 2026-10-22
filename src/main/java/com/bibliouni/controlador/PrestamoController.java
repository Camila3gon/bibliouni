package com.bibliouni.controlador;

import com.bibliouni.dto.PrestamoRequest;
import com.bibliouni.dto.PrestamoResponse;
import com.bibliouni.servicio.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoResponse prestar(@Valid @RequestBody PrestamoRequest request) {
        return PrestamoResponse.desde(prestamoService.prestar(request.usuarioId(), request.materialId()));
    }

    @PostMapping("/{id}/devolucion")
    public PrestamoResponse devolver(@PathVariable Long id) {
        return PrestamoResponse.desde(prestamoService.devolver(id));
    }
}
