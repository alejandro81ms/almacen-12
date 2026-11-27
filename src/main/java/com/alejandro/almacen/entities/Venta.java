package com.alejandro.almacen.entities;

import com.alejandro.almacen.enums.EstadoVenta;
import com.alejandro.almacen.exceptions.ConflictoException;
import com.alejandro.almacen.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalleVentas = new ArrayList<>();


    public void agregarDetalle(DetalleVenta detalleVenta){

        if (detalleVenta == null)
            throw new DatoInvalidoException("El detalle de la venta es requerida");

        this.detalleVentas.add(detalleVenta);
    }


    public void cancelar(){

        if(this.estadoVenta == EstadoVenta.CANCELADA)
            throw new ConflictoException("La venta ya está cancelada");

        this.estadoVenta = EstadoVenta.CANCELADA;

    }

    public static void validarDatos(LocalDate fecha, Sucursal sucursal){

        if (fecha == null)
            throw new DatoInvalidoException("La fecha es requerida");

        if (sucursal == null)
            throw new DatoInvalidoException("La sucursal es requerida");
    }

    public static Venta crear(
            LocalDate fecha, Sucursal sucursal
    ){
        validarDatos(fecha, sucursal);
        return Venta.builder()
                .fecha(fecha)
                .sucursal(sucursal)
                .estadoVenta(EstadoVenta.REGISTRADA)
                .detalleVentas(new ArrayList<>())
                .build();
    }

    public BigDecimal calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVenta detalle : detalleVentas){
            total = total.add(detalle.calcularSubtotal());
        }
        return  total;
    }

}

