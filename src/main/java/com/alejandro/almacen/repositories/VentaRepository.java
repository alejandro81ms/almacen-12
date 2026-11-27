package com.alejandro.almacen.repositories;

import com.alejandro.almacen.entities.Venta;
import com.alejandro.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);
}
