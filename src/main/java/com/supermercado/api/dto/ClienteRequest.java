package com.supermercado.api.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /** Obligatorio porque la regla de negocio lo exige unico. */
    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    private String telefono;
}
