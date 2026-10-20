package com.supermercado.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

/**
 * Una linea del cuerpo de la venta: "producto 3, cantidad 2".
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaRequest {

    @NotNull(message = "El productoId es obligatorio")
    @NonNull
    private Long productoId;

    /** @Min(1) rechaza cantidades 0 o negativas con un 400 automatico. */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

}
