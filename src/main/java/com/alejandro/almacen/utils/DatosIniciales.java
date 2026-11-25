package com.alejandro.almacen.utils;

import com.alejandro.almacen.entities.Producto;
import com.alejandro.almacen.entities.Sucursal;
import com.alejandro.almacen.enums.Categoria;
import com.alejandro.almacen.repositories.ProductoRepository;
import com.alejandro.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    private final SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) throws Exception {

        if (productoRepository.count()== 0){
            productoRepository.saveAll(List.of(
                    new Producto(
                            null,
                            "Laptop Gamer",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(25),
                            50000),

                    new Producto(
                            null,
                            "Mouse Inalámbrico",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(25),
                            5000),

                    new Producto(
                            null,
                            "Camiseta Deportiva",
                            Categoria.ROPA,
                            BigDecimal.valueOf(25),
                            500)
            ));



        }
        if (sucursalRepository.count() == 0){
            sucursalRepository.saveAll(List.of(
                new Sucursal(
                        null,
                        "Sucursal central",
                        "Av. Principal 123"
                ),
                new Sucursal(
                        null,
                        "Sucursal norte",
                        "Calle Norte 456"
                ),
                new Sucursal(
                        null,
                        "Sucursal sur",
                        "Calle sur 789"
                )
            ));
            log.info("Sucursales de prueba cargadas correctamente");
        }
    }
}

