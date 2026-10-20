package com.supermercado.api.controller;

import com.supermercado.api.dto.VentaRequest;
import com.supermercado.api.dto.VentaResponse;
import com.supermercado.api.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * Las ventas NO tienen PUT: no se actualizan.
 * Tampoco tienen body de entrada directo: el cuerpo es el de VentaRequest
 * (clienteId + detalles con cantidades), y precio/subtotal/total los calcula
 * el servidor.
 */
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentaRequest request) {
        VentaResponse creada = ventaService.registrar(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @GetMapping
    public List<VentaResponse> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public VentaResponse obtener(@PathVariable @NonNull Long id) {
        return ventaService.obtener(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<VentaResponse> porCliente(@PathVariable @NonNull Long clienteId) {
        return ventaService.listarPorCliente(clienteId);
    }

    /** Ejemplo: /api/ventas/fecha?inicio=2026-10-01&fin=2026-10-31 */
    @GetMapping("/fecha")
    public List<VentaResponse> porFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ventaService.listarPorFecha(inicio, fin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204, stock restituido
    }
}
