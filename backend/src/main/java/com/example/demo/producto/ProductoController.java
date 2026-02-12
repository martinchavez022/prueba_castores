package com.example.demo.producto;

import com.example.demo.common.entity.Producto;
import com.example.demo.common.exception.InvalidQuantityException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.exception.UserCanNotAccess;
import com.example.demo.common.service.ProductoService;
import com.example.demo.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * CREAR UN PRODUCTO
     *
     * Administrador: X
     */
    @PostMapping("/{id_usuario}")
    public ResponseEntity<ApiResponse<Producto>> createProducto(
            @RequestBody Producto producto,
            @PathVariable int id_usuario
    ) {
        try {
            Producto savedProducto = productoService.saveProducto(producto, id_usuario);
            return new ResponseEntity<>(ApiResponse.ok("Producto creado exitosamente", savedProducto),
                    HttpStatus.CREATED);
        } catch (UserCanNotAccess e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * ACTUALIZAR CANTIDAD DE UN PRODUCTO (solo sumar, no resta)
     *
     * Administrador: X
     */
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

    /**
     * ACTUALIZAR ESTADO DE PRODUCTO
     */
    @GetMapping("/{id}/{id_usuario}")
    public ResponseEntity<ApiResponse<Producto>> actualizarEstadoProducto (
            @PathVariable Long id,
            @PathVariable int id_usuario
    )
    {
        try {
            Producto updateProducto = productoService.actualizarEstatus(id,id_usuario);
            return new ResponseEntity<>(ApiResponse.ok("Estatus actualizado", updateProducto), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * VER LISTA DE PRODUCTOS
     *
     * Administrador: X
     * Almacenista: X
     */
    @GetMapping("/obtener")
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerProductos () {
        try {
            List<Producto> productos = productoService.obtenerProductos();
            return new ResponseEntity<>(ApiResponse.ok("Lista de productos obtenida exitosamente", productos), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * RESTAR INVENTARIO DEL ALMACEN
     *
     * Almacenista: X
     */
    @PutMapping("/restar/{id}/{id_usuario}")
    public ResponseEntity<ApiResponse<Producto>> restarProducto(
            @PathVariable Long id,
            @PathVariable int id_usuario,
            @RequestBody int cantidad)
    {
        try {
            Producto updateProducto = productoService.restarProducto(id, cantidad, id_usuario);
            return new ResponseEntity<>(ApiResponse.ok("Cantidad de producto actualizada!", updateProducto), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidQuantityException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserCanNotAccess e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
