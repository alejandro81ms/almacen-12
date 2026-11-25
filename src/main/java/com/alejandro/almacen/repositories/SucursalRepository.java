package com.alejandro.almacen.repositories;


import com.alejandro.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    //SELECT COUNT(*) FROM SUCURSALES WHERE NOMBRE = ? AND ID <> ?
    boolean existsByNombreIgnoreCase(String nombre);


    //SELECT COUNT(*) FROM SUCURSALES WHERE NOMBRE = ? AND ID <> ?
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
