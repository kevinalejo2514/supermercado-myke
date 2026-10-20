package com.supermercado.api.repository;

import com.supermercado.api.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByClienteId(Long clienteId);

    /**
     * Rango de fechas: inicio incluido, fin NO incluido (ya le sumamos 1 dia
     * en el servicio para que la fecha "fin" se tome completa).
     */
    List<Venta> findByFechaGreaterThanEqualAndFechaLessThan(LocalDateTime inicio, LocalDateTime fin);
}
