package com.supermercado.api.controller;

import com.supermercado.api.dto.ProveedorRequest;
import com.supermercado.api.dto.ProveedorResponse;
import com.supermercado.api.service.ProveedorService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public List<ProveedorResponse> listar() {
        return proveedorService.listar();
    }

    @GetMapping("/{id}")
    public ProveedorResponse obtener(@PathVariable @NonNull Long id) {
        return proveedorService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ProveedorResponse> crear(@Valid @RequestBody ProveedorRequest request) {
        ProveedorResponse creada = proveedorService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping("/{id}")
    public ProveedorResponse actualizar(@PathVariable @NonNull Long id,
                                        @Valid @RequestBody ProveedorRequest request) {
        return proveedorService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
