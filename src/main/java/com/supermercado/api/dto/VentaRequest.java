package com.supermercado.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {

    @NotNull(message = "El clienteId es obligatorio")
    @NonNull
    private Long clienteId;

    @NotNull(message = "La venta debe tener detalles")
    @NotEmpty(message = "La venta debe tener al menos un detalle")
    @Valid
    private List<DetalleVentaRequest> detalles;
}
