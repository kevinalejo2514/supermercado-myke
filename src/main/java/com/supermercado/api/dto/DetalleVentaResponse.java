package com.supermercado.api.dto;

import com.supermercado.api.entity.DetalleVenta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaResponse {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static DetalleVentaResponse from(DetalleVenta d) {
        return DetalleVentaResponse.builder()
                .id(d.getId())
                .productoId(d.getProducto().getId())
                .productoNombre(d.getProducto().getNombre())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .build();
    }
}
