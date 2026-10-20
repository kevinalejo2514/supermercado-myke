package com.supermercado.api.exception;

/**
 * Se lanza cuando los datos o una regla de negocio no se cumplen
 * (respuesta 400): venta sin detalles, stock insuficiente, precio invalido...
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
