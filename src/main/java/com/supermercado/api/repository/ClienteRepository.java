package com.supermercado.api.repository;

import com.supermercado.api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    /** Para update: el documento debe ser unico, salvo el propio cliente. */
    boolean existsByDocumentoAndIdNot(String documento, Long id);
}
