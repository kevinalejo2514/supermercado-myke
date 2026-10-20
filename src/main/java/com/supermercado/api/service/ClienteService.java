package com.supermercado.api.service;

import com.supermercado.api.dto.ClienteRequest;
import com.supermercado.api.dto.ClienteResponse;
import com.supermercado.api.entity.Cliente;
import com.supermercado.api.exception.ConflictException;
import com.supermercado.api.exception.ResourceNotFoundException;
import com.supermercado.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clientes;

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clientes.findAll().stream()
                .map(ClienteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtener(@NonNull Long id) {
        return ClienteResponse.from(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerPorDocumento(String documento) {
        return clientes.findByDocumento(documento)
                .map(ClienteResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con documento " + documento + " no existe"));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        validarDocumento(request.getDocumento(), null);

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setDocumento(request.getDocumento());
        cliente.setTelefono(request.getTelefono());
        return ClienteResponse.from(clientes.save(cliente));
    }

    @Transactional
    public ClienteResponse actualizar(@NonNull Long id, ClienteRequest request) {
        Cliente cliente = buscarEntidad(id);
        validarDocumento(request.getDocumento(), id);

        cliente.setNombre(request.getNombre());
        cliente.setDocumento(request.getDocumento());
        cliente.setTelefono(request.getTelefono());
        return ClienteResponse.from(clientes.save(cliente));
    }

    @Transactional
    public void eliminar(@NonNull Long id) {
        buscarEntidad(id);
        clientes.deleteById(id);
    }

    /** REGLA: el documento del cliente es unico. */
    private void validarDocumento(String documento, Long excludeId) {
        boolean duplicado = (excludeId == null)
                ? clientes.existsByDocumento(documento)
                : clientes.existsByDocumentoAndIdNot(documento, excludeId);
        if (duplicado) {
            throw new ConflictException("Ya existe un cliente con el documento " + documento);
        }
    }

    private Cliente buscarEntidad(@NonNull Long id) {
        return clientes.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.porId("Cliente", id));
    }
}
