package com.supermercado.api.controller;

import com.supermercado.api.dto.ClienteRequest;
import com.supermercado.api.dto.ClienteResponse;
import com.supermercado.api.service.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    /** Literal "documento" antes de /{id} -> prioridad garantizada. */
    @GetMapping("/documento/{documento}")
    public ClienteResponse porDocumento(@PathVariable String documento) {
        return clienteService.obtenerPorDocumento(documento);
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable @NonNull Long id) {
        return clienteService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse creado = clienteService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable @NonNull Long id,
                                      @Valid @RequestBody ClienteRequest request) {
        return clienteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
