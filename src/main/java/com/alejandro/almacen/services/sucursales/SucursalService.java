package com.alejandro.almacen.services.sucursales;

import com.alejandro.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.alejandro.almacen.dto.sucursales.SucursalRequest;
import com.alejandro.almacen.dto.sucursales.SucursalResponse;

import java.util.List;

public interface SucursalService {
    List<SucursalResponse> listar();

    List<ReporteVentasSucursalResponse> obtenerReporteVentasPorSucursal();

    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);
}
