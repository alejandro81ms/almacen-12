package com.alejandro.almacen.dto.ventas;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Detalle de un producto dentro de una venta")
public record DetalleVentaResponse(

        @Schema(description = "ID del producto", example = "1")
        Long idProducto,

        @Schema(description = "Nombre del prodcto", example = "Laptop Gamer")
        String nombreProducto,

        @Schema(description = "Cantidad de unidades ventidas", example = "10")
        Integer cantidadProducto,

        @Schema(description = "Precio unitario del producto", example = "1500.00")
        BigDecimal precioProducto,

        @Schema(description = "Subtotal del producto", example = "1500.00")
        BigDecimal subtotal
) {
}
