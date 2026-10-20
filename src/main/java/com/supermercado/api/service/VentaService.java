package com.supermercado.api.service;

import com.supermercado.api.dto.DetalleVentaRequest;
import com.supermercado.api.dto.VentaRequest;
import com.supermercado.api.dto.VentaResponse;
import com.supermercado.api.entity.Cliente;
import com.supermercado.api.entity.DetalleVenta;
import com.supermercado.api.entity.Producto;
import com.supermercado.api.entity.Venta;
import com.supermercado.api.exception.BadRequestException;
import com.supermercado.api.exception.ResourceNotFoundException;
import com.supermercado.api.repository.ClienteRepository;
import com.supermercado.api.repository.ProductoRepository;
import com.supermercado.api.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Logica de negocio de las ventas: el corazon del proyecto.
 *
 * Todas las operaciones son @Transactional: si algo falla a mitad de camino
 * (ej: no hay stock del segundo producto), la transaccion se DESHACE y no
 * queda ninguna venta a medias ni stock descontado.
 */
@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventas;
    private final ClienteRepository clientes;
    private final ProductoRepository productos;

    /**
     * POST /api/ventas - Registrar una venta.
     *
     * Pasos:
     *  1. Verificar que el cliente y los productos existan (404 si no).
     *  2. Verificar cantidades > 0 y stock suficiente (400).
     *  3. Tomar el precio ACTUAL del producto.
     *  4. Calcular subtotal y total.
     *  5. Descontar el stock.
     *  6. Guardar la venta con la fecha actual.
     */
    @Transactional
    public VentaResponse registrar(VentaRequest request) {

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BadRequestException("La venta debe tener al menos un detalle");
        }

        Cliente cliente = clientes.findById(request.getClienteId())
                .orElseThrow(() -> ResourceNotFoundException.porId("Cliente", request.getClienteId()));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaRequest dr : request.getDetalles()) {

            if (dr.getCantidad() == null || dr.getCantidad() <= 0) {
                throw new BadRequestException("La cantidad debe ser mayor a 0");
            }

            Producto producto = productos.findById(dr.getProductoId())
                    .orElseThrow(() -> ResourceNotFoundException.porId("Producto", dr.getProductoId()));

            // REGLA: no se puede vender mas de lo que hay en stock
            if (producto.getStock() < dr.getCantidad()) {
                throw new BadRequestException(
                        "Stock insuficiente para el producto '" + producto.getNombre()
                                + "': disponible " + producto.getStock()
                                + ", solicitado " + dr.getCantidad());
            }

            // Precio ACTUAL del producto (no lo envia el cliente)
            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(dr.getCantidad()));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(dr.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);
            venta.getDetalles().add(detalle);

            // 5. Descontar stock
            producto.setStock(producto.getStock() - dr.getCantidad());

            total = total.add(subtotal);
        }

        venta.setTotal(total); // 4. Total de la venta

        Venta guardada = ventas.save(venta); // 6. Se guarda venta + detalles (cascade)
        return VentaResponse.from(guardada);
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        return ventas.findAll().stream()
                .map(VentaResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaResponse obtener(@NonNull Long id) {
        return VentaResponse.from(buscarEntidad(id));
    }

    /** GET /api/ventas/cliente/{clienteId} */
    @Transactional(readOnly = true)
    public List<VentaResponse> listarPorCliente(@NonNull Long clienteId) {
        if (!clientes.existsById(clienteId)) {
            throw ResourceNotFoundException.porId("Cliente", clienteId);
        }
        return ventas.findByClienteId(clienteId).stream()
                .map(VentaResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * GET /api/ventas/fecha?inicio=2026-10-01&fin=2026-10-31
     * El rango es por FECHA COMPLETA: se incluye el dia "fin" entero,
     * por eso se busca desde inicio 00:00 hasta fin+1 dia 00:00 (exclusivo).
     */
    @Transactional(readOnly = true)
    public List<VentaResponse> listarPorFecha(LocalDate inicio, LocalDate fin) {
        if (fin.isBefore(inicio)) {
            throw new BadRequestException("La fecha fin no puede ser anterior a la fecha inicio");
        }
        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay();
        return ventas.findByFechaGreaterThanEqualAndFechaLessThan(desde, hasta).stream()
                .map(VentaResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * DELETE /api/ventas/{id}
     * REGLA: al eliminar una venta se RESTITUYE el stock de cada producto.
     */
    @Transactional
    public void eliminar(@NonNull Long id) {
        Venta venta = buscarEntidad(id);

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
        }

        ventas.delete(venta);
    }

    private Venta buscarEntidad(@NonNull Long id) {
        return ventas.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.porId("Venta", id));
    }
}
