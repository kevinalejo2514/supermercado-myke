package com.supermercado.api.dto;

import com.supermercado.api.entity.Cliente;
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
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String documento;
    private String telefono;

    public static ClienteResponse from(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .documento(c.getDocumento())
                .telefono(c.getTelefono())
                .build();
    }
}
