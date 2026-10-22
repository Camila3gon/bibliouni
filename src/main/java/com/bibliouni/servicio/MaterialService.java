package com.bibliouni.servicio;

import com.bibliouni.dto.MaterialRequest;
import com.bibliouni.dto.PaginaResponse;
import com.bibliouni.excepcion.RecursoNoEncontradoException;
import com.bibliouni.excepcion.ReglaNegocioException;
import com.bibliouni.modelo.Material;
import com.bibliouni.repositorio.MaterialRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material registrar(MaterialRequest request) {
        if (materialRepository.existsByIsbn(request.isbn())) {
            throw new ReglaNegocioException("Ya existe un material con el ISBN " + request.isbn());
        }
        Material material = new Material(request.titulo(), request.autor(), request.isbn(), request.categoria());
        return materialRepository.save(material);
    }

    public PaginaResponse<Material> listar(int pagina, int tamano) {
        int paginaSegura = Math.max(0, pagina);
        int tamanoSeguro = Math.max(1, Math.min(tamano, 100));
        return PaginaResponse.desde(
                materialRepository.findAll(PageRequest.of(paginaSegura, tamanoSeguro, Sort.by("id"))));
    }

    public Material obtener(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Material no encontrado: " + id));
    }
}
