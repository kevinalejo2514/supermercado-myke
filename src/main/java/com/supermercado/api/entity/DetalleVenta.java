package com.supermercado.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Linea de una venta: "2 unidades del producto X a 15.00 c/u".
 *
 * Muchos-a-uno con Venta (pertenece a una sola venta)
 * Muchos-a-uno con Producto (describe un solo producto).
 */
@Getter
@Setter
@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /**
     * Precio del producto AL MOMENTO de la venta.
     * Se copia aqui para que si el precio del producto cambia mañana,
     * la venta historica conserve el precio con el que se vendio.
     */
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    /** Dueña de la relacion: la columna venta_id esta en detalles_venta. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}
