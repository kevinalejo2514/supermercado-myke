package com.supermercado.api.exception;

/**
 * Se lanza cuando la peticion entra en conflicto con el estado actual
 * del servidor (respuesta 409): codigo de barras duplicado,
 * documento duplicado, eliminar una categoria que tiene productos...
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String mensaje) {
        super(mensaje);
    }
}
