package com.alejandro.almacen.mappers;


import com.alejandro.almacen.dto.ventas.DetalleVentaResponse;
import com.alejandro.almacen.dto.ventas.VentaRequest;
import com.alejandro.almacen.dto.ventas.VentaResponse;
import com.alejandro.almacen.entities.DetalleVenta;
import com.alejandro.almacen.entities.Sucursal;
import com.alejandro.almacen.entities.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VentaMapper {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SucursalMapper sucursalMapper;

    private DetalleVentaResponse convertirDetalle(DetalleVenta detalleVenta){
        return new DetalleVentaResponse(
                detalleVenta.getProducto().getId(),
                detalleVenta.getProducto().getNombre(),
                detalleVenta.getCantidadProducto(),
                detalleVenta.getPrecioProducto(),
                detalleVenta.calcularSubtotal()
        );
    }

    public Venta requestAEntidad(VentaRequest request, LocalDate fecha, Sucursal sucursal){
        return request == null
                ? null
                :Venta.crear(fecha, sucursal);
    }


    public VentaResponse entidadAResponse(Venta venta){

        List<DetalleVentaResponse> detalles = venta.getDetalleVentas()
                .stream()
                .map(this::convertirDetalle)
                .toList();


        return new VentaResponse(
                        venta.getId(),
                        venta.getFecha().format(FORMATO_FECHA),
                        venta.getEstadoVenta().getDescripcion(),
                        sucursalMapper.entidadAResponse(venta.getSucursal()),
                        detalles,
                        venta.calcularTotal()
        );
    }
}
