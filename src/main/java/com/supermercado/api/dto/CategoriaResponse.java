package com.supermercado.api.dto;

import com.supermercado.api.entity.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Datos de SALIDA: lo que la API devuelve al cliente.
 * Nunca devolvemos la entidad directamente (evita exponer relaciones internas
 * y problemas de serializacion con Hibernate).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponse {

    private Long id;
    private String nombre;
    private String descripcion;

    public static CategoriaResponse from(Categoria c) {
        return CategoriaResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .build();
    }
}
