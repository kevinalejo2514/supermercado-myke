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
 * Empresa que suministra productos al supermercado.
 */
@Getter
@Setter
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    /** Un proveedor suministra muchos productos. */
    @OneToMany(mappedBy = "proveedor")
    private List<Producto> productos = new ArrayList<>();
}
