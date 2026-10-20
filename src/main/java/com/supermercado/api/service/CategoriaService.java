package com.supermercado.api.service;

import com.supermercado.api.dto.CategoriaRequest;
import com.supermercado.api.dto.CategoriaResponse;
import com.supermercado.api.entity.Categoria;
import com.supermercado.api.exception.ConflictException;
import com.supermercado.api.exception.ResourceNotFoundException;
import com.supermercado.api.repository.CategoriaRepository;
import com.supermercado.api.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Capa de SERVICIO: toda la logica de negocio de categorias vive aqui.
 * El controller solo recibe la peticion y llama a estos metodos.
 */
@Service
@RequiredArgsConstructor // genera un constructor con los campos finales (Lombok)
public class CategoriaService {

    private final CategoriaRepository categorias;
    private final ProductoRepository productos;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categorias.findAll().stream()
                .map(CategoriaResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtener(@NonNull Long id) {
        return CategoriaResponse.from(buscarEntidad(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return CategoriaResponse.from(categorias.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(@NonNull Long id, CategoriaRequest request) {
        Categoria categoria = buscarEntidad(id); // 404 si no existe
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return CategoriaResponse.from(categorias.save(categoria));
    }

    /**
     * REGLA: no se puede eliminar una categoria que tenga productos.
     * Si tiene productos -> 409 Conflict.
     */
    @Transactional
    public void eliminar(@NonNull Long id) {
        buscarEntidad(id);
        if (productos.countByCategoriaId(id) > 0) {
            throw new ConflictException(
                    "No se puede eliminar la categoria porque tiene productos asociados");
        }
        categorias.deleteById(id);
    }

    private Categoria buscarEntidad(@NonNull Long id) {
        return categorias.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.porId("Categoria", id));
    }
}
