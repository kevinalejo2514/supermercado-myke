package com.supermercado.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Venta realizada por un cliente.
 *
 * Relaciones:
 *  - muchos-a-uno con Cliente (muchas ventas pertenecen a un cliente)
 *  - uno-a-muchos con DetalleVenta (una venta tiene varios detalles)
 */
@Getter
@Setter
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora en que se registro la venta. */
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * Una venta tiene muchos detalles.
     *
     * cascade = ALL: cuando guardamos la venta, Hibernate guarda tambien sus
     * detalles automaticamente (no hace falta guardarlos uno por uno).
     * orphanRemoval = true: si quitamos un detalle de la lista, se borra de la BD.
     */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();
}
