package com.alejandro.almacen.controllers;


import com.alejandro.almacen.docs.ProblemaDoc;
import com.alejandro.almacen.dto.productos.ProductoRequest;
import com.alejandro.almacen.dto.productos.ProductoResponse;
import com.alejandro.almacen.services.productos.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión del inventario de productos")
// Errores que pueden ocurrir en cualquier endpoint:
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar prouctos", description = "Todos los filtros son opcionales")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<ProductoResponse>> listar(
            @Parameter(description = "Búsqueda por nombre", example = "Laptop")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Búsqueda por categoría", example = "Electrónico")
            @RequestParam(required = false) String categoria,

            @Parameter(description = "Búqueda por precio mínimo", example = "1000")
            @RequestParam(required = false) BigDecimal precioMin,

            @Parameter(description = "Búsqueda por precio máximo", example = "2000")
            @RequestParam(required = false) BigDecimal precioMax
            ){
        return  ResponseEntity.ok(productoService.listar(nombre, categoria, precioMin, precioMax));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> obtenerPorId(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id

    ){
        return  ResponseEntity.ok(productoService.obtenerPorId(id));
    }


    @PostMapping
    @Operation(summary = "Registrar un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto registrado")
    public ResponseEntity<ProductoResponse> registrar(
            @Valid @RequestBody ProductoRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> actualizar(

            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser postivo") Long id,

            @Valid  @RequestBody ProductoRequest request
    ){
        return ResponseEntity.ok(productoService.actualizar(request, id));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @ApiResponse(responseCode = "409", description = "El producto está en uso y no puede eliminarse",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> eliminar(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID deber ser positivo") Long id
    ){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
