package com.alejandro.almacen.repositories;


import com.alejandro.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.alejandro.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    //SELECT COUNT(*) FROM SUCURSALES WHERE NOMBRE = ? AND ID <> ?
    boolean existsByNombreIgnoreCase(String nombre);


    //SELECT COUNT(*) FROM SUCURSALES WHERE NOMBRE = ? AND ID <> ?
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);


    @Query("""
          SELECT new com.alejandro.almacen.dto.reportes.ReporteVentasSucursalResponse (
            s.id,
            s.nombre,
            COALESCE(SUM(d.precioProducto * d.cantidadProducto) ,0BD),
            COALESCE(SUM(d.cantidadProducto), 0L)
          )
          FROM Sucursal s
          LEFT JOIN Venta v ON v.sucursal = s
            AND v.estadoVenta = com.alejandro.almacen.enums.EstadoVenta.REGISTRADA
          LEFT JOIN v.detalleVentas d
          GROUP BY s.id, s.nombre
          ORDER BY s.id
          """)
    List<ReporteVentasSucursalResponse> obtenerReporteVentasPorSucursal();
}
