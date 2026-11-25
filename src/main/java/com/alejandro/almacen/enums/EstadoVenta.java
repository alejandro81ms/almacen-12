package com.alejandro.almacen.enums;


import com.alejandro.almacen.exceptions.DatoInvalidoException;
import com.alejandro.almacen.utils.StringCustomUtils;
import com.alejandro.almacen.utils.ValoresNumericosUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoVenta {
    REGISTRADA(1L, "Registrada"),
    CANCELADA(0L, "Cancelada");

    private final Long codigo;

    private final String descripcion;

    public static EstadoVenta obtenerEstadoVentaPorDescripcion(String descripcion){

        StringCustomUtils.validarNoVacio(descripcion, "la descripción es requerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (EstadoVenta estadoVenta : values()) {
            if (StringCustomUtils.normalizarTexto(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;
        }

        throw new DatoInvalidoException("No existe un estado de venta con la descripción" + descripcion);
    }

    public static EstadoVenta obtenerEstadoVentaPorCodigo(Long codigo) {
        ValoresNumericosUtils.validarNumeroRequerido(codigo, "El codigo es requerido");

        for (EstadoVenta estadoVenta : values()) {
            if (estadoVenta.codigo.equals(codigo))
                return estadoVenta;
        }
        throw new DatoInvalidoException("No existe un estado de venta con el código: " + codigo);
    }


}
