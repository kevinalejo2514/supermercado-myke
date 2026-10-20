package com.supermercado.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Datos de ENTRADA para crear o actualizar una categoria.
 * Si el campo viene vacio o nulo, la validacion responde 400 automaticamente.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;
}
