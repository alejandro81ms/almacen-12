package com.alejandro.almacen.mappers;

import com.alejandro.almacen.dto.productos.ProductoRequest;
import com.alejandro.almacen.dto.productos.ProductoResponse;
import com.alejandro.almacen.entities.Producto;
import com.alejandro.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto requestAEntidad(ProductoRequest request, Categoria categoria){
        return request == null
                ? null
                : Producto.crear(
                        request.nombre(),
                        categoria,
                        request.precio(),
                        request.cantidad());
    }

    public ProductoResponse entidadAResponse(Producto producto){
        return producto == null
                ? null
                : new ProductoResponse(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getCategoria().getDescripcion(),
                        producto.getPrecio(),
                        producto.getCantidad());
    }
}
