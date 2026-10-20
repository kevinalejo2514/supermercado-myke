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
 * Una categoria agrupa productos (ej: "Bebidas", "Lacteos").
 *
 * @Entity le dice a Hibernate: "esta clase se guarda en una tabla".
 * @Table(name = "categorias") define el nombre exacto de esa tabla.
 */
@Getter
@Setter
@Entity
@Table(name = "categorias")
public class Categoria {

    /** Clave primaria autogenerada por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Obligatorio: la regla de negocio exige nombre. */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    /**
     * Relacion uno-a-muchos: una categoria tiene muchos productos.
     * mappedBy = "categoria" indica que la dueña de la relacion es la tabla
     * productos (ella lleva la columna categoria_id).
     * No usamos cascade: borrar la categoria NO debe borrar sus productos,
     * la validacion correspondiente se hace en el servicio (409).
     */
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();
}
