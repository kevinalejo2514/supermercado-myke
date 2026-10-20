package com.supermercado.api.exception;

/**
 * Se lanza cuando el recurso pedido no existe (respuesta 404).
 * Extiende RuntimeException: es una excepcion de RUNTIME,
 * no obliga a los metodos que la lanzan a declararla con "throws".
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    /** Mensaje estandar: "Producto con id 99 no existe". */
    public static ResourceNotFoundException porId(String recurso, Long id) {
        return new ResourceNotFoundException(recurso + " con id " + id + " no existe");
    }
}
