package com.alejandro.almacen.services.ventas;

import com.alejandro.almacen.dto.ventas.VentaRequest;
import com.alejandro.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listar();

    List<VentaResponse> listarCanceladas();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    void cancelar(Long id);
}
