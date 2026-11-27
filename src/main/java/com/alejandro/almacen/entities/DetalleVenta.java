package com.alejandro.almacen.entities;


import com.alejandro.almacen.exceptions.DatoInvalidoException;
import com.alejandro.almacen.utils.ValoresNumericosUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "DETALLES_VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_VENTA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENTA", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;

    @Column(name = "CANTIDAD_PRODUCTO", nullable = false)
    private Integer cantidadProducto;

    @Column(name = "PRECIO_PRODUCTO", nullable = false)
    private BigDecimal precioProducto;


    public void asignarVenta(Venta venta){
        if (venta == null)
            throw new DatoInvalidoException("La venta es requerida");

        this.venta = venta;
    }

    public static void validarDatos(Integer cantidadProducto, BigDecimal precioProducto){

        ValoresNumericosUtils.validarBigDecimalPositivo(precioProducto,
                "El precio es requerido y debe ser positivo");

        ValoresNumericosUtils.validarEnteroPositivo(cantidadProducto,
                "La cantidad es requerida y debe ser positiva");
    }

    public static DetalleVenta crear(Producto producto, Integer cantidadProducto){
        if (producto == null)
            throw new DatoInvalidoException("El producto es requerido");

        BigDecimal precioProducto = producto.getPrecio();
        validarDatos(cantidadProducto, precioProducto);

        return DetalleVenta.builder()
                .producto(producto)
                .cantidadProducto(cantidadProducto)
                .precioProducto(precioProducto)
                .build();

    }

    public BigDecimal calcularSubtotal(){
        return  precioProducto.multiply(BigDecimal.valueOf(cantidadProducto));
    }
}