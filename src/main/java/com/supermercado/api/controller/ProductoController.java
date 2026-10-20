package com.supermercado.api.controller;

import com.supermercado.api.dto.ProductoRequest;
import com.supermercado.api.dto.ProductoResponse;
import com.supermercado.api.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoService.listar();
    }

    /**
     * Rutas literales como /buscar y /stock-bajo tienen PRIORIDAD
     * sobre /{id}, asi GET /api/productos/buscar nunca choca con un id.
     */
    @GetMapping("/buscar")
    public List<ProductoResponse> buscar(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo(@RequestParam(defaultValue = "10") Integer limite) {
        return productoService.listarStockBajo(limite);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<ProductoResponse> porCategoria(@PathVariable @NonNull Long categoriaId) {
        return productoService.listarPorCategoria(categoriaId);
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable @NonNull Long id) {
        return productoService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse creado = productoService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable @NonNull Long id,
                                       @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
