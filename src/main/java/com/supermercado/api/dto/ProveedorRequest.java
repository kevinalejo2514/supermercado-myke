package com.supermercado.api.dto;

import jakarta.validation.constraints.Email;
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
public class ProveedorRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String telefono;

    @Email(message = "El correo no tiene un formato valido")
    private String correo;
}
