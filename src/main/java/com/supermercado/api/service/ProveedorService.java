package com.supermercado.api.service;

import com.supermercado.api.dto.ProveedorRequest;
import com.supermercado.api.dto.ProveedorResponse;
import com.supermercado.api.entity.Proveedor;
import com.supermercado.api.exception.ConflictException;
import com.supermercado.api.exception.ResourceNotFoundException;
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
public class ProveedorService {

    private final ProveedorRepository proveedores;
    private final ProductoRepository productos;

    @Transactional(readOnly = true)
    public List<ProveedorResponse> listar() {
        return proveedores.findAll().stream()
                .map(ProveedorResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProveedorResponse obtener(@NonNull Long id) {
        return ProveedorResponse.from(buscarEntidad(id));
    }

    @Transactional
    public ProveedorResponse crear(ProveedorRequest request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(request.getNombre());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setCorreo(request.getCorreo());
        return ProveedorResponse.from(proveedores.save(proveedor));
    }

    @Transactional
    public ProveedorResponse actualizar(@NonNull Long id, ProveedorRequest request) {
        Proveedor proveedor = buscarEntidad(id);
        proveedor.setNombre(request.getNombre());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setCorreo(request.getCorreo());
        return ProveedorResponse.from(proveedores.save(proveedor));
    }

    /** REGLA: no se puede eliminar un proveedor que tenga productos. */
    @Transactional
    public void eliminar(@NonNull Long id) {
        buscarEntidad(id);
        if (productos.countByProveedorId(id) > 0) {
            throw new ConflictException(
                    "No se puede eliminar el proveedor porque tiene productos asociados");
        }
        proveedores.deleteById(id);
    }

    private Proveedor buscarEntidad(@NonNull Long id) {
        return proveedores.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.porId("Proveedor", id));
    }
}
