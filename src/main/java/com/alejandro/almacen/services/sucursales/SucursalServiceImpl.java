package com.alejandro.almacen.services.sucursales;


import com.alejandro.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.alejandro.almacen.dto.sucursales.SucursalRequest;
import com.alejandro.almacen.dto.sucursales.SucursalResponse;
import com.alejandro.almacen.entities.Sucursal;
import com.alejandro.almacen.exceptions.ConflictoException;
import com.alejandro.almacen.exceptions.RecursoNoEncontradoException;
import com.alejandro.almacen.mappers.SucursalMapper;
import com.alejandro.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService{

    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        log.info("Listando todas las sucursales...");

        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> obtenerReporteVentasPorSucursal() {
        log.info("Consultando el reporte de ventas por sucursal");
        return sucursalRepository.obtenerReporteVentasPorSucursal();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalOException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {

        log.info("Registrado nuevo producto...");
        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);

        sucursalRepository.saveAndFlush(sucursal);
        log.info("Nueva sucursa {} registrada", sucursal.getNombre());
        return sucursalMapper.entidadAResponse(sucursal);
    }


    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);
        log.info("Actualizando sucursal con id: {}", id);

        validarDatosUnicos(request, id);

        sucursal.actualizar(request.nombre(), request.direccion());
        sucursalRepository.saveAndFlush(sucursal);

        log.info("Sucursal con id: {} actualizada", id);
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);

        log.info("Eliminando sucursal con id: {}", id);

        sucursalRepository.delete(sucursal);
        sucursalRepository.flush();

        log.info("Sucursal con id: {} liminado", id);
    }

    private Sucursal obtenerSucursalOException(Long id){

        log.info("Buscando sucursal con id: {}", id);

        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + id));
    }


    private void validarDatosUnicos(SucursalRequest request){
        log.info("Validando nombre único...");

        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException("Ya existe una sucursal con el nombre de:" + request.nombre());
    }

    private void validarDatosUnicos(SucursalRequest request, Long id){
        log.info("Validando cambio en nombre único...");

        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new ConflictoException("Ya existe una sucursal con el nombre de:" + request.nombre());
    }
}



