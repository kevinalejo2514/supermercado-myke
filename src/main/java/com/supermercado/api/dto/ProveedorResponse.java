package com.supermercado.api.dto;

import com.supermercado.api.entity.Proveedor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorResponse {

    private Long id;
    private String nombre;
    private String telefono;
    private String correo;

    public static ProveedorResponse from(Proveedor p) {
        return ProveedorResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .telefono(p.getTelefono())
                .correo(p.getCorreo())
                .build();
    }
}
