package com.example.demo.producto;

import com.example.demo.common.entity.Producto;
import com.example.demo.common.service.ProductoService;
import com.example.demo.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agregar-producto")
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
}
