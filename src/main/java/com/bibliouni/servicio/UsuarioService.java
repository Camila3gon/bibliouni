package com.bibliouni.servicio;

import com.bibliouni.dto.UsuarioRequest;
import com.bibliouni.excepcion.RecursoNoEncontradoException;
import com.bibliouni.excepcion.ReglaNegocioException;
import com.bibliouni.modelo.Usuario;
import com.bibliouni.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ReglaNegocioException("Ya existe un usuario con el correo " + request.email());
        }
        return usuarioRepository.save(new Usuario(request.nombre(), request.email()));
    }

    public Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }
}
