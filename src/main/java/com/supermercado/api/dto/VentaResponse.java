package com.supermercado.api.dto;

import com.supermercado.api.entity.Venta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponse {

    private Long id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private Long clienteId;
    private String clienteNombre;
    private List<DetalleVentaResponse> detalles;

    public static VentaResponse from(Venta v) {
        return VentaResponse.builder()
                .id(v.getId())
                .fecha(v.getFecha())
                .total(v.getTotal())
                .clienteId(v.getCliente().getId())
                .clienteNombre(v.getCliente().getNombre())
                .detalles(v.getDetalles().stream()
                        .map(DetalleVentaResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
