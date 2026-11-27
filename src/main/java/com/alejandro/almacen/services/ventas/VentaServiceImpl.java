package com.alejandro.almacen.services.ventas;

import com.alejandro.almacen.dto.ventas.DetalleVentaRequest;
import com.alejandro.almacen.dto.ventas.VentaRequest;
import com.alejandro.almacen.dto.ventas.VentaResponse;
import com.alejandro.almacen.entities.DetalleVenta;
import com.alejandro.almacen.entities.Producto;
import com.alejandro.almacen.entities.Sucursal;
import com.alejandro.almacen.entities.Venta;
import com.alejandro.almacen.enums.EstadoVenta;
import com.alejandro.almacen.exceptions.DatoInvalidoException;
import com.alejandro.almacen.exceptions.RecursoNoEncontradoException;
import com.alejandro.almacen.mappers.VentaMapper;
import com.alejandro.almacen.repositories.ProductoRepository;
import com.alejandro.almacen.repositories.SucursalRepository;
import com.alejandro.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        log.info("Listando ventas registradas");

        return ventaRepository.findByEstadoVenta(EstadoVenta.REGISTRADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        log.info("Listando ventas canceladas");

        return ventaRepository.findByEstadoVenta(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {
        Venta venta = ventaRepository.findById(id)
                .filter(v -> v.getEstadoVenta() == EstadoVenta.REGISTRADA)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta activa no encontrada con id: " + id));
        return  ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + request.idSucursal()));

        Venta venta = ventaMapper.requestAEntidad(request, LocalDate.now(), sucursal);

        for (DetalleVentaRequest detalleRequest : request.productos()){
            Producto producto = productoRepository.findById(detalleRequest.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id:" + detalleRequest.idProducto()
                    ));
            Long cantidadSolicitada = detalleRequest.cantidadProducto();

            if (cantidadSolicitada == null ||cantidadSolicitada <= 0 ||cantidadSolicitada > Integer.MAX_VALUE){
                throw new DatoInvalidoException(
                        "La cantidad del producto está fuera del rango permitido");
            }

            int cantidad = cantidadSolicitada.intValue();
            producto.descontarCantidad(cantidad);

            DetalleVenta detalle = DetalleVenta.crear(producto,cantidad);
            detalle.asignarVenta(venta);
            venta.agregarDetalle(detalle);

        }

        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Venta registrada con id: {}", ventaGuardada.getId());

        return ventaMapper.entidadAResponse(ventaGuardada);
    }

    @Override
    public void cancelar(Long id) {
        Venta venta = obtenerVentaOException(id);
        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVentas()){
            detalle.getProducto().aumentarCantidad(detalle.getCantidadProducto());
        }
        log.info("Venta cancelada con id: {}", venta.getId());

    }


    private Venta obtenerVentaOException(Long id) {

        log.info("Buscando venta con id: {}", id);

        return ventaRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Venta no encontrado con id: " + id));

    }
}
