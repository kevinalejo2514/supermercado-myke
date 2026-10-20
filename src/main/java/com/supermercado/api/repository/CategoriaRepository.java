package com.supermercado.api.repository;

import com.supermercado.api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de Categoria.
 *
 * Al extender JpaRepository<Categoria, Long> obtenemos GRATIS:
 *  save(), findById(), findAll(), deleteById(), count()...
 *
 * Spring crea la implementacion en tiempo de ejecucion.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
