package com.supermercado.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Cliente del supermercado. El documento es unico (DNI, cedula...).
 */
@Getter
@Setter
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Unico: dos clientes no pueden tener el mismo documento. */
    @Column(name = "documento", nullable = false, unique = true, length = 30)
    private String documento;

    @Column(name = "telefono", length = 30)
    private String telefono;

    /** Un cliente tiene muchas ventas. */
    @OneToMany(mappedBy = "cliente")
    private List<Venta> ventas = new ArrayList<>();
}
