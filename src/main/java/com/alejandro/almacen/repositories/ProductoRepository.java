package com.alejandro.almacen.repositories;

import com.alejandro.almacen.entities.Producto;
import com.alejandro.almacen.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {



    @Query(
            """
            SELECT  p
            FROM    Producto p
            WHERE   (:nombre IS NULL
                    OR LOWER(p.nombre) LIKE CONCAT ('%', LOWER(:nombre), '%'))
                AND (:categoria IS NULL OR p.categoria = :categoria)
                AND (:precioMin IS NULL OR p.precio >= :precioMin)
                AND (:precioMax IS NULL OR p.precio <= :precioMax)
            """
    )
    List<Producto> listar(
            String nombre,
            Categoria categoria,
            BigDecimal precioMin,
            BigDecimal precioMax
    );
}
