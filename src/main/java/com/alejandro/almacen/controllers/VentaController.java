package com.alejandro.almacen.controllers;


import com.alejandro.almacen.docs.ProblemaDoc;
import com.alejandro.almacen.dto.ventas.VentaRequest;
import com.alejandro.almacen.dto.ventas.VentaResponse;
import com.alejandro.almacen.services.ventas.VentaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Venta registrada")
    @ApiResponse(responseCode = "404",description = "Sucursal o producto no encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)
            )
    )
    @ApiResponse(responseCode = "400",description = "Datos inválidos o existencias insuficientes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)
            )
    )
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrar(request));
    }


    @PatchMapping("/{id}/cancelar")
    @ApiResponse(responseCode = "204", description = "Venta cancelada")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)
            )
    )
    @ApiResponse(responseCode = "409", description = "La venta ya está cancelada",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)
            )
    )
    public ResponseEntity<Void> cancelar (@PathVariable("id") Long id){
        ventaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Listado de ventas registradas")
    public ResponseEntity<List<VentaResponse>> listar(){
        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/canceladas")
    @ApiResponse(responseCode = "200", description = "Listado de ventas canceladas")
    public ResponseEntity<List<VentaResponse>> listarCanceladas(){
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Venta activa encontrada")
    @ApiResponse(responseCode = "404",description = "Venta activa no encontrada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)
            )
    )
    public ResponseEntity<VentaResponse> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }
}
