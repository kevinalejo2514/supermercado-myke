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
 * Producto del supermercado.
 *
 * Relaciones: muchos productos pertenecen a una categoria
 * y muchos productos los provee un proveedor (muchos-a-uno).
 */
@Getter
@Setter
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    /** BigDecimal en vez de double: el dinero no debe tener errores de coma flotante. */
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    /** Unico en toda la tabla: dos productos no pueden compartir codigo de barras. */
    @Column(name = "codigo_barras", nullable = false, unique = true, length = 50)
    private String codigoBarras;

    /**
     * Muchos-a-uno con Categoria.
     * @JoinColumn declara la columna fisica categoria_id en la tabla productos.
     * La categoria es opcional (el enunciado solo obliga nombre, precio, stock y codigo).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /** Muchos-a-uno con Proveedor. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
