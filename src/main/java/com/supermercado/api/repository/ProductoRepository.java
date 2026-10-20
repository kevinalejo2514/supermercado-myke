package com.supermercado.api.repository;

import com.supermercado.api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Producto.
 *
 * Spring Data "deriva" SQL a partir del NOMBRE del metodo:
 *
 *  findByCategoriaId               -> WHERE categoria_id = ?
 *  findByNombreContainingIgnoreCase-> WHERE LOWER(nombre) LIKE LOWER(%?%)
 *  findByStockLessThanEqual        -> WHERE stock <= ?
 *  existsByCodigoBarras            -> SELECT COUNT(*) > 0 WHERE codigo_barras = ?
 *
 * No hay que escribir una sola linea de SQL para estas 5 consultas.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByStockLessThanEqual(Integer limite);

    boolean existsByCodigoBarras(String codigoBarras);

    /** Para update: el codigo debe ser unico, salvo el propio producto (IdNot). */
    boolean existsByCodigoBarrasAndIdNot(String codigoBarras, Long id);

    /** Usado para la regla: no se puede borrar una categoria con productos. */
    long countByCategoriaId(Long categoriaId);

    long countByProveedorId(Long proveedorId);
}
