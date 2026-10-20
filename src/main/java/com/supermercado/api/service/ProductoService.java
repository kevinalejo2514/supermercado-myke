package com.supermercado.api.service;

import com.supermercado.api.dto.ProductoRequest;
import com.supermercado.api.dto.ProductoResponse;
import com.supermercado.api.entity.Categoria;
import com.supermercado.api.entity.Producto;
import com.supermercado.api.entity.Proveedor;
import com.supermercado.api.exception.ConflictException;
import com.supermercado.api.exception.ResourceNotFoundException;
import com.supermercado.api.repository.CategoriaRepository;
import com.supermercado.api.repository.ProductoRepository;
import com.supermercado.api.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productos;
    private final CategoriaRepository categorias;
    private final ProveedorRepository proveedores;

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productos.findAll().stream()
                .map(ProductoResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtener(@NonNull Long id) {
        return ProductoResponse.from(buscarEntidad(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        validarCodigoBarras(request.getCodigoBarras(), null);

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCodigoBarras(request.getCodigoBarras());
        producto.setCategoria(resolverCategoria(request.getCategoriaId()));
        producto.setProveedor(resolverProveedor(request.getProveedorId()));

        return ProductoResponse.from(productos.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(@NonNull Long id, ProductoRequest request) {
        Producto producto = buscarEntidad(id); // 404 si no existe
        validarCodigoBarras(request.getCodigoBarras(), id);

        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCodigoBarras(request.getCodigoBarras());
        producto.setCategoria(resolverCategoria(request.getCategoriaId()));
        producto.setProveedor(resolverProveedor(request.getProveedorId()));

        return ProductoResponse.from(productos.save(producto));
    }

    @Transactional
    public void eliminar(@NonNull Long id) {
        buscarEntidad(id);
        productos.deleteById(id);
    }

    /** GET /api/productos/categoria/{categoriaId} */
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarPorCategoria(@NonNull Long categoriaId) {
        if (!categorias.existsById(categoriaId)) {
            throw ResourceNotFoundException.porId("Categoria", categoriaId);
        }
        return productos.findByCategoriaId(categoriaId).stream()
                .map(ProductoResponse::from)
                .collect(Collectors.toList());
    }

    /** GET /api/productos/buscar?nombre=... (sin distinguir mayusculas) */
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return productos.findByNombreContainingIgnoreCase(nombre).stream()
                .map(ProductoResponse::from)
                .collect(Collectors.toList());
    }

    /** GET /api/productos/stock-bajo?limite=10 */
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarStockBajo(Integer limite) {
        return productos.findByStockLessThanEqual(limite).stream()
                .map(ProductoResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * REGLA: el codigo de barras es unico.
     * excludeId = null al crear; = id propio al actualizar.
     */
    private void validarCodigoBarras(String codigoBarras, Long excludeId) {
        boolean duplicado = (excludeId == null)
                ? productos.existsByCodigoBarras(codigoBarras)
                : productos.existsByCodigoBarrasAndIdNot(codigoBarras, excludeId);
        if (duplicado) {
            throw new ConflictException("Ya existe un producto con el codigo de barras " + codigoBarras);
        }
    }

    /** Si trae categoriaId, debe existir; si no trae, se queda sin categoria. */
    private Categoria resolverCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categorias.findById(categoriaId)
                .orElseThrow(() -> ResourceNotFoundException.porId("Categoria", categoriaId));
    }

    private Proveedor resolverProveedor(Long proveedorId) {
        if (proveedorId == null) {
            return null;
        }
        return proveedores.findById(proveedorId)
                .orElseThrow(() -> ResourceNotFoundException.porId("Proveedor", proveedorId));
    }

    private Producto buscarEntidad(@NonNull Long id) {
        return productos.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.porId("Producto", id));
    }
}
