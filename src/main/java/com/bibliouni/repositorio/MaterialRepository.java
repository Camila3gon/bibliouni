package com.bibliouni.repositorio;

import com.bibliouni.modelo.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    boolean existsByIsbn(String isbn);

    List<Material> findByDisponibleTrue();

    List<Material> findByDisponibleTrueAndCategoriaIn(Collection<String> categorias);
}
