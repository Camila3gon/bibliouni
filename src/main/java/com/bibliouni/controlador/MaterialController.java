package com.bibliouni.controlador;

import com.bibliouni.dto.MaterialRequest;
import com.bibliouni.dto.PaginaResponse;
import com.bibliouni.excepcion.AccesoDenegadoException;
import com.bibliouni.modelo.Material;
import com.bibliouni.servicio.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/materiales")
public class MaterialController {

    private static final String ROL_BIBLIOTECARIO = "BIBLIOTECARIO";

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /** Solo el rol BIBLIOTECARIO (cabecera X-Rol) puede registrar materiales. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Material registrar(@RequestHeader(value = "X-Rol", required = false) String rol,
                              @Valid @RequestBody MaterialRequest request) {
        if (!ROL_BIBLIOTECARIO.equals(rol)) {
            throw new AccesoDenegadoException("Solo un bibliotecario puede registrar materiales");
        }
        return materialService.registrar(request);
    }

    @GetMapping
    public PaginaResponse<Material> listar(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return materialService.listar(page, size);
    }

    @GetMapping("/{id}")
    public Material obtener(@PathVariable Long id) {
        return materialService.obtener(id);
    }
}
