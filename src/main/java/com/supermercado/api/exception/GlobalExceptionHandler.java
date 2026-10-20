package com.supermercado.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador GLOBAL de excepciones.
 *
 * Cualquier excepcion que lance un controller o un servicio llega aqui.
 * En vez de devolver el "Whitelabel Error Page" de Spring (HTML feo),
 * devolvemos siempre JSON con la estructura ErrorResponse y el codigo HTTP
 * que corresponda.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Recurso inexistente -> 404. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    /** Regla de negocio o dato invalido -> 400. */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    /** Conflicto (duplicados, relaciones existentes) -> 409. */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    /**
     * Falla la validacion de los @NotBlank, @Positive, @Min... de los DTOs -> 400.
     * Devolvemos campo por campo que esta mal.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, String>> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(this::campoError)
                .collect(Collectors.toList());
        return construir(HttpStatus.BAD_REQUEST, "Datos invalidos", request, errores);
    }

    /** Llego un id no numerico en la ruta (ej: GET /api/productos/abc) -> 400. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String mensaje = "El parametro '" + ex.getName() + "' tiene un valor invalido: " + ex.getValue();
        return construir(HttpStatus.BAD_REQUEST, mensaje, request, null);
    }

    /** El cuerpo JSON no se puede leer o no respeta la forma esperada -> 400. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNoLegible(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la peticion no es un JSON valido", request, null);
    }

    /**
     * Violacion de integridad de la base de datos (ej: unico duplicado que
     * llego por la via de JPA) -> 409.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegridad(DataIntegrityViolationException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, "La operacion viola una restriccion de integridad de datos", request, null);
    }

    /**
     * La ruta existe pero con otro metodo (ej: PUT a /api/ventas/1) -> 405.
     * Sin este metodo, el handler generico de abajo lo convertiria en 500.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMetodo(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String mensaje = "El metodo " + ex.getMethod() + " no esta permitido en esta ruta. Metodos admitidos: "
                + String.join(", ", ex.getSupportedMethods() == null ? new String[]{} : ex.getSupportedMethods());
        return construir(HttpStatus.METHOD_NOT_ALLOWED, mensaje, request, null);
    }

    /** El Content-Type no es el esperado (ej: XML en un POST) -> 415. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return construir(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Content-Type no soportado. Usa application/json", request, null);
    }

    /** Falta un @RequestParam obligatorio -> 400. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleParamFaltante(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "Falta el parametro obligatorio '" + ex.getParameterName() + "'", request, null);
    }

    /** La ruta no existe en absoluto (ej: /api/inventos) -> 404. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleRutaInexistente(NoResourceFoundException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "La ruta " + request.getRequestURI() + " no existe", request, null);
    }

    /** Cualquier otro error inesperado -> 500. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request, null);
    }

    private Map<String, String> campoError(FieldError fe) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("campo", fe.getField());
        m.put("mensaje", fe.getDefaultMessage());
        return m;
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje,
                                                    HttpServletRequest request, List<Map<String, String>> errores) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(mensaje)
                .path(request.getRequestURI())
                .errors(errores)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
