package com.supermercado.api.dto;

import com.supermercado.api.entity.Producto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Salida de un producto. En vez de devolver el objeto Categoria completo,
 * devolvemos solo id y nombre: es mas simple y mas claro para quien consume la API.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private String codigoBarras;
    private Long categoriaId;
    private String categoriaNombre;
    private Long proveedorId;
    private String proveedorNombre;

    public static ProductoResponse from(Producto p) {
        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .codigoBarras(p.getCodigoBarras())
                .categoriaId(p.getCategoria() != null ? p.getCategoria().getId() : null)
                .categoriaNombre(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
                .proveedorId(p.getProveedor() != null ? p.getProveedor().getId() : null)
                .proveedorNombre(p.getProveedor() != null ? p.getProveedor().getNombre() : null)
                .build();
    }
}
