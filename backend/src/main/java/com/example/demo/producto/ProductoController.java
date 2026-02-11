package com.example.demo.producto;

import com.example.demo.common.entity.Producto;
import com.example.demo.common.exception.InvalidQuantityException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.service.ProductoService;
import com.example.demo.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Producto>> createProducto(@RequestBody Producto producto) {
        Producto savedProducto = productoService.saveProducto(producto);
        return new ResponseEntity<>(ApiResponse.ok("Producto creado exitosamente", savedProducto),
                HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}/{id_usuario}/cantidad")
    public ResponseEntity<ApiResponse<Producto>> actualizarProducto(
            @PathVariable Long id,
            @PathVariable int id_usuario,
            @RequestBody int cantidad)
    {
        try {
            Producto updateProducto = productoService.actualizarCantidadProducto(id, cantidad, id_usuario);
            return new ResponseEntity<>(ApiResponse.ok("Cantidad de producto actualizada!", updateProducto), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidQuantityException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> actualizarEstadoProducto (
            @PathVariable Long id)
    {
        try {
            Producto updateProducto = productoService.actualizarEstatus(id);
            return new ResponseEntity<>(ApiResponse.ok("Estatus actualizado", updateProducto), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
